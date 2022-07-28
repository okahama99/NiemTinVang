package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.TaskAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {
    Page<TaskAssignment> findAllByStatusNotIn
            (Collection<Status> statusCollection, Pageable paging);


    /* Id */
    Optional<TaskAssignment> findByAssignmentIdAndStatusNotIn
            (long assignmentId, Collection<Status> statusCollection);
    List<TaskAssignment> findAllByAssignmentIdInAndStatusNotIn
            (Collection<Long> assignmentIdCollection, Collection<Status> statusCollection);
    /* Id & taskId & assignerId & assigneeId */
    boolean existsByTaskIdAndAssignerIdAndAssigneeIdAndAssignmentIdIsNotAndStatusNotIn
            (long taskId, long assignerId, long assigneeId, long assignmentId, Collection<Status> statusCollection);


    /* taskId */
    Optional<TaskAssignment> findByTaskIdAndStatusNotIn
            (long taskId, Collection<Status> statusCollection);
    List<TaskAssignment> findAllByTaskIdInAndStatusNotIn
            (Collection<Long> taskIdCollection, Collection<Status> statusCollection);
    Page<TaskAssignment> findAllByTaskIdInAndStatusNotIn
            (Collection<Long> taskIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* assignerId */
    List<TaskAssignment> findAllByAssignerIdAndStatusNotIn
            (long assignerId, Collection<Status> statusCollection);
    Page<TaskAssignment> findAllByAssignerIdAndStatusNotIn
            (long assignerId, Collection<Status> statusCollection, Pageable paging);
    List<TaskAssignment> findAllByAssignerIdInAndStatusNotIn
            (Collection<Long> assignerIdCollection, Collection<Status> statusCollection);
    Page<TaskAssignment> findAllByAssignerIdInAndStatusNotIn
            (Collection<Long> assignerIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* assigneeId */
    List<TaskAssignment> findAllByAssigneeIdAndStatusNotIn
            (long assigneeId, Collection<Status> statusCollection);
    Page<TaskAssignment> findAllByAssigneeIdAndStatusNotIn
            (long assigneeId, Collection<Status> statusCollection, Pageable paging);
    List<TaskAssignment> findAllByAssigneeIdInAndStatusNotIn
            (Collection<Long> assigneeIdCollection, Collection<Status> statusCollection);
    Page<TaskAssignment> findAllByAssigneeIdInAndStatusNotIn
            (Collection<Long> assigneeIdCollection, Collection<Status> statusCollection, Pageable paging);
    /* taskId & assignerId & assigneeId */
    boolean existsByTaskIdAndAssignerIdAndAssigneeIdAndStatusNotIn
            (long taskId, long assignerId, long assigneeId, Collection<Status> statusCollection);


    /* assignDate */
    List<TaskAssignment> findAllByAssignDateAfterAndStatusNotIn
            (LocalDateTime afterDate, Collection<Status> statusCollection);
    List<TaskAssignment> findAllByAssignDateBeforeAndStatusNotIn
            (LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<TaskAssignment> findAllByAssignDateBetweenAndStatusNotIn
            (LocalDateTime fromDate, LocalDateTime toDate, Collection<Status> statusCollection);
}
