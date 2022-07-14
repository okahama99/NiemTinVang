package com.ntv.ntvcons_backend.services.reportDetail;

import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.entities.ReportDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ReportDetailService { /* TODO: throws Exception for controller to handle */
    ReportDetail createReportDetail(ReportDetail newReportDetail) throws Exception;
    ReportDetailReadDTO createReportDetailByDTO(ReportDetailCreateDTO newReportDetailDTO) throws Exception;

    List<ReportDetail> createBulkReportDetail(Collection<ReportDetail> newReportDetailList) throws Exception;
    List<ReportDetailReadDTO> createBulkReportDetailByDTOList(Collection<ReportDetailCreateDTO> newReportDetailDTOList) throws Exception;

    /* READ */
    Page<ReportDetail> getPageAll(Pageable paging) throws Exception;
    List<ReportDetailReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    ReportDetail getById(long reportDetailId) throws Exception;
    ReportDetailReadDTO getDTOById(long reportDetailId) throws Exception;

    List<ReportDetail> getAllByIdIn(Collection<Long> reportDetailIdCollection) throws Exception;
    List<ReportDetailReadDTO> getAllDTOByIdIn(Collection<Long> reportDetailIdCollection) throws Exception;

    List<ReportDetail> getAllByReportId(long reportId) throws Exception;
    List<ReportDetailReadDTO> getAllDTOByReportId(long reportId) throws Exception;
    Page<ReportDetail> getPageAllByReportId(Pageable paging, long reportId) throws Exception;
    List<ReportDetailReadDTO> getAllDTOInPagingByReportId(Pageable paging,long reportId) throws Exception;

    List<ReportDetail> getAllByReportIdIn(Collection<Long> reportIdCollection) throws Exception;
    List<ReportDetailReadDTO> getAllDTOByReportIdIn(Collection<Long> reportIdCollection) throws Exception;
    Map<Long, List<ReportDetailReadDTO>> mapReportIdReportDetailDTOListByReportIdIn(Collection<Long> reportIdCollection) throws Exception;
    Page<ReportDetail> getPageAllByReportIdIn(Pageable paging, Collection<Long> reportIdCollection) throws Exception;
    List<ReportDetailReadDTO> getAllDTOInPagingByReportIdIn(Pageable paging, Collection<Long> reportIdCollection) throws Exception;

    /* UPDATE */
    ReportDetail updateReportDetail(ReportDetail updatedReportDetail) throws Exception;
    ReportDetailReadDTO updateReportDetailByDTO(ReportDetailUpdateDTO updatedReportDetailDTO) throws Exception;

    List<ReportDetail> updateBulkReportDetail(Collection<ReportDetail> updatedReportDetailList) throws Exception;
    List<ReportDetailReadDTO> updateBulkReportDetailByDTOList(Collection<ReportDetailUpdateDTO> updatedReportDetailDTOList) throws Exception;

    /* DELETE */
    boolean deleteReportDetail(long reportDetailId) throws Exception;

    /** Cascade when delete Report */
    boolean deleteAllByReportId(long reportId) throws Exception;

    /** Cascade when delete bulk Report */
    boolean deleteAllByReportIdIn(Collection<Long> reportIdCollection) throws Exception;
}
