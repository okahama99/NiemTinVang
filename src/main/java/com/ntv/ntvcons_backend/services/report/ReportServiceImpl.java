package com.ntv.ntvcons_backend.services.report;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.SearchOption;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
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
import com.ntv.ntvcons_backend.services.entityWrapper.EntityWrapperService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.misc.FileCombineService;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import com.ntv.ntvcons_backend.services.reportDetail.ReportDetailService;
import com.ntv.ntvcons_backend.services.reportType.ReportTypeService;
import com.ntv.ntvcons_backend.services.taskReport.TaskReportService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private EntityWrapperService entityWrapperService;
    @Autowired
    private FileCombineService fileCombineService;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;

    private final EntityType ENTITY_TYPE = EntityType.REPORT_ENTITY;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public Report createReport(Report newReport) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!projectService.existsById(newReport.getProjectId())) {
            errorMsg += "No Project found with Id: '" + newReport.getProjectId()
                    + "'. Which violate constraint: FK_Report_Project. ";
        }
        if (!reportTypeService.existsById(newReport.getReportTypeId())) {
            errorMsg += "No ReportType found with Id: '" + newReport.getReportTypeId()
                    + "'. Which violate constraint: FK_Report_ReportType. ";
        }
        if (!userService.existsById(newReport.getReporterId())) {
            errorMsg += "No User (Reporter) found with Id: '" + newReport.getReporterId()
                    + "'. Which violate constraint: FK_Report_User_ReporterId. ";
        }
        if (!userService.existsById(newReport.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newReport.getCreatedBy()
                    + "'. Which violate constraint: FK_Report_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (reportRepository
                .existsByProjectIdAndReportNameAndStatusNotIn(
                        newReport.getProjectId(),
                        newReport.getReportName(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another Report with name: '" + newReport.getReportName()
                    + "' for Project with Id:' " +  newReport.getProjectId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return reportRepository.saveAndFlush(newReport);
    }
    @Override
    public ReportReadDTO createReportByDTO(ReportCreateDTO newReportDTO) throws Exception {
        modelMapper.typeMap(ReportCreateDTO.class, Report.class)
                .addMappings(mapper -> {
                    mapper.skip(Report::setReportDate);});

        Report newReport = modelMapper.map(newReportDTO, Report.class);

        /* Already check NOT NULL */
        newReport.setReportDate(
                LocalDateTime.parse(newReportDTO.getReportDate(), dateTimeFormatter));

//        if (newReport.getReportDate().isAfter(LocalDateTime.now())) {
//            throw new IllegalArgumentException("reportDate can't be in the future");
//        }

        newReport = createReport(newReport);

        long newReportId = newReport.getReportId();

        /* Create associated EntityWrapper */
        entityWrapperService
                .createEntityWrapper(newReportId, ENTITY_TYPE, newReport.getCreatedBy());

        /* Create associated ReportDetail; Set required FK reportId */
        List<ReportDetailCreateDTO> newReportDetailDTOList = newReportDTO.getReportDetailList();
        if (newReportDetailDTOList != null) {
            newReportDetailDTOList = newReportDetailDTOList.stream()
                    .peek(newReportDetailDTO -> newReportDetailDTO.setReportId(newReportId))
                    .collect(Collectors.toList());

            reportDetailService.createBulkReportDetailByDTOList(newReportDetailDTOList);
        }

        /* Create associated TaskReport; Set required FK reportId */
        List<TaskReportCreateDTO> newTaskReportDTOList = newReportDTO.getTaskReportList();
        if (newTaskReportDTOList != null) {
            newTaskReportDTOList = newTaskReportDTOList.stream()
                    .peek(newReportDetailDTO -> newReportDetailDTO.setReportId(newReportId))
                    .collect(Collectors.toList());

            taskReportService.createBulkTaskReportByDTOList(newTaskReportDTOList);
        }

        return fillDTO(newReport);
    }

    /* READ */
    @Override
    public Page<Report> getPageAll(Pageable paging) throws Exception {
        Page<Report> reportPage =
                reportRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (reportPage.isEmpty()) 
            return null;

        return reportPage;
    }
    @Override
    public List<ReportReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<Report> reportPage = getPageAll(paging);

        if (reportPage == null) 
            return null;

        List<Report> reportList = reportPage.getContent();

        if (reportList.isEmpty()) 
            return null;

        return fillAllDTO(reportList, reportPage.getTotalPages());
    }

    @Override
    public boolean existsById(long reportId) throws Exception {
        return reportRepository
                .existsByReportIdAndStatusNotIn(reportId, N_D_S_STATUS_LIST);
    }
    @Override
    public Report getById(long reportId) throws Exception {
        return reportRepository
                .findByReportIdAndStatusNotIn(reportId, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public ReportReadDTO getDTOById(long reportId) throws Exception {
        Report report = getById(reportId);

        if (report == null) 
            return null;

        return fillDTO(report);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> reportIdCollection) throws Exception {
        return reportRepository
                .existsAllByReportIdInAndStatusNotIn(reportIdCollection, N_D_S_STATUS_LIST);
    }
    @Override
    public List<Report> getAllByIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByReportIdInAndStatusNotIn(reportIdCollection, N_D_S_STATUS_LIST);

        if (reportList.isEmpty()) 
            return null;

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<Report> reportList = getAllByIdIn(reportIdCollection);

        if (reportList == null) 
            return null;

        return fillAllDTO(reportList, null);
    }

    @Override
    public List<Report> getAllByProjectId(long projectId) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByProjectIdAndStatusNotIn(projectId, N_D_S_STATUS_LIST);

        if (reportList.isEmpty()) 
            return null;

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByProjectId(long projectId) throws Exception {
        List<Report> reportList = getAllByProjectId(projectId);

        if (reportList == null) 
            return null;

        return fillAllDTO(reportList, null);
    }
    @Override
    public Page<Report> getPageAllByProjectId(Pageable paging, long projectId) throws Exception {
        Page<Report> reportPage =
                reportRepository.findAllByProjectIdAndStatusNotIn(projectId, N_D_S_STATUS_LIST, paging);

        if (reportPage.isEmpty()) 
            return null;

        return reportPage;
    }
    @Override
    public List<ReportReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception {
        Page<Report> reportPage = getPageAllByProjectId(paging, projectId);

        if (reportPage == null) 
            return null;

        List<Report> reportList = reportPage.getContent();

        if (reportList.isEmpty()) 
            return null;

        return fillAllDTO(reportList, reportPage.getTotalPages());
    }

    @Override
    public List<Report> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByProjectIdInAndStatusNotIn(projectIdCollection, N_D_S_STATUS_LIST);

        if (reportList.isEmpty()) 
            return null;

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Report> reportList = getAllByProjectIdIn(projectIdCollection);

        if (reportList == null) 
            return null;

        return fillAllDTO(reportList, null);
    }
    @Override
    public Map<Long, List<ReportReadDTO>> mapProjectIdReportDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<ReportReadDTO> reportDTOList = getAllDTOByProjectIdIn(projectIdCollection);

        if (reportDTOList == null) 
            return new HashMap<>();

        Map<Long, List<ReportReadDTO>> projectIdReportDTOListMap = new HashMap<>();

        long tmpProjectId;
        List<ReportReadDTO> tmpReportDTOList;

        for (ReportReadDTO reportDTO : reportDTOList) {
            tmpProjectId = reportDTO.getProjectId();
            tmpReportDTOList = projectIdReportDTOListMap.get(tmpProjectId);

            if (tmpReportDTOList == null) {
                projectIdReportDTOListMap.put(tmpProjectId, new ArrayList<>(Collections.singletonList(reportDTO)));
            } else {
                tmpReportDTOList.add(reportDTO);

                projectIdReportDTOListMap.put(tmpProjectId, tmpReportDTOList);
            }
        }

        return projectIdReportDTOListMap;
    }

    @Override
    public List<Report> getAllByReporterId(long reporterId) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByReporterIdAndStatusNotIn(reporterId, N_D_S_STATUS_LIST);

        if (reportList.isEmpty()) 
            return null;

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByReporterId(long reporterId) throws Exception {
        List<Report> reportList = getAllByReporterId(reporterId);

        if (reportList == null) 
            return null;

        return fillAllDTO(reportList, null);
    }
    @Override
    public Page<Report> getPageAllByReporterId(Pageable paging, long reporterId) throws Exception {
        Page<Report> reportPage =
                reportRepository.findAllByReporterIdAndStatusNotIn(reporterId, N_D_S_STATUS_LIST, paging);

        if (reportPage.isEmpty()) 
            return null;

        return reportPage;
    }
    @Override
    public List<ReportReadDTO> getAllDTOInPagingByReporterId(Pageable paging, long reporterId) throws Exception {
        Page<Report> reportPage = getPageAllByReporterId(paging, reporterId);

        if (reportPage == null) 
            return null;

        List<Report> reportList = reportPage.getContent();

        if (reportList.isEmpty()) 
            return null;

        return fillAllDTO(reportList, reportPage.getTotalPages());
    }

    @Override
    public List<Report> getAllByReportName(String reportName) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByReportNameAndStatusNotIn(reportName, N_D_S_STATUS_LIST);

        if (reportList.isEmpty())
            return null;

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByReportName(String reportName) throws Exception {
        List<Report> reportList = getAllByReportName(reportName);

        if (reportList == null)
            return null;

        return fillAllDTO(reportList, null);
    }
    @Override
    public Page<Report> getPageAllByReportName(Pageable paging, String reportName) throws Exception {
        Page<Report> reportPage =
                reportRepository.findAllByReportNameAndStatusNotIn(reportName, N_D_S_STATUS_LIST, paging);

        if (reportPage.isEmpty())
            return null;

        return reportPage;
    }
    @Override
    public List<ReportReadDTO> getAllDTOInPagingByReportName(Pageable paging, String reportName) throws Exception {
        Page<Report> reportPage = getPageAllByReportName(paging, reportName);

        if (reportPage == null)
            return null;

        List<Report> reportList = reportPage.getContent();

        if (reportList.isEmpty())
            return null;

        return fillAllDTO(reportList, reportPage.getTotalPages());
    }

    @Override
    public List<Report> getAllByReportNameContains(String reportName) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByReportNameContainsAndStatusNotIn(reportName, N_D_S_STATUS_LIST);

        if (reportList.isEmpty()) 
            return null;

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByReportNameContains(String reportName) throws Exception {
        List<Report> reportList = getAllByReportNameContains(reportName);

        if (reportList == null) 
            return null;

        return fillAllDTO(reportList, null);
    }
    @Override
    public Page<Report> getPageAllByReportNameContains(Pageable paging, String reportName) throws Exception {
        Page<Report> reportPage =
                reportRepository.findAllByReportNameContainsAndStatusNotIn(reportName, N_D_S_STATUS_LIST, paging);

        if (reportPage.isEmpty()) 
            return null;

        return reportPage;
    }
    @Override
    public List<ReportReadDTO> getAllDTOInPagingByReportNameContains(Pageable paging, String reportName) throws Exception {
        Page<Report> reportPage = getPageAllByReportNameContains(paging, reportName);

        if (reportPage == null) 
            return null;

        List<Report> reportList = reportPage.getContent();

        if (reportList.isEmpty()) 
            return null;

        return fillAllDTO(reportList, reportPage.getTotalPages());
    }

    @Override
    public List<Report> getAllByReportTypeId(long reportTypeId) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByReportTypeIdAndStatusNotIn(reportTypeId, N_D_S_STATUS_LIST);

        if (reportList.isEmpty()) 
            return null;

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByReportTypeId(long reportTypeId) throws Exception {
        List<Report> reportList = getAllByReportTypeId(reportTypeId);

        if (reportList == null) 
            return null;

        return fillAllDTO(reportList, null);
    }
    @Override
    public Page<Report> getPageAllByReportTypeId(Pageable paging, long reportTypeId) throws Exception {
        Page<Report> reportPage =
                reportRepository.findAllByReportTypeIdAndStatusNotIn(reportTypeId, N_D_S_STATUS_LIST, paging);

        if (reportPage.isEmpty()) 
            return null;

        return reportPage;
    }
    @Override
    public List<ReportReadDTO> getAllDTOInPagingByReportTypeId(Pageable paging, long reportTypeId) throws Exception {
        Page<Report> reportPage = getPageAllByReportTypeId(paging, reportTypeId);

        if (reportPage == null) 
            return null;

        List<Report> reportList = reportPage.getContent();

        if (reportList.isEmpty()) 
            return null;

        return fillAllDTO(reportList, reportPage.getTotalPages());
    }

    @Override
    public List<Report> getAllByProjectIdAndReporterId(int projectId, int reporterId) throws Exception {
        List<Report> reportList =
                reportRepository.findAllByProjectIdAndReporterIdAndStatusNotIn(projectId, reporterId, N_D_S_STATUS_LIST);

        if (reportList.isEmpty()) 
            return null;

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByProjectIdAndReporterId(int projectId, int reporterId) throws Exception {
        List<Report> reportList = getAllByProjectIdAndReporterId(projectId, reporterId);

        if (reportList == null) 
            return null;

        return fillAllDTO(reportList, null);
    }

    @Override
    public List<Report> getAllByReportDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Report> reportList = new ArrayList<>();

        switch (searchOption) {
            case BEFORE_DATE:
                reportList = reportRepository.findAllByReportDateBeforeAndStatusNotIn(toDate, N_D_S_STATUS_LIST);
                break;

            case AFTER_DATE:
                reportList = reportRepository.findAllByReportDateAfterAndStatusNotIn(fromDate, N_D_S_STATUS_LIST);
                break;

            case BETWEEN_DATE:
                reportList = reportRepository.findAllByReportDateBetweenAndStatusNotIn(fromDate, toDate, N_D_S_STATUS_LIST);
                break;

            default:
                /* All invalid searchOption should've been caught at controller,
                   if reach here then: not normal / slip up */
                throw new IllegalArgumentException("Invalid SearchOption used for entity Report");
        }

        if (reportList.isEmpty()) 
            return null;

        return reportList;
    }
    @Override
    public List<ReportReadDTO> getAllDTOByReportDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Report> reportList = getAllByReportDate(searchOption, fromDate, toDate);

        if (reportList == null) 
            return null;

        return fillAllDTO(reportList, null);
    }

    /* UPDATE */
    @Override
    public Report updateReport(Report updatedReport) throws Exception {
        Report oldReport = getById(updatedReport.getReportId());

        if (oldReport == null) 
            return null; /* Not found by Id, return null */

        String errorMsg = "";

        /* Check FK */
        if (!oldReport.getProjectId().equals(updatedReport.getProjectId())) {
            if (!projectService.existsById(updatedReport.getProjectId())) {
                errorMsg += "No Project found with Id: '" + updatedReport.getProjectId()
                        + "'. Which violate constraint: FK_Report_Project. ";
            }
        }
        if (!oldReport.getReportTypeId().equals(updatedReport.getReportTypeId())) {
            if (!reportTypeService.existsById(updatedReport.getReportTypeId())) {
                errorMsg += "No ReportType found with Id: '" + updatedReport.getReportTypeId()
                        + "'. Which violate constraint: FK_Report_ReportType. ";
            }
        }
        if (!oldReport.getReporterId().equals(updatedReport.getReporterId())) {
            if (!userService.existsById(updatedReport.getReporterId())) {
                errorMsg += "No User (Reporter) found with Id: '" + updatedReport.getReporterId()
                        + "'. Which violate constraint: FK_Report_User_ReporterId. ";
            }
        }
        if (oldReport.getUpdatedBy() != null) {
            if (!oldReport.getUpdatedBy().equals(updatedReport.getUpdatedBy())) {
                if (!userService.existsById(updatedReport.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedReport.getUpdatedBy()
                            + "'. Which violate constraint: FK_Report_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedReport.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedReport.getUpdatedBy()
                        + "'. Which violate constraint: FK_Report_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (reportRepository
                .existsByProjectIdAndReportNameAndReportIdIsNotAndStatusNotIn(
                        updatedReport.getProjectId(),
                        updatedReport.getReportName(),
                        updatedReport.getReportId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another Report with name: '" + updatedReport.getReportName()
                    + "' for Project with Id:' " +  updatedReport.getProjectId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedReport.setCreatedAt(oldReport.getCreatedAt());
        updatedReport.setCreatedBy(oldReport.getCreatedBy());

        return reportRepository.saveAndFlush(updatedReport);
    }
    @Override
    public ReportReadDTO updateReportByDTO(ReportUpdateDTO updatedReportDTO) throws Exception {
        modelMapper.typeMap(ReportUpdateDTO.class, Report.class)
                .addMappings(mapper -> {
                    mapper.skip(Report::setReportDate);});

        Report updatedReport = modelMapper.map(updatedReportDTO, Report.class);

        /* Already check NOT NULL */
        updatedReport.setReportDate(
                LocalDateTime.parse(updatedReportDTO.getReportDate(), dateTimeFormatter));

//        if (updatedReport.getReportDate().isAfter(LocalDateTime.now())) {
//            throw new IllegalArgumentException("reportDate can't be in the future");
//        }

        updatedReport = updateReport(updatedReport);

        if (updatedReport == null) 
            return null;
        long updatedReportId = updatedReport.getReportId();

        /* (If change) Update/Create associated ReportDetail; Set required FK reportId */
        List<ReportDetailUpdateDTO> reportDetailDTOList = updatedReportDTO.getReportDetailList();
        if (reportDetailDTOList != null) {
            reportDetailDTOList =
                    reportDetailDTOList.stream()
                            .peek(reportDetailDTO -> reportDetailDTO.setReportId(updatedReportId))
                            .collect(Collectors.toList());

            /* TODO: to use later when login done
            modelMapper.typeMap(ReportDetailUpdateDTO.class, ReportDetailCreateDTO.class)
                    .addMappings(mapper -> {
                        mapper.map(ReportDetailUpdateDTO::getUpdatedBy, ReportDetailCreateDTO::setCreatedBy);});*/

            List<ReportDetailCreateDTO> newReportDetailDTOList = new ArrayList<>();
            List<ReportDetailUpdateDTO> updatedReportDetailDTOList = new ArrayList<>();

            for (ReportDetailUpdateDTO updatedReportDetailDTO : reportDetailDTOList) {
                if (updatedReportDetailDTO.getReportDetailId() <= 0) {
                    newReportDetailDTOList.add(
                            modelMapper.map(updatedReportDetailDTO, ReportDetailCreateDTO.class));
                } else {
                    updatedReportDetailDTOList.add(updatedReportDetailDTO);
                }
            }

            /* Create associated ReportDetail */
            if (!newReportDetailDTOList.isEmpty()) {
                reportDetailService.createBulkReportDetailByDTOList(newReportDetailDTOList);
            }

            /* Update associated ReportDetail */
            if (!updatedReportDetailDTOList.isEmpty()) {
                reportDetailService.updateBulkReportDetailByDTOList(updatedReportDetailDTOList);
            }
        }

        /* (If change) Update/Create associated TaskReport; Set required FK reportId */
        List<TaskReportUpdateDTO> taskReportDTOList = updatedReportDTO.getTaskReportList();
        if (taskReportDTOList != null) {
            taskReportDTOList =
                    taskReportDTOList.stream()
                            .peek(taskReportDTO -> taskReportDTO.setReportId(updatedReportId))
                            .collect(Collectors.toList());

            /* TODO: to use later when login done
            modelMapper.typeMap(TaskReportUpdateDTO.class, TaskReportCreateDTO.class)
                    .addMappings(mapper -> {
                        mapper.map(TaskReportUpdateDTO::getUpdatedBy, TaskReportCreateDTO::setCreatedBy);});*/

            List<TaskReportCreateDTO> newTaskReportDTOList = new ArrayList<>();
            List<TaskReportUpdateDTO> updatedTaskReportDTOList = new ArrayList<>();

            for (TaskReportUpdateDTO updatedTaskReportDTO : taskReportDTOList) {
                if (updatedTaskReportDTO.getTaskReportId() <= 0) {
                    newTaskReportDTOList.add(modelMapper.map(updatedTaskReportDTO, TaskReportCreateDTO.class));
                } else {
                    updatedTaskReportDTOList.add(updatedTaskReportDTO);
                }
            }

            /* Create associated TaskReport; Set required FK reportId (Just in case) */
            if (!newTaskReportDTOList.isEmpty()) {
                taskReportService.createBulkTaskReportByDTOList(newTaskReportDTOList);
            }

            /* Update associated TaskReport; Set required FK reportId (Just in case) */
            if (!updatedTaskReportDTOList.isEmpty()) {
                taskReportService.updateBulkTaskReportByDTOList(updatedTaskReportDTOList);
            }
        }

        return fillDTO(updatedReport);
    }

    /* DELETE */
    @Override
    public boolean deleteReport(long reportId) throws Exception {
        Report report = getById(reportId);
        
        if (report == null)
            return false; /* Not found with Id */

        /* Delete all associate detail */
        reportDetailService.deleteAllByReportId(reportId);

        /* Delete all associated File (In DB And Firebase) */
        List<ExternalFileReadDTO> fileDTOList =
                eFEWPairingService
                        .getAllExternalFileDTOByEntityIdAndEntityType(reportId, ENTITY_TYPE);

        if (fileDTOList != null && !fileDTOList.isEmpty()) {
            fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
        }

        /* Delete associated EntityWrapper => All EFEWPairing */
        entityWrapperService.deleteByEntityIdAndEntityType(reportId, ENTITY_TYPE);

        report.setStatus(Status.DELETED);
        reportRepository.saveAndFlush(report);
        
        return true;
    }

    @Override
    public boolean deleteAllByProjectId(long projectId) throws Exception {
        List<Report> reportList = getAllByProjectId(projectId);

        if (reportList == null)
            return false; /* Not found with projectId */

        /* Set to avoid duplicate */
        Set<Long> reportIdSet = new HashSet<>();

        reportList = reportList.stream()
                .peek(report -> {
                    reportIdSet.add(report.getReportId());

                    report.setStatus(Status.DELETED);})
                .collect(Collectors.toList());

        /* Delete all associate detail */
        reportDetailService.deleteAllByReportIdIn(reportIdSet);

        /* Delete associated File (In DB And Firebase) */
        Map<Long, List<ExternalFileReadDTO>> reportIdFileListDTOMap =
                eFEWPairingService
                        .mapEntityIdExternalFileDTOListByEntityIdInAndEntityType(reportIdSet, ENTITY_TYPE);

        if (reportIdFileListDTOMap != null && !reportIdFileListDTOMap.isEmpty()) {
            List<ExternalFileReadDTO> fileDTOList = new ArrayList<>();

            List<ExternalFileReadDTO> tmpFileDTOList;
            for (Long blueprintId : reportIdFileListDTOMap.keySet()) {
                tmpFileDTOList = reportIdFileListDTOMap.get(blueprintId);
                if (tmpFileDTOList != null) {
                    fileDTOList.addAll(tmpFileDTOList);
                }
            }

            if (!fileDTOList.isEmpty()) {
                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
            }
        }

        /* Delete associated EntityWrapper => All EFEWPairing */
        entityWrapperService.deleteAllByEntityIdInAndEntityType(reportIdSet, ENTITY_TYPE);

        reportRepository.saveAllAndFlush(reportList);

        return true;
    }

    /* Utils */
    private ReportReadDTO fillDTO(Report report) throws Exception {
        long reportId = report.getReportId();

        ReportReadDTO reportDTO = modelMapper.map(report, ReportReadDTO.class);

        /* NOT NULL */
        /* Get associated ReportType */
        reportDTO.setReportType(reportTypeService.getDTOById(report.getReportTypeId()));

        /* Nullable */
        /* Get associated ReportDetail */
        reportDTO.setReportDetailList(reportDetailService.getAllDTOByReportId(reportId));
        /* Get associated TaskReport */
        reportDTO.setTaskReportList(taskReportService.getAllDTOByReportId(reportId));
        /* Get associated ExternalFile */
        reportDTO.setFileList(
                eFEWPairingService
                        .getAllExternalFileDTOByEntityIdAndEntityType(reportId, ENTITY_TYPE));

        return reportDTO;
    }

    private List<ReportReadDTO> fillAllDTO(Collection<Report> reportCollection, Integer totalPage) throws Exception {
        Set<Long> reportIdSet = new HashSet<>();
        Set<Long> reportTypeIdSet = new HashSet<>();

        for (Report report : reportCollection) {
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
        /* Get associated ExternalFile */
        Map<Long, List<ExternalFileReadDTO>> reportIdExternalFileDTOListMap =
                eFEWPairingService
                        .mapEntityIdExternalFileDTOListByEntityIdInAndEntityType(reportIdSet, ENTITY_TYPE);

        return reportCollection.stream()
                .map(report -> {
                    ReportReadDTO reportDTO =
                            modelMapper.map(report, ReportReadDTO.class);

                    long tmpReportId = report.getReportId();

                    /* NOT NULL */
                    reportDTO.setReportType(reportTypeIdReportTypeDTOMap.get(report.getReportTypeId()));

                    /* Nullable */
                    reportDTO.setReportDetailList(reportIdReportDetailDTOListMap.get(tmpReportId));
                    reportDTO.setTaskReportList(reportIdTaskReportDTOListMap.get(tmpReportId));
                    reportDTO.setFileList(
                            reportIdExternalFileDTOListMap.get(tmpReportId));

                    reportDTO.setTotalPage(totalPage);

                    return reportDTO;})
                .collect(Collectors.toList());
    }
}
