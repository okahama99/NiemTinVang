package com.ntv.ntvcons_backend.services.reportDetail;

import com.ntv.ntvcons_backend.entities.ReportDetail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ReportDetailService {
    ReportDetail createReportDetail();

    /* READ */
    List<ReportDetail> getAll();

    List<ReportDetail> getAllByReportId(int reportID);

    ReportDetail getById(int reportId);

    /* UPDATE */
    //ReportDetail updateReport();

    /* DELETE */
    boolean deleteReport(int reportId);
}
