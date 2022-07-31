package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByTaskIdAndStatusNotIn(
            long taskId, Collection<Status> statusCollection);
    Optional<Task> findByTaskIdAndStatusNotIn(
            long taskId, Collection<Status> statusCollection);
    boolean existsAllByTaskIdInAndStatusNotIn(
            Collection<Long> taskIdCollection, Collection<Status> statusCollection);
    List<Task> findAllByTaskIdInAndStatusNotIn(
            Collection<Long> taskIdCollection, Collection<Status> statusCollection);
    /* Id & projectId & taskName */
    /** Check duplicate projectId & taskName for update */
    boolean existsByProjectIdAndTaskNameAndTaskIdIsNotAndStatusNotIn(
            long projectId, String taskName, long taskId, Collection<Status> statusCollection);


    /* projectId */
    List<Task> findAllByProjectIdAndStatusNotIn(
            long projectId, Collection<Status> statusCollection);
    Page<Task> findAllByProjectIdAndStatusNotIn(
            long projectId, Collection<Status> statusCollection, Pageable paging);
    List<Task> findAllByProjectIdInAndStatusNotIn(
            Collection<Long> projectIdCollection, Collection<Status> statusCollection);
    Page<Task> findAllByProjectIdInAndStatusNotIn(
            Collection<Long> projectIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* taskName */
    List<Task> findAllByTaskNameAndStatusNotIn(
            String taskName, Collection<Status> statusCollection); /* List because duplicate = name + projectId */
    Page<Task> findAllByTaskNameAndStatusNotIn(
            String taskName, Collection<Status> statusCollection, Pageable paging);
    List<Task> findAllByTaskNameContainsAndStatusNotIn(
            String taskName, Collection<Status> statusCollection);
    Page<Task> findAllByTaskNameContainsAndStatusNotIn(
            String taskName, Collection<Status> statusCollection, Pageable paging);
    /* projectId & taskName */
    boolean existsByProjectIdAndTaskNameAndStatusNotIn(
            long projectId, String taskName, Collection<Status> statusCollection);


    /* planStartDate */
    List<Task> findAllByPlanStartDateAfterAndStatusNotIn(
            LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Task> findAllByPlanStartDateBeforeAndStatusNotIn(
            LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Task> findAllByPlanStartDateBetweenAndStatusNotIn(
            LocalDateTime from, LocalDateTime to, Collection<Status> statusCollection);
    /* planEndDate */
    List<Task> findAllByPlanEndDateAfterAndStatusNotIn(
            LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Task> findAllByPlanEndDateBeforeAndStatusNotIn(
            LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Task> findAllByPlanEndDateBetweenAndStatusNotIn(
            LocalDateTime from, LocalDateTime to, Collection<Status> statusCollection);
    /* planStartDate & planEndDate in range */
    List<Task> findAllByPlanStartDateAfterAndPlanEndDateBeforeAndStatusNotIn(
            LocalDateTime afterDate, LocalDateTime beforeDate, Collection<Status> statusCollection);


    /* actualStartDate */
    List<Task> findAllByActualStartDateAfterAndStatusNotIn(
            LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Task> findAllByActualStartDateBeforeAndStatusNotIn(
            LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Task> findAllByActualStartDateBetweenAndStatusNotIn(
            LocalDateTime from, LocalDateTime to, Collection<Status> statusCollection);
    /* actualEndDate */
    List<Task> findAllByActualEndDateAfterAndStatusNotIn(
            LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Task> findAllByActualEndDateBeforeAndStatusNotIn(
            LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Task> findAllByActualEndDateBetweenAndStatusNotIn(
            LocalDateTime from, LocalDateTime to, Collection<Status> statusCollection);
    /* actualStartDate & ActualEndDate in range */
    List<Task> findAllByActualStartDateAfterAndActualEndDateBeforeAndStatusNotIn(
            LocalDateTime afterDate, LocalDateTime beforeDate, Collection<Status> statusCollection);
}
