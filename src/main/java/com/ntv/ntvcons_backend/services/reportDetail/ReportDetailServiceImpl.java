package com.ntv.ntvcons_backend.services.reportDetail;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.ReportDetail;
import com.ntv.ntvcons_backend.entities.ReportDetailModels.ShowReportDetailModel;
import com.ntv.ntvcons_backend.repositories.PagingRepositories.ReportDetailPagingRepository;
import com.ntv.ntvcons_backend.repositories.ReportDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportDetailServiceImpl implements ReportDetailService{
    @Autowired
    ReportDetailRepository reportDetailRepository;

    @Autowired
    ReportDetailPagingRepository reportDetailPagingRepository;

    @Override
    public ReportDetail createReportDetail() {
        return null;
    }

    @Override
    public List<ShowReportDetailModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType)
        {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
        Page<ReportDetail> pagingResult = reportDetailPagingRepository.findAll(paging);
        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);
            Page<ShowReportDetailModel> modelResult = pagingResult.map(new Converter<ReportDetail, ShowReportDetailModel>() {
                @Override
                protected ShowReportDetailModel doForward(ReportDetail reportDetail) {
                    ShowReportDetailModel model = new ShowReportDetailModel();
                    //TODO
                    return model;
                }

                @Override
                protected ReportDetail doBackward(ShowReportDetailModel showReportDetailModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ShowReportDetailModel>();
        }
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
    public boolean updateReport(ShowReportDetailModel showReportDetailModel) {
        return false;
    }

    @Override
    public boolean deleteReport(int reportId) {
        return false;
    }
}
