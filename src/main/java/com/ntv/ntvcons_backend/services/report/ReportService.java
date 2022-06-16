package com.ntv.ntvcons_backend.services.report;

import com.ntv.ntvcons_backend.constants.SearchOption;
import com.ntv.ntvcons_backend.dtos.report.ReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportUpdateDTO;
import com.ntv.ntvcons_backend.entities.Report;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface ReportService { /* TODO: throws Exception for controller to handle */
    /* CREATE */
    Report createReport(Report newReport) throws Exception;
    ReportReadDTO createReportByDTO(ReportCreateDTO newReportDTO) throws Exception;

    List<Report> createBulkReport(List<Report> newReportList) throws Exception;
    List<ReportReadDTO> createBulkReportByDTO(List<ReportCreateDTO> newReportDTOList) throws Exception;

    /* READ */
    List<Report> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<ReportReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    boolean existsById(long reportId) throws Exception;
    Report getById(long reportId) throws Exception;
    ReportReadDTO getDTOById(long reportId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> reportIdCollection) throws Exception;
    List<Report> getAllByIdIn(Collection<Long> reportIdCollection) throws Exception;
    List<ReportReadDTO> getAllDTOByIdIn(Collection<Long> reportIdCollection) throws Exception;

    List<Report> getAllByProjectId(long projectId) throws Exception;
    List<ReportReadDTO> getAllDTOByProjectId(long projectId) throws Exception;

    List<Report> getAllByReporterId(long reporterId) throws Exception;
    List<ReportReadDTO> getAllDTOByReporterId(long reporterId) throws Exception;

    List<Report> getAllByReportTypeId(long reportTypeId) throws Exception;
    List<ReportReadDTO> getAllDTOByReportTypeId(long reportTypeId) throws Exception;

    List<Report> getAllByProjectIdAndReporterId(int projectId, int reporterId) throws Exception;
    List<ReportReadDTO> getAllDTOByProjectIdAndReporterId(int projectId, int reporterId) throws Exception;

    List<Report> getAllByReportDate(SearchOption searchOption, Instant fromDate, Instant toDate) throws Exception;
    List<ReportReadDTO> getAllDTOByReportDate(SearchOption searchOption, Instant fromDate, Instant toDate) throws Exception;

    /* UPDATE */
    Report updateReport(Report updatedReport) throws Exception;
    ReportReadDTO updateReportByDTO(ReportUpdateDTO updatedReportDTO) throws Exception;

    /* DELETE */
    boolean deleteReport(long reportId) throws Exception;

    /** Cascade when delete Project */
    boolean deleteAllByProjectId(long projectId) throws Exception;
}
