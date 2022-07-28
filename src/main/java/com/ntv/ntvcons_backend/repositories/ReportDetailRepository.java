package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
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
    Page<ReportDetail> findAllByStatusNotIn
            (Collection<Status> statusCollection, Pageable paging);


    /* Id */
    Optional<ReportDetail> findByReportDetailIdAndStatusNotIn
            (long reportDetailId, Collection<Status> statusCollection);
    List<ReportDetail> findAllByReportDetailIdInAndStatusNotIn
            (Collection<Long> reportDetailIdCollection, Collection<Status> statusCollection);
    /* reportId & itemDesc & itemPrice */
    /** Check duplicate for Update */
    boolean existsByReportIdAndItemDescAndItemPriceAndReportDetailIdIsNotAndStatusNotIn
            (long reportId, String itemDesc, double itemPrice, long reportDetailId, Collection<Status> statusCollection);


    /* reportId */
    List<ReportDetail> findAllByReportIdAndStatusNotIn
            (long reportId, Collection<Status> statusCollection);
    Page<ReportDetail> findAllByReportIdAndStatusNotIn
            (long reportId, Collection<Status> statusCollection, Pageable paging);
    List<ReportDetail> findAllByReportIdInAndStatusNotIn
            (Collection<Long> reportIdCollection, Collection<Status> statusCollection);
    Page<ReportDetail> findAllByReportIdInAndStatusNotIn
            (Collection<Long> reportIdCollection, Collection<Status> statusCollection, Pageable paging);
    /* reportId & itemDesc & itemPrice */
    /** Check duplicate for Create */
    boolean existsByReportIdAndItemDescAndItemPriceAndStatusNotIn
            (long reportId, String itemDesc, double itemPrice, Collection<Status> statusCollection);
}
