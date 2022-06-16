package com.ntv.ntvcons_backend.services.report;

import com.ntv.ntvcons_backend.constants.SearchOption;
import com.ntv.ntvcons_backend.dtos.report.ReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportUpdateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportReadDTO;
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

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ModelMapper modelMapper;
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

        /* TODO: verify exists projectId */
//        if (!projectService.existsById(newReport.getProjectId())) {
//            errorMsg += "No Project found with Id: " + newReport.getProjectId()
//                    + "\nWhich violate constraint: FK_Report_Project\n";
//        }

        /* TODO: verify exists userId */
//        if (!userService.existsById(newReport.getReporterId())) {
//            errorMsg += "No User found with Id: " + newReport.getReporterId()
//                    + "\nWhich violate constraint: FK_Report_User\n";
//        }

        if (!reportTypeService.existsById(newReport.getReportTypeId())) {
            errorMsg += "No ReportType found with Id: " + newReport.getReportTypeId()
                    + "\nWhich violate constraint: FK_Report_ReportType\n";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        /* TODO: create EntityWrapper for report */

        return reportRepository.saveAndFlush(newReport);
    }
    @Override
    public ReportReadDTO createReportByDTO(ReportCreateDTO newReportDTO) throws Exception {
        Report newReport = modelMapper.map(newReportDTO, Report.class);

        newReport = createReport(newReport);

        ReportReadDTO reportDTO = modelMapper.map(newReport, ReportReadDTO.class);

        /* Get associated ReportType */
        reportDTO.setReportType(reportTypeService.getDTOById(newReport.getReportTypeId()));

        /* Create associated ReportDetail */
        List<ReportDetailCreateDTO> newReportDetailDTOList = newReportDTO.getReportDetailList();
        if (newReportDetailDTOList != null && !newReportDetailDTOList.isEmpty()) {
            reportDTO.setReportDetailList(
                    reportDetailService.createBulkReportDetailByDTOList(newReportDetailDTOList));
        }

        /* Create associated TaskReport */
        List<TaskReportCreateDTO> newTaskReportDTOList = newReportDTO.getTaskReportList();
        if (newReportDetailDTOList != null && !newReportDetailDTOList.isEmpty()) {
            reportDTO.setTaskReportList(
                    taskReportService.createBulkTaskReportByDTOList(newTaskReportDTOList));
        }

        return reportDTO;
    }

    @Override
    public List<Report> createBulkReport(List<Report> newReportList) throws Exception {
        String errorMsg = "";

        Set<Long> projectIdSet = new HashSet<>();
        Set<Long> userIdSet = new HashSet<>();
        Set<Long> reportTypeIdSet = new HashSet<>();

        for (Report newReport : newReportList) {
            projectIdSet.add(newReport.getProjectId());
            userIdSet.add(newReport.getReporterId());
            reportTypeIdSet.add(newReport.getReportTypeId());
        }

        /* TODO: verify exists projectId */
//        if (!projectService.existsAllByIdIn(projectIdSet)) {
//            errorMsg += "1 or more Project not found with Id"
//                    + "\nWhich violate constraint: FK_Report_Project\n";
//        }

        /* TODO: verify exists userId */
//        if (!userService.existsAllByIdIn(userIdSet)) {
//            errorMsg += "1 or more User not found with Id"
//                    + "\nWhich violate constraint: FK_Report_User\n";
//        }

        if (!reportTypeService.existsAllByIdIn(reportTypeIdSet)) {
            errorMsg += "1 or more ReportType not found with Id"
                    + "\nWhich violate constraint: FK_Report_ReportType\n";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        /* TODO: create EntityWrapper for report */

        return reportRepository.saveAllAndFlush(newReportList);
    }
    @Override
    public List<ReportReadDTO> createBulkReportByDTO(List<ReportCreateDTO> newReportDTOList) throws Exception {
        Set<Long> reportTypeIdSet = new HashSet<>();
        List<ReportDetailCreateDTO> newReportDetailDTOList = new ArrayList<>();
        List<TaskReportCreateDTO> newTaskReportDTOList = new ArrayList<>();

        List<Report> newReportList = newReportDTOList.stream()
                .map(newReportDTO -> {
                    /* Also while streaming */
                    reportTypeIdSet.add(newReportDTO.getReportTypeId());
                    if (newReportDTO.getReportDetailList() != null) {
                        newReportDetailDTOList.addAll(newReportDTO.getReportDetailList());
                    }
                    if (newReportDTO.getTaskReportList() != null) {
                        newTaskReportDTOList.addAll(newReportDTO.getTaskReportList());
                    }

                    return modelMapper.map(newReportDTO, Report.class);})
                .collect(Collectors.toList());

        newReportList = createBulkReport(newReportList);

        /* Get associated ReportType */
        /* No need check null, already check at createBulkReport() */
        Map<Long, ReportTypeReadDTO> reportTypeIdReportTypeDTOMap =
                reportTypeService.mapReportTypeIdReportTypeDTOByIdIn(reportTypeIdSet);

        /* Create associated ReportDetail */
        Map<Long, List<ReportDetailReadDTO>> reportIdReportDetailDTOListMap = new HashMap<>();
        if (!newReportDetailDTOList.isEmpty()) {
            List<ReportDetailReadDTO> tmpReportDetailDTOList;
            long tmpReportId;
            for (ReportDetailReadDTO newReportDetailDTO
                    : reportDetailService.createBulkReportDetailByDTOList(newReportDetailDTOList)) {
                tmpReportId = newReportDetailDTO.getReportId();
                tmpReportDetailDTOList = reportIdReportDetailDTOListMap.get(tmpReportId);

                if (tmpReportDetailDTOList == null) {
                    reportIdReportDetailDTOListMap.put(tmpReportId, Collections.singletonList(newReportDetailDTO));
                } else {
                    tmpReportDetailDTOList.add(newReportDetailDTO);
                    reportIdReportDetailDTOListMap.put(tmpReportId, tmpReportDetailDTOList);
                }
            }
        }

        /* Create associated TaskReport */
        Map<Long, List<TaskReportReadDTO>> reportIdTaskReportDTOListMap = new HashMap<>();
        if (!newReportDetailDTOList.isEmpty()) {
            List<TaskReportReadDTO> tmpTaskReportDTOList;
            long tmpReportId;
            for (TaskReportReadDTO newTaskReportDTO
                    : taskReportService.createBulkTaskReportByDTOList(newTaskReportDTOList)) {
                tmpReportId = newTaskReportDTO.getReportId();
                tmpTaskReportDTOList = reportIdTaskReportDTOListMap.get(tmpReportId);

                if (tmpTaskReportDTOList == null) {
                    reportIdTaskReportDTOListMap.put(tmpReportId, Collections.singletonList(newTaskReportDTO));
                } else {
                    tmpTaskReportDTOList.add(newTaskReportDTO);
                    reportIdTaskReportDTOListMap.put(tmpReportId, tmpTaskReportDTOList);
                }
            }
        }

        return newReportList.stream()
                .map(newReport -> {
                    ReportReadDTO newReportDTO =
                            modelMapper.map(newReport, ReportReadDTO.class);

                    newReportDTO.setReportType(reportTypeIdReportTypeDTOMap.get(newReport.getReportTypeId()));
                    newReportDTO.setReportDetailList(reportIdReportDetailDTOListMap.get(newReport.getReportId()));
                    newReportDTO.setTaskReportList(reportIdTaskReportDTOListMap.get(newReport.getReportId()));

                    return newReportDTO;})
                .collect(Collectors.toList());
    }

    /* READ */
    @Override
    public List<Report> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
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

        return reportPage.getContent();
    }
    @Override
    public List<ReportReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        List<Report> reportList = getAll(pageNo, pageSize, sortBy, sortType);

        if (reportList != null && !reportList.isEmpty()) {
            int totalPage = (int) Math.ceil((double) reportList.size() / pageSize);

            /* TODO: get all detail */

            return reportList.stream()
                    .map(report -> {
                        ReportReadDTO reportReadDTO =
                                modelMapper.map(report, ReportReadDTO.class);
                        reportReadDTO.setTotalPage(totalPage);
                        return reportReadDTO;})
                    .collect(Collectors.toList());

        } else {
            return null;
        }
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

        /* TODO: get all detail */

        return modelMapper.map(report, ReportReadDTO.class);
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

        /* TODO: get all detail */

        return reportList.stream()
                .map(report -> modelMapper.map(report, ReportReadDTO.class))
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

        /* TODO: get all detail */

        return reportList.stream()
                .map(report -> modelMapper.map(report, ReportReadDTO.class))
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

        /* TODO: get all detail */

        return reportList.stream()
                .map(report -> modelMapper.map(report, ReportReadDTO.class))
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

        /* TODO: get all detail */

        return reportList.stream()
                .map(report -> modelMapper.map(report, ReportReadDTO.class))
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

        /* TODO: get all detail */

        return reportList.stream()
                .map(report -> modelMapper.map(report, ReportReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> getAllByReportDate(SearchOption searchOption, Instant fromDate, Instant toDate) throws Exception {
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
    public List<ReportReadDTO> getAllDTOByReportDate(SearchOption searchOption, Instant fromDate, Instant toDate) throws Exception {
        List<Report> reportList = getAllByReportDate(searchOption, fromDate, toDate);

        if (reportList == null) {
            return null;
        }

        return reportList.stream()
                .map(report -> modelMapper.map(report, ReportReadDTO.class))
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

        /* TODO: verify exists projectId */
//        if (!oldReport.getProjectId().equals(updatedReport.getProjectId())) {
//            if (!projectService.existsById(updatedReport.getProjectId())) {
//                errorMsg += "No Project found with Id: " + newReport.getProjectId()
//                        + "\nWhich violate constraint: FK_Report_Project\n";
//            }
//        }

        /* TODO: verify exists userId */
//        if (!oldReport.getReporterId().equals(updatedReport.getReporterId())) {
//            if (!userService.existsById(updatedReport.getReporterId())) {
//                errorMsg += "No User found with Id: " + newReport.getReporterId()
//                        + "\nWhich violate constraint: FK_Report_Project\n";
//            }
//        }

       if (!oldReport.getReportTypeId().equals(updatedReport.getReportTypeId())) {
           if (!reportTypeService.existsById(updatedReport.getReportTypeId())) {
               errorMsg += "No ReportType found with Id: " + updatedReport.getReportTypeId()
                       + "\nWhich violate constraint: FK_Report_Project\n";
           }
       }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return reportRepository.saveAndFlush(updatedReport);
    }
    @Override
    public ReportReadDTO updateReportByDTO(ReportUpdateDTO updatedReportDTO) throws Exception {
        Report updatedReport = modelMapper.map(updatedReportDTO, Report.class);

        updatedReport = updateReport(updatedReport);

        if (updatedReport == null) {
            return null;
        }

        return modelMapper.map(updatedReport, ReportReadDTO.class);
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
