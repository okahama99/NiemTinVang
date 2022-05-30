package com.ntv.ntvcons_backend.services.report;

import com.ntv.ntvcons_backend.entities.Report;
import com.ntv.ntvcons_backend.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Override
    public Report createReport(int projectId, int reporterId, Timestamp reportDate, String reportDesc) {
        return null;
    }

    @Override
    public List<Report> getAll() {
        return null;
    }

    @Override
    public List<Report> getAllByIdIn(Collection<Integer> reportIdCollection) {
        return null;
    }

    @Override
    public List<Report> getAllByProjectId(int projectId) {
        return null;
    }

    @Override
    public List<Report> getAllByReporterId(int reporterId) {
        return null;
    }

    @Override
    public List<Report> getAllByProjectIdAndReporterId(int projectId, int reporterId) {
        return null;
    }

    @Override
    public Report getById(int reportId) {
        return null;
    }

    @Override
    public Report updateReport(int reportId, int projectId, int reporterId, Timestamp reportDate, String reportDesc) {
        return null;
    }

    @Override
    public boolean deleteReport(int reportId) {
        return false;
    }
}
