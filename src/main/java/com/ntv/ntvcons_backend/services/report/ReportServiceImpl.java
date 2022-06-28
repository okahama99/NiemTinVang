package com.ntv.ntvcons_backend.services.report;

import com.ntv.ntvcons_backend.constants.SearchOption;
import com.ntv.ntvcons_backend.dtos.report.ReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportUpdateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportReadDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportUpdateDTO;
import com.ntv.ntvcons_backend.entities.Report;
import com.ntv.ntvcons_backend.repositories.ReportRepository;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import com.ntv.ntvcons_backend.services.reportDetail.ReportDetailService;
import com.ntv.ntvcons_backend.services.reportType.ReportTypeService;
import com.ntv.ntvcons_backend.services.taskReport.TaskReportService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ProjectService projectService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Autowired
    private ReportTypeService reportTypeService;
    @Autowired
    private ReportDetailService reportDetailService;
    @Autowired
    private TaskReportService taskReportService;

    /* CREATE */
    @Override
    public Report createReport(Report newReport) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!projectService.existsById(newReport.getProjectId())) {
            errorMsg += "No Project found with Id: '" + newReport.getProjectId()
                    + "'. Which violate constraint: FK_Report_Project. ";
        }

        if (!userService.existsById(newReport.getReporterId())) {
            errorMsg += "No User found with Id: '" + newReport.getReporterId()
                    + "'. Which violate constraint: FK_Report_User. ";
        }

        if (!reportTypeService.existsById(newReport.getReportTypeId())) {
            errorMsg += "No ReportType found with Id: '" + newReport.getReportTypeId()
                    + "'. Which violate constraint: FK_Report_ReportType. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        /* TODO: create EntityWrapper for report */

        return reportRepository.saveAndFlush(newReport);
    }
    @Override
    public ReportReadDTO createReportByDTO(ReportCreateDTO newReportDTO) throws Exception {
        modelMapper.typeMap(ReportCreateDTO.class, Report.class)
                .addMappings(mapper -> {
                    mapper.skip(Report::setReportDate);});

        Report newReport = modelMapper.map(newReportDTO, Report.class);
        newReport.setReportDate(LocalDateTime.parse(newReportDTO.getReportDate(), dateTimeFormatter));

        newReport = createReport(newReport);

        ReportReadDTO reportDTO = modelMapper.map(newReport, ReportReadDTO.class);

        /* Get associated ReportType */
        reportDTO.setReportType(reportTypeService.getDTOById(newReport.getReportTypeId()));

        /* Create associated ReportDetail; Set required FK reportId */
        List<ReportDetailCreateDTO> newReportDetailDTOList = newReportDTO.getReportDetailList();
        if (newReportDetailDTOList != null && !newReportDetailDTOList.isEmpty()) {
            reportDTO.setReportDetailList(
                    reportDetailService.createBulkReportDetailByDTOList(
                            newReportDetailDTOList.stream()
                                    .peek(newReportDetailDTO -> newReportDetailDTO.setReportId(reportDTO.getReportId()))
                                    .collect(Collectors.toList())));
        }

        /* Create associated TaskReport; Set required FK reportId */
        List<TaskReportCreateDTO> newTaskReportDTOList = newReportDTO.getTaskReportList();
        if (newTaskReportDTOList != null && !newTaskReportDTOList.isEmpty()) {
            reportDTO.setTaskReportList(
                    taskReportService.createBulkTaskReportByDTOList(
                            newTaskReportDTOList.stream()
                                    .peek(newReportDetailDTO -> newReportDetailDTO.setReportId(reportDTO.getReportId()))
                                    .collect(Collectors.toList())));
        }

        return reportDTO;
    }

    /* READ */
    @Override
    public Page<Report> getPageAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Report> reportPage = reportRepository.findAllByIsDeletedIsFalse(paging);

        if (reportPage.isEmpty()) {
            return null;
        }

        return reportPage;
    }
    @Override
    public List<ReportReadDTO> getAllDTOInPaging(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Page<Report> reportPage = getPageAll(pageNo, pageSize, sortBy, sortType);

        if (reportPage == null) {
            return null;
        }

        List<Report> reportList = reportPage.getContent();

        if (reportList.isEmpty()) {
            return null;
        }

        int totalPage = reportPage.getTotalPages();

        Set<Long> reportIdSet = new HashSet<>();
        Set<Long> reportTypeIdSet = new HashSet<>();

        for (Report report : reportList) {
            reportIdSet.add(report.getReportId());
            reportTypeIdSet.add(report.getReportTypeId());
        }

        /* Get associated ReportType */
        Map<Long, ReportTypeReadDTO> reportTypeIdReportTypeDTOMap =
                reportTypeService.mapReportTypeIdReportTypeDTOByIdIn(reportTypeIdSet);

        /* Get associated ReportDetail */
        Map<Long, List<ReportDetailReadDTO>> reportIdReportDetailDTOListMap =
                reportDetailService.mapReportIdReportDetailDTOListByReportIdIn(reportIdSet);

        /* Get associated TaskReport */
        Map<Long, List<TaskReportReadDTO>> reportIdTaskReportDTOListMap =
                taskReportService.mapReportIdTaskReportDTOListByReportIdIn(reportIdSet);

        return reportList.stream()
                .map(report -> {
                    ReportReadDTO reportDTO =
                            modelMapper.map(report, ReportReadDTO.class);

                    reportDTO.setReportType(reportTypeIdReportTypeDTOMap.get(report.getReportTypeId()));
                    reportDTO.setReportDetailList(reportIdReportDetailDTOListMap.get(report.getReportId()));
                    reportDTO.setTaskReportList(reportIdTaskReportDTOListMap.get(report.getReportId()));
                    reportDTO.setTotalPage(totalPage);

                    return reportDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(long reportId) throws Exception {
        return reportRepository.existsByReportIdAndIsDeletedIsFalse(reportId);
    }
    @Override
    public Report getById(long reportId) throws Exception {
        return reportRepository
                .findByReportIdAndIsDeletedIsFalse(reportId)
                .orElse(null);
    }
    @Override
    public ReportReadDTO getDTOById(long reportId) throws Exception {
        Report report = getById(reportId);

        if (report == null) {
            return null;
        }

        ReportReadDTO reportDTO = modelMapper.map(report, ReportReadDTO.class);

        /* Get associated ReportType */
        reportDTO.setReportType(reportTypeService.getDTOById(report.getReportTypeId()));

        /* Get associated ReportDetail */
        reportDTO.setReportDetailList(reportDetailService.getAllDTOByReportId(reportId));

        /* Get associated TaskReport */
        reportDTO.setTaskReportList(taskReportService.getAllDTOByReportId(reportId));

        return reportDTO;
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> reportIdCollection) throws Exception {
        return reportRepository.existsAllByReportIdInAndIsDeletedIsFalse(reportIdCollection);
    }
    @Override
    public List<Report> getAllByIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByReportIdInAndIsDeletedIsFalse(reportIdCollection);

        if (reportList.isEmpty()) {
            return null;
        }

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<Report> reportList = getAllByIdIn(reportIdCollection);

        if (reportList == null) {
            return null;
        }

        Set<Long> reportIdSet = new HashSet<>();
        Set<Long> reportTypeIdSet = new HashSet<>();

        for (Report report : reportList) {
            reportIdSet.add(report.getReportId());
            reportTypeIdSet.add(report.getReportTypeId());
        }

        /* Get associated ReportType */
        Map<Long, ReportTypeReadDTO> reportTypeIdReportTypeDTOMap =
                reportTypeService.mapReportTypeIdReportTypeDTOByIdIn(reportTypeIdSet);

        /* Get associated ReportDetail */
        Map<Long, List<ReportDetailReadDTO>> reportIdReportDetailDTOListMap =
                reportDetailService.mapReportIdReportDetailDTOListByReportIdIn(reportIdSet);

        /* Get associated TaskReport */
        Map<Long, List<TaskReportReadDTO>> reportIdTaskReportDTOListMap =
                taskReportService.mapReportIdTaskReportDTOListByReportIdIn(reportIdSet);

        return reportList.stream()
                .map(report -> {
                    ReportReadDTO reportDTO =
                            modelMapper.map(report, ReportReadDTO.class);

                    reportDTO.setReportType(reportTypeIdReportTypeDTOMap.get(report.getReportTypeId()));
                    reportDTO.setReportDetailList(reportIdReportDetailDTOListMap.get(report.getReportId()));
                    reportDTO.setTaskReportList(reportIdTaskReportDTOListMap.get(report.getReportId()));

                    return reportDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> getAllByProjectId(long projectId) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId);

        if (reportList.isEmpty()) {
            return null;
        }

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByProjectId(long projectId) throws Exception {
        List<Report> reportList = getAllByProjectId(projectId);

        if (reportList == null) {
            return null;
        }

        Set<Long> reportIdSet = new HashSet<>();
        Set<Long> reportTypeIdSet = new HashSet<>();

        for (Report report : reportList) {
            reportIdSet.add(report.getReportId());
            reportTypeIdSet.add(report.getReportTypeId());
        }

        /* Get associated ReportType */
        Map<Long, ReportTypeReadDTO> reportTypeIdReportTypeDTOMap =
                reportTypeService.mapReportTypeIdReportTypeDTOByIdIn(reportTypeIdSet);

        /* Get associated ReportDetail */
        Map<Long, List<ReportDetailReadDTO>> reportIdReportDetailDTOListMap =
                reportDetailService.mapReportIdReportDetailDTOListByReportIdIn(reportIdSet);

        /* Get associated TaskReport */
        Map<Long, List<TaskReportReadDTO>> reportIdTaskReportDTOListMap =
                taskReportService.mapReportIdTaskReportDTOListByReportIdIn(reportIdSet);

        return reportList.stream()
                .map(report -> {
                    ReportReadDTO reportDTO =
                            modelMapper.map(report, ReportReadDTO.class);

                    reportDTO.setReportType(reportTypeIdReportTypeDTOMap.get(report.getReportTypeId()));
                    reportDTO.setReportDetailList(reportIdReportDetailDTOListMap.get(report.getReportId()));
                    reportDTO.setTaskReportList(reportIdTaskReportDTOListMap.get(report.getReportId()));

                    return reportDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> getAllByReporterId(long reporterId) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByReporterIdAndIsDeletedIsFalse(reporterId);

        if (reportList.isEmpty()) {
            return null;
        }

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByReporterId(long reporterId) throws Exception {
        List<Report> reportList = getAllByReporterId(reporterId);

        if (reportList == null) {
            return null;
        }

        Set<Long> reportIdSet = new HashSet<>();
        Set<Long> reportTypeIdSet = new HashSet<>();

        for (Report report : reportList) {
            reportIdSet.add(report.getReportId());
            reportTypeIdSet.add(report.getReportTypeId());
        }

        /* Get associated ReportType */
        Map<Long, ReportTypeReadDTO> reportTypeIdReportTypeDTOMap =
                reportTypeService.mapReportTypeIdReportTypeDTOByIdIn(reportTypeIdSet);

        /* Get associated ReportDetail */
        Map<Long, List<ReportDetailReadDTO>> reportIdReportDetailDTOListMap =
                reportDetailService.mapReportIdReportDetailDTOListByReportIdIn(reportIdSet);

        /* Get associated TaskReport */
        Map<Long, List<TaskReportReadDTO>> reportIdTaskReportDTOListMap =
                taskReportService.mapReportIdTaskReportDTOListByReportIdIn(reportIdSet);

        return reportList.stream()
                .map(report -> {
                    ReportReadDTO reportDTO =
                            modelMapper.map(report, ReportReadDTO.class);

                    reportDTO.setReportType(reportTypeIdReportTypeDTOMap.get(report.getReportTypeId()));
                    reportDTO.setReportDetailList(reportIdReportDetailDTOListMap.get(report.getReportId()));
                    reportDTO.setTaskReportList(reportIdTaskReportDTOListMap.get(report.getReportId()));

                    return reportDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> getAllByReportTypeId(long reportTypeId) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByReportTypeIdAndIsDeletedIsFalse(reportTypeId);

        if (reportList.isEmpty()) {
            return null;
        }

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByReportTypeId(long reportTypeId) throws Exception {
        List<Report> reportList = getAllByReportTypeId(reportTypeId);

        if (reportList == null) {
            return null;
        }

        Set<Long> reportIdSet = new HashSet<>();

        for (Report report : reportList) {
            reportIdSet.add(report.getReportId());
        }

        /* Get associated ReportType */
        ReportTypeReadDTO reportTypeDTO = reportTypeService.getDTOById(reportTypeId);

        /* Get associated ReportDetail */
        Map<Long, List<ReportDetailReadDTO>> reportIdReportDetailDTOListMap =
                reportDetailService.mapReportIdReportDetailDTOListByReportIdIn(reportIdSet);

        /* Get associated TaskReport */
        Map<Long, List<TaskReportReadDTO>> reportIdTaskReportDTOListMap =
                taskReportService.mapReportIdTaskReportDTOListByReportIdIn(reportIdSet);

        return reportList.stream()
                .map(report -> {
                    ReportReadDTO reportDTO =
                            modelMapper.map(report, ReportReadDTO.class);

                    reportDTO.setReportType(reportTypeDTO);
                    reportDTO.setReportDetailList(reportIdReportDetailDTOListMap.get(report.getReportId()));
                    reportDTO.setTaskReportList(reportIdTaskReportDTOListMap.get(report.getReportId()));

                    return reportDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> getAllByProjectIdAndReporterId(int projectId, int reporterId) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByProjectIdAndReporterIdAndIsDeletedIsFalse(projectId, reporterId);

        if (reportList.isEmpty()) {
            return null;
        }

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByProjectIdAndReporterId(int projectId, int reporterId) throws Exception {
        List<Report> reportList = getAllByProjectIdAndReporterId(projectId, reporterId);

        if (reportList == null) {
            return null;
        }

        Set<Long> reportIdSet = new HashSet<>();
        Set<Long> reportTypeIdSet = new HashSet<>();

        for (Report report : reportList) {
            reportIdSet.add(report.getReportId());
            reportTypeIdSet.add(report.getReportTypeId());
        }

        /* Get associated ReportType */
        Map<Long, ReportTypeReadDTO> reportTypeIdReportTypeDTOMap =
                reportTypeService.mapReportTypeIdReportTypeDTOByIdIn(reportTypeIdSet);

        /* Get associated ReportDetail */
        Map<Long, List<ReportDetailReadDTO>> reportIdReportDetailDTOListMap =
                reportDetailService.mapReportIdReportDetailDTOListByReportIdIn(reportIdSet);

        /* Get associated TaskReport */
        Map<Long, List<TaskReportReadDTO>> reportIdTaskReportDTOListMap =
                taskReportService.mapReportIdTaskReportDTOListByReportIdIn(reportIdSet);

        return reportList.stream()
                .map(report -> {
                    ReportReadDTO reportDTO =
                            modelMapper.map(report, ReportReadDTO.class);

                    reportDTO.setReportType(reportTypeIdReportTypeDTOMap.get(report.getReportTypeId()));
                    reportDTO.setReportDetailList(reportIdReportDetailDTOListMap.get(report.getReportId()));
                    reportDTO.setTaskReportList(reportIdTaskReportDTOListMap.get(report.getReportId()));

                    return reportDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> getAllByReportDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Report> reportList = new ArrayList<>();

        switch (searchOption) {
            case BEFORE_DATE:
                reportList = reportRepository.findAllByReportDateBeforeAndIsDeletedIsFalse(toDate);
                break;

            case AFTER_DATE:
                reportList = reportRepository.findAllByReportDateAfterAndIsDeletedIsFalse(fromDate);
                break;

            case BETWEEN_DATE:
                reportList = reportRepository.findAllByReportDateBetweenAndIsDeletedIsFalse(fromDate, toDate);
                break;

            default:
                /* All invalid searchOption should've been caught at controller,
                   if reach here then: not normal / slip up */
                throw new IllegalArgumentException("Invalid SearchOption used for entity Report");
        }

        if (reportList.isEmpty()) {
            return null;
        }

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByReportDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Report> reportList = getAllByReportDate(searchOption, fromDate, toDate);

        if (reportList == null) {
            return null;
        }

        Set<Long> reportIdSet = new HashSet<>();
        Set<Long> reportTypeIdSet = new HashSet<>();

        for (Report report : reportList) {
            reportIdSet.add(report.getReportId());
            reportTypeIdSet.add(report.getReportTypeId());
        }

        /* Get associated ReportType */
        Map<Long, ReportTypeReadDTO> reportTypeIdReportTypeDTOMap =
                reportTypeService.mapReportTypeIdReportTypeDTOByIdIn(reportTypeIdSet);

        /* Get associated ReportDetail */
        Map<Long, List<ReportDetailReadDTO>> reportIdReportDetailDTOListMap =
                reportDetailService.mapReportIdReportDetailDTOListByReportIdIn(reportIdSet);

        /* Get associated TaskReport */
        Map<Long, List<TaskReportReadDTO>> reportIdTaskReportDTOListMap =
                taskReportService.mapReportIdTaskReportDTOListByReportIdIn(reportIdSet);

        return reportList.stream()
                .map(report -> {
                    ReportReadDTO reportDTO =
                            modelMapper.map(report, ReportReadDTO.class);

                    reportDTO.setReportType(reportTypeIdReportTypeDTOMap.get(report.getReportTypeId()));
                    reportDTO.setReportDetailList(reportIdReportDetailDTOListMap.get(report.getReportId()));
                    reportDTO.setTaskReportList(reportIdTaskReportDTOListMap.get(report.getReportId()));

                    return reportDTO;})
                .collect(Collectors.toList());
    }

    /* UPDATE */
    @Override
    public Report updateReport(Report updatedReport) throws Exception {
        Report oldReport = getById(updatedReport.getReportId());

        if (oldReport == null) {
            return null;
            /* Not found by Id, return null */
        }

        String errorMsg = "";

        if (!oldReport.getProjectId().equals(updatedReport.getProjectId())) {
            if (!projectService.existsById(updatedReport.getProjectId())) {
                errorMsg += "No Project found with Id: '" + updatedReport.getProjectId()
                        + "'. Which violate constraint: FK_Report_Project. ";
            }
        }

        if (!oldReport.getReporterId().equals(updatedReport.getReporterId())) {
            if (!userService.existsById(updatedReport.getReporterId())) {
                errorMsg += "No User found with Id: '" + updatedReport.getReporterId()
                        + "'. Which violate constraint: FK_Report_Project. ";
            }
        }

       if (!oldReport.getReportTypeId().equals(updatedReport.getReportTypeId())) {
           if (!reportTypeService.existsById(updatedReport.getReportTypeId())) {
               errorMsg += "No ReportType found with Id: '" + updatedReport.getReportTypeId()
                       + "'. Which violate constraint: FK_Report_Project. ";
           }
       }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return reportRepository.saveAndFlush(updatedReport);
    }
    @Override
    public ReportReadDTO updateReportByDTO(ReportUpdateDTO updatedReportDTO) throws Exception {
        modelMapper.typeMap(ReportUpdateDTO.class, Report.class)
                .addMappings(mapper -> {
                    mapper.skip(Report::setReportDate);});

        Report updatedReport = modelMapper.map(updatedReportDTO, Report.class);

        updatedReport.setReportDate(
                LocalDateTime.parse(updatedReportDTO.getReportDate(), dateTimeFormatter));

        updatedReport = updateReport(updatedReport);

        if (updatedReport == null) {
            return null;
        }

        ReportReadDTO reportDTO = modelMapper.map(updatedReport, ReportReadDTO.class);

        /* Get associated ReportTpe */
        reportDTO.setReportType(reportTypeService.getDTOById(updatedReport.getReportTypeId()));

        /* Create associated ReportDetail; Set required FK reportId (Just in case) */
        List<ReportDetailCreateDTO> newReportDetailDTOList = updatedReportDTO.getNewReportDetailList();
        if (newReportDetailDTOList != null && !newReportDetailDTOList.isEmpty()) {
            reportDetailService.createBulkReportDetailByDTOList(
                    newReportDetailDTOList.stream()
                            .peek(newReportDetailDTO -> newReportDetailDTO.setReportId(reportDTO.getReportId()))
                            .collect(Collectors.toList()));
        }

        /* Update associated ReportDetail */
        List<ReportDetailUpdateDTO> updatedReportDetailDTOList = updatedReportDTO.getUpdatedReportDetailList();
        if (updatedReportDetailDTOList != null && !updatedReportDetailDTOList.isEmpty()) {
            reportDetailService.updateBulkReportDetailByDTOList(updatedReportDetailDTOList);
        }

        /* Get associated ReportDetail (After insert & update) */
        reportDTO.setReportDetailList(reportDetailService.getAllDTOByReportId(updatedReport.getReportId()));

        /* Create associated TaskReport; Set required FK reportId (Just in case) */
        List<TaskReportCreateDTO> newTaskReportDTOList = updatedReportDTO.getNewTaskReportList();
        if (newTaskReportDTOList != null && !newTaskReportDTOList.isEmpty()) {
            taskReportService.createBulkTaskReportByDTOList(
                    newTaskReportDTOList.stream()
                            .peek(newReportDetailDTO -> newReportDetailDTO.setReportId(reportDTO.getReportId()))
                            .collect(Collectors.toList()));
        }

        /* Update associated TaskReport */
        List<TaskReportUpdateDTO> updatedTaskReportDTOList = updatedReportDTO.getUpdatedTaskReportList();
        if (updatedTaskReportDTOList != null && !updatedTaskReportDTOList.isEmpty()) {
            taskReportService.updateBulkTaskReportByDTOList(updatedTaskReportDTOList);
        }

        /* Get associated TaskReport (After insert & update) */
        reportDTO.setTaskReportList(taskReportService.getAllDTOByReportId(updatedReport.getReportId()));

        return reportDTO;
    }

    /* DELETE */
    @Override
    public boolean deleteReport(long reportId) throws Exception {
        Report report = getById(reportId);
        
        if (report == null) {
            return false;
            /* Not found with Id */
        }

        /* Delete all associate detail */
        reportDetailService.deleteAllByReportId(reportId);

        report.setIsDeleted(true);
        reportRepository.saveAndFlush(report);
        
        return true;
    }

    @Override
    public boolean deleteAllByProjectId(long projectId) throws Exception {
        List<Report> reportList = getAllByProjectId(projectId);

        if (reportList == null) {
            return false;
            /* Not found with projectId */
        }

        /* Set to avoid duplicate */
        Set<Long> reportIdSet = new HashSet<>();

        reportList = reportList.stream()
                .peek(report -> {
                    reportIdSet.add(report.getReportId());
                    report.setIsDeleted(true);
                })
                .collect(Collectors.toList());

        /* Delete all associate detail */
        reportDetailService.deleteAllByReportIdIn(reportIdSet);

        reportRepository.saveAllAndFlush(reportList);

        return true;
    }
}
