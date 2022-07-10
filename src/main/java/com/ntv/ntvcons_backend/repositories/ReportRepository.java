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
    Page<Report> findAllByIsDeletedIsFalse(Pageable paging);


    /* Id */
    boolean existsByReportIdAndIsDeletedIsFalse(long reportId);
    Optional<Report> findByReportIdAndIsDeletedIsFalse(long reportId);
    boolean existsAllByReportIdInAndIsDeletedIsFalse(Collection<Long> reportIdCollection);
    List<Report> findAllByReportIdInAndIsDeletedIsFalse(Collection<Long> reportIdCollection);
    /* Id & reportName */
    boolean existsByReportNameAndReportIdIsNotAndIsDeletedIsFalse(String reportName, long reportId);


    /* reportTypeId */
    List<Report> findAllByReportTypeIdAndIsDeletedIsFalse(long reportTypeId);
    Page<Report> findAllByReportTypeIdAndIsDeletedIsFalse(long reportTypeId, Pageable paging);
    List<Report> findAllByReportTypeIdInAndIsDeletedIsFalse(Collection<Long> reportTypeIdCollection);
    Page<Report> findAllByReportTypeIdInAndIsDeletedIsFalse(Collection<Long> reportTypeIdCollection, Pageable paging);


    /* projectId */
    List<Report> findAllByProjectIdAndIsDeletedIsFalse(long projectId);
    Page<Report> findAllByProjectIdAndIsDeletedIsFalse(long projectId, Pageable paging);
    List<Report> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection);
    Page<Report> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection, Pageable paging);


    /* reporterId */
    List<Report> findAllByReporterIdAndIsDeletedIsFalse(long reporterId);
    Page<Report> findAllByReporterIdAndIsDeletedIsFalse(long reporterId, Pageable paging);
    List<Report> findAllByReporterIdInAndIsDeletedIsFalse(Collection<Long> reporterIdCollection);
    Page<Report> findAllByReporterIdInAndIsDeletedIsFalse(Collection<Long> reporterIdCollection, Pageable paging);
    /* projectId & reporterId */
    List<Report> findAllByProjectIdAndReporterIdAndIsDeletedIsFalse(long projectId, long reporterId);


    /* reportName */
    Optional<Report> findByReportNameAndIsDeletedIsFalse(String reportName);
    List<Report> findAllByReportNameContainsAndIsDeletedIsFalse(String reportName);
    Page<Report> findAllByReportNameContainsAndIsDeletedIsFalse(String reportName, Pageable paging);
    /* reportName */
    boolean existsByReportNameAndIsDeletedIsFalse(String reportName);


    /* reportDate */
    List<Report> findAllByReportDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Report> findAllByReportDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Report> findAllByReportDateBetweenAndIsDeletedIsFalse(LocalDateTime from, LocalDateTime to);
}
