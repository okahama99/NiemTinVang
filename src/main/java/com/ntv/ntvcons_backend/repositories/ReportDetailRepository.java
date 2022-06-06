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
    Page<ReportDetail> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<ReportDetail> findByReportDetailIdAndIsDeletedIsFalse(long reportDetailId);
    List<ReportDetail> findAllByReportDetailIdInAndIsDeletedIsFalse(Collection<Long> reportDetailIdCollection);


    /* reportId */
    List<ReportDetail> findAllByReportIdAndIsDeletedIsFalse(long reportId);
    List<ReportDetail> findAllByReportIdInAndIsDeletedIsFalse(Collection<Long> reportIdCollection);
}
