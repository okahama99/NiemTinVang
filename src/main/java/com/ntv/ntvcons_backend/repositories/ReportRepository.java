package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<Report> findByReportIdAndIsDeletedIsFalse(long reportId);
    List<Report> findAllByReportIdInAndIsDeletedIsFalse(Collection<Long> reportIdCollection);


    /* projectId */
    List<Report> findAllByProjectIdAndIsDeletedIsFalse(long projectId);
    List<Report> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection);


    /* reportTypeId */
    List<Report> findAllByReportTypeIdAndIsDeletedIsFalse(long reportTypeId);
    List<Report> findAllByReportTypeIdInAndIsDeletedIsFalse(Collection<Long> reportTypeIdCollection);


    /* reporterId */
    List<Report> findAllByReporterIdAndIsDeletedIsFalse(long reporterId);
    List<Report> findAllByReporterIdInAndIsDeletedIsFalse(Collection<Long> reporterIdCollection);


    /* reportDate */
    List<Report> findAllByReportDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Report> findAllByReportDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Report> findAllByReportDateBetweenAndIsDeletedIsFalse(LocalDateTime from, LocalDateTime to);
}
