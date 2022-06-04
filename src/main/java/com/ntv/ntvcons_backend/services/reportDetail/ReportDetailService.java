package com.ntv.ntvcons_backend.services.reportDetail;

import com.ntv.ntvcons_backend.entities.ReportDetail;
import com.ntv.ntvcons_backend.entities.ReportDetailModels.ShowReportDetailModel;

import java.util.List;

public interface ReportDetailService {
    ReportDetail createReportDetail();

    /* READ */
    List<ShowReportDetailModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ReportDetail> getAllByReportId(int reportID);

    ReportDetail getById(int reportId);

    /* UPDATE */
    boolean updateReport(ShowReportDetailModel showReportDetailModel);

    /* DELETE */
    boolean deleteReport(int reportId);
}
