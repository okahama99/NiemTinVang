package com.ntv.ntvcons_backend.services.reportDetail;

import com.ntv.ntvcons_backend.repositories.ReportDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportDetailServiceImpl implements ReportDetailService{
    @Autowired
    ReportDetailRepository reportDetailRepository;

    @Override
    public ReportDetail createReportDetail() {
        return null;
    }

    @Override
    public List<ReportDetail> getAll() {
        return null;
    }

    @Override
    public List<ReportDetail> getAllByReportId(int reportID) {
        return null;
    }

    @Override
    public ReportDetail getById(int reportId) {
        return null;
    }

    @Override
    public boolean deleteReport(int reportId) {
        return false;
    }
}
