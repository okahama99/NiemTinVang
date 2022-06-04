package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findAllByIsDeletedFalse();


    /* Id */
    Optional<Report> findByReportIdAndIsDeletedIsFalse(int reportId);
    List<Report> findAllByReportIdInAndIsDeletedIsFalse(Collection<Integer> reportIdCollection);


    /* projectId */
    List<Report> findAllByProjectIdAndIsDeletedIsFalse(int projectId);
    List<Report> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Integer> projectIdCollection);


    /* reportTypeId */
    List<Report> findAllByReportTypeIdAndIsDeletedIsFalse(int reportTypeId);
    List<Report> findAllByReportTypeIdInAndIsDeletedIsFalse(Collection<Integer> reportTypeIdCollection);


    /* reporterId */
    List<Report> findAllByReporterIdAndIsDeletedIsFalse(int reporterId);
    List<Report> findAllByReporterIdInAndIsDeletedIsFalse(Collection<Integer> reporterIdCollection);


    /* reportDate */
    List<Report> findAllByReportDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Report> findAllByReportDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Report> findAllByReportDateBetweenAndIsDeletedIsFalse(Instant from, Instant to);
}
