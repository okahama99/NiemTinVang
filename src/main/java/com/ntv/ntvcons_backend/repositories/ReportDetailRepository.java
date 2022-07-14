package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ReportDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportDetailRepository extends JpaRepository<ReportDetail, Long> {
    Page<ReportDetail> findAllByIsDeletedIsFalse(Pageable paging);


    /* Id */
    Optional<ReportDetail> findByReportDetailIdAndIsDeletedIsFalse(long reportDetailId);
    List<ReportDetail> findAllByReportDetailIdInAndIsDeletedIsFalse(Collection<Long> reportDetailIdCollection);
    /* reportId & itemDesc & itemPrice */
    boolean existsByReportIdAndItemDescAndItemPriceAndReportDetailIdIsNotAndIsDeletedIsFalse
            (long reportId, String itemDesc, double itemPrice, long reportDetailId);


    /* reportId */
    List<ReportDetail> findAllByReportIdAndIsDeletedIsFalse(long reportId);
    Page<ReportDetail> findAllByReportIdAndIsDeletedIsFalse(long reportId, Pageable paging);
    List<ReportDetail> findAllByReportIdInAndIsDeletedIsFalse(Collection<Long> reportIdCollection);
    Page<ReportDetail> findAllByReportIdInAndIsDeletedIsFalse(Collection<Long> reportIdCollection, Pageable paging);
    /* reportId & itemDesc & itemPrice */
    boolean existsByReportIdAndItemDescAndItemPriceAndIsDeletedIsFalse(long reportId, String itemDesc, double itemPrice);
}
