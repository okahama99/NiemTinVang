package com.ntv.ntvcons_backend.services.report;

import com.ntv.ntvcons_backend.constants.SearchOption;
import com.ntv.ntvcons_backend.dtos.report.ReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportUpdateDTO;
import com.ntv.ntvcons_backend.entities.Report;
import com.ntv.ntvcons_backend.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ReportService extends BaseService { /* TODO: throws Exception for controller to handle */
    /* CREATE */
    Report createReport(Report newReport) throws Exception;
    ReportReadDTO createReportByDTO(ReportCreateDTO newReportDTO) throws Exception;

    /* READ */
    Page<Report> getPageAll(Pageable paging) throws Exception;
    List<ReportReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    boolean existsById(long reportId) throws Exception;
    Report getById(long reportId) throws Exception;
    ReportReadDTO getDTOById(long reportId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> reportIdCollection) throws Exception;
    List<Report> getAllByIdIn(Collection<Long> reportIdCollection) throws Exception;
    List<ReportReadDTO> getAllDTOByIdIn(Collection<Long> reportIdCollection) throws Exception;

    List<Report> getAllByProjectId(long projectId) throws Exception;
    List<ReportReadDTO> getAllDTOByProjectId(long projectId) throws Exception;
    Page<Report> getPageAllByProjectId(Pageable paging, long projectId) throws Exception;
    List<ReportReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception;

    List<Report> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    List<ReportReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    Map<Long, List<ReportReadDTO>> mapProjectIdReportDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;

    List<Report> getAllByReporterId(long reporterId) throws Exception;
    List<ReportReadDTO> getAllDTOByReporterId(long reporterId) throws Exception;
    Page<Report> getPageAllByReporterId(Pageable paging, long reporterId) throws Exception;
    List<ReportReadDTO> getAllDTOInPagingByReporterId(Pageable paging, long reporterId) throws Exception;

    List<Report> getAllByReportName(String reportName) throws Exception;
    List<ReportReadDTO> getAllDTOByReportName(String reportName) throws Exception;
    Page<Report> getPageAllByReportName(Pageable paging, String reportName) throws Exception;
    List<ReportReadDTO> getAllDTOInPagingByReportName(Pageable paging, String reportName) throws Exception;

    List<Report> getAllByReportNameContains(String reportName) throws Exception;
    List<ReportReadDTO> getAllDTOByReportNameContains(String reportName) throws Exception;
    Page<Report> getPageAllByReportNameContains(Pageable paging, String reportName) throws Exception;
    List<ReportReadDTO> getAllDTOInPagingByReportNameContains(Pageable paging, String reportName) throws Exception;

    List<Report> getAllByReportTypeId(long reportTypeId) throws Exception;
    List<ReportReadDTO> getAllDTOByReportTypeId(long reportTypeId) throws Exception;
    Page<Report> getPageAllByReportTypeId(Pageable paging, long reportTypeId) throws Exception;
    List<ReportReadDTO> getAllDTOInPagingByReportTypeId(Pageable paging, long reportTypeId) throws Exception;

    List<Report> getAllByProjectIdAndReporterId(int projectId, int reporterId) throws Exception;
    List<ReportReadDTO> getAllDTOByProjectIdAndReporterId(int projectId, int reporterId) throws Exception;

    List<Report> getAllByReportDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<ReportReadDTO> getAllDTOByReportDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    /* UPDATE */
    Report updateReport(Report updatedReport) throws Exception;
    ReportReadDTO updateReportByDTO(ReportUpdateDTO updatedReportDTO) throws Exception;

    /* DELETE */
    boolean deleteReport(long reportId) throws Exception;

    /** Cascade when delete Project */
    boolean deleteAllByProjectId(long projectId) throws Exception;
}
