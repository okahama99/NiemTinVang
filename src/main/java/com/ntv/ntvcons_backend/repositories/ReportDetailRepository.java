package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ProjectWorker;
import com.ntv.ntvcons_backend.entities.ReportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportDetailRepository extends JpaRepository<ReportDetail, Integer> {
    List<ReportDetail> findAllByIsDeletedFalse();


    /* Id */
    Optional<ReportDetail> findByReportDetailIdAndIsDeletedIsFalse(int reportDetailId);
    List<ReportDetail> findAllByReportDetailIdInAndIsDeletedIsFalse(Collection<Integer> reportDetailIdCollection);


    /* reportId */
    List<ReportDetail> findAllByReportIdAndIsDeletedIsFalse(int reportId);
    List<ReportDetail> findAllByReportIdInAndIsDeletedIsFalse(Collection<Integer> reportIdCollection);
}
