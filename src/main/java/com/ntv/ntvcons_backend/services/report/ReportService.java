package com.ntv.ntvcons_backend.services.report;

import com.ntv.ntvcons_backend.entities.Report;
import com.ntv.ntvcons_backend.entities.ReportModels.ReportModel;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface ReportService {
    /* CREATE */
    Report createReport(int projectId, int reporterId, Timestamp reportDate, String reportDesc);

    /* READ */
    List<Report> getAll();

    List<Report> getAllByIdIn(Collection<Integer> reportIdCollection);

    List<Report> getAllByProjectId(int projectId);

    List<Report> getAllByReporterId(int reporterId);

    List<Report> getAllByProjectIdAndReporterId(int projectId, int reporterId);

    Report getById(int reportId);

    /* UPDATE */
    boolean updateReport(ReportModel reportModel);

    /* DELETE */
    boolean deleteReport(int reportId);
}
