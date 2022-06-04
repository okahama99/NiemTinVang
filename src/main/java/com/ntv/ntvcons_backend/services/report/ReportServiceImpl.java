package com.ntv.ntvcons_backend.services.report;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Report;
import com.ntv.ntvcons_backend.entities.ReportModels.ShowReportModel;
import com.ntv.ntvcons_backend.repositories.PagingRepositories.ReportPagingRepository;
import com.ntv.ntvcons_backend.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportPagingRepository reportPagingRepository;

    @Override
    public Report createReport(int projectId, int reporterId, Timestamp reportDate, String reportDesc) {
        return null;
    }

    @Override
    public List<ShowReportModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType)
        {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
        Page<Report> pagingResult = reportPagingRepository.findAll(paging);
        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);
            Page<ShowReportModel> modelResult = pagingResult.map(new Converter<Report, ShowReportModel>() {
                @Override
                protected ShowReportModel doForward(Report report) {
                    ShowReportModel model = new ShowReportModel();
                    //TODO
                    return model;
                }

                @Override
                protected Report doBackward(ShowReportModel showReportModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ShowReportModel>();
        }
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
    public boolean updateReport(ShowReportModel showReportModel) {
        return true;
    }

    @Override
    public boolean deleteReport(int reportId) {
        return false;
    }
}
