package com.ntv.ntvcons_backend.repositories;

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
    Page<TaskAssignment> findAllByIsDeletedIsFalse(Pageable paging);


    /* Id */
    Optional<TaskAssignment> findByAssignmentIdAndIsDeletedIsFalse(long assignmentId);
    List<TaskAssignment> findAllByAssignmentIdInAndIsDeletedIsFalse(Collection<Long> assignmentIdCollection);
    /* Id & taskId & assignerId & assigneeId */
    boolean existsByTaskIdAndAssignerIdAndAssigneeIdAndAssignmentIdIsNotAndIsDeletedIsFalse
            (long taskId, long assignerId, long assigneeId, long assignmentId);


    /* taskId */
    Optional<TaskAssignment> findByTaskIdAndIsDeletedIsFalse(long taskId);
    List<TaskAssignment> findAllByTaskIdInAndIsDeletedIsFalse(Collection<Long> taskIdCollection);
    Page<TaskAssignment> findAllByTaskIdInAndIsDeletedIsFalse(Collection<Long> taskIdCollection, Pageable paging);


    /* assignerId */
    List<TaskAssignment> findAllByAssignerIdAndIsDeletedIsFalse(long assignerId);
    Page<TaskAssignment> findAllByAssignerIdAndIsDeletedIsFalse(long assignerId, Pageable paging);
    List<TaskAssignment> findAllByAssignerIdInAndIsDeletedIsFalse(Collection<Long> assignerIdCollection);
    Page<TaskAssignment> findAllByAssignerIdInAndIsDeletedIsFalse(Collection<Long> assignerIdCollection, Pageable paging);


    /* assigneeId */
    List<TaskAssignment> findAllByAssigneeIdAndIsDeletedIsFalse(long assigneeId);
    Page<TaskAssignment> findAllByAssigneeIdAndIsDeletedIsFalse(long assigneeId, Pageable paging);
    List<TaskAssignment> findAllByAssigneeIdInAndIsDeletedIsFalse(Collection<Long> assigneeIdCollection);
    Page<TaskAssignment> findAllByAssigneeIdInAndIsDeletedIsFalse(Collection<Long> assigneeIdCollection, Pageable paging);
    /* taskId & assignerId & assigneeId */
    boolean existsByTaskIdAndAssignerIdAndAssigneeIdAndIsDeletedIsFalse(long taskId, long assignerId, long assigneeId);


    /* assignDate */
    List<TaskAssignment> findAllByAssignDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<TaskAssignment> findAllByAssignDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<TaskAssignment> findAllByAssignDateBetweenAndIsDeletedIsFalse(LocalDateTime fromDate, LocalDateTime toDate);
}
