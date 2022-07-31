package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.TaskReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskReportRepository extends JpaRepository<TaskReport, Long> {
    Page<TaskReport> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByTaskReportIdAndStatusNotIn(
            long taskReportId, Collection<Status> statusCollection);
    Optional<TaskReport> findByTaskReportIdAndStatusNotIn(
            long taskReportId, Collection<Status> statusCollection);
    List<TaskReport> findAllByTaskReportIdInAndStatusNotIn(
            Collection<Long> taskReportIdCollection, Collection<Status> statusCollection);
    /* Id & reportId & taskId & taskProgress */
    /** Check duplicate reportId & taskId & taskProgress for update */
    boolean existsByReportIdAndTaskIdAndTaskReportIdIsNotAndStatusNotIn(
            long reportId, long taskId, long taskReportId, Collection<Status> statusCollection);


    /* reportId */
    List<TaskReport> findAllByReportIdAndStatusNotIn(
            long reportId, Collection<Status> statusCollection);
    List<TaskReport> findAllByReportIdInAndStatusNotIn(
            Collection<Long> reportIdCollection, Collection<Status> statusCollection);


    /* taskId */
    List<TaskReport> findAllByTaskIdAndStatusNotIn(
            long taskId, Collection<Status> statusCollection);
    List<TaskReport> findAllByTaskIdInAndStatusNotIn(
            Collection<Long> taskIdCollection, Collection<Status> statusCollection);
    /* Id & reportId & taskId */
    boolean existsByReportIdAndTaskIdAndStatusNotIn(
            long reportId, long taskId, Collection<Status> statusCollection);
}
