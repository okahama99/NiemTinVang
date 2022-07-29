package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
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
    Page<Report> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByReportIdAndStatusNotIn(
            long reportId, Collection<Status> statusCollection);
    Optional<Report> findByReportIdAndStatusNotIn(
            long reportId, Collection<Status> statusCollection);
    boolean existsAllByReportIdInAndStatusNotIn(
            Collection<Long> reportIdCollection, Collection<Status> statusCollection);
    List<Report> findAllByReportIdInAndStatusNotIn(
            Collection<Long> reportIdCollection, Collection<Status> statusCollection);
    /* Id & projectId & reportName */
    /** Check duplicate for Update */
    boolean existsByProjectIdAndReportNameAndReportIdIsNotAndStatusNotIn(
            long projectId, String reportName, long reportId, Collection<Status> statusCollection);


    /* reportTypeId */
    List<Report> findAllByReportTypeIdAndStatusNotIn(
            long reportTypeId, Collection<Status> statusCollection);
    Page<Report> findAllByReportTypeIdAndStatusNotIn(
            long reportTypeId, Collection<Status> statusCollection, Pageable paging);
    List<Report> findAllByReportTypeIdInAndStatusNotIn(
            Collection<Long> reportTypeIdCollection, Collection<Status> statusCollection);
    Page<Report> findAllByReportTypeIdInAndStatusNotIn(
            Collection<Long> reportTypeIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* projectId */
    List<Report> findAllByProjectIdAndStatusNotIn(
            long projectId, Collection<Status> statusCollection);
    Page<Report> findAllByProjectIdAndStatusNotIn(
            long projectId, Collection<Status> statusCollection, Pageable paging);
    List<Report> findAllByProjectIdInAndStatusNotIn(
            Collection<Long> projectIdCollection, Collection<Status> statusCollection);
    Page<Report> findAllByProjectIdInAndStatusNotIn(
            Collection<Long> projectIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* reporterId */
    List<Report> findAllByReporterIdAndStatusNotIn(
            long reporterId, Collection<Status> statusCollection);
    Page<Report> findAllByReporterIdAndStatusNotIn(
            long reporterId, Collection<Status> statusCollection, Pageable paging);
    List<Report> findAllByReporterIdInAndStatusNotIn(
            Collection<Long> reporterIdCollection, Collection<Status> statusCollection);
    Page<Report> findAllByReporterIdInAndStatusNotIn(
            Collection<Long> reporterIdCollection, Collection<Status> statusCollection, Pageable paging);
    /* projectId & reporterId */
    List<Report> findAllByProjectIdAndReporterIdAndStatusNotIn(
            long projectId, long reporterId, Collection<Status> statusCollection);


    /* reportName */
    List<Report> findAllByReportNameAndStatusNotIn(
            String reportName, Collection<Status> statusCollection);
    Page<Report> findAllByReportNameAndStatusNotIn(
            String reportName, Collection<Status> statusCollection, Pageable paging);
    List<Report> findAllByReportNameContainsAndStatusNotIn(
            String reportName, Collection<Status> statusCollection);
    Page<Report> findAllByReportNameContainsAndStatusNotIn(
            String reportName, Collection<Status> statusCollection, Pageable paging);
    /* projectId & reportName */
    /** Check duplicate for Create */
    boolean existsByProjectIdAndReportNameAndStatusNotIn(
            long projectId, String reportName, Collection<Status> statusCollection);


    /* reportDate */
    List<Report> findAllByReportDateAfterAndStatusNotIn(
            LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Report> findAllByReportDateBeforeAndStatusNotIn(
            LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Report> findAllByReportDateBetweenAndStatusNotIn(
            LocalDateTime from, LocalDateTime to, Collection<Status> statusCollection);
}
