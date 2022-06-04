package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Integer> {
    List<TaskAssignment> findAllByIsDeletedFalse();


    /* Id */
    Optional<TaskAssignment> findByAssignmentIdAndIsDeletedIsFalse(int assignmentId);
    List<TaskAssignment> findAllByAssignmentIdInAndIsDeletedIsFalse(Collection<Integer> assignmentIdCollection);


    /* taskId */
    List<TaskAssignment> findAllByTaskIdAndIsDeletedIsFalse(int taskId);
    List<TaskAssignment> findAllByTaskIdInAndIsDeletedIsFalse(Collection<Integer> taskIdCollection);


    /* assignerId */
    List<TaskAssignment> findAllByAssignerIdAndIsDeletedIsFalse(int assignerId);
    List<TaskAssignment> findAllByAssignerIdInAndIsDeletedIsFalse(Collection<Integer> assignerIdCollection);


    /* assigneeId */
    List<TaskAssignment> findAllByAssigneeIdAndIsDeletedIsFalse(int assigneeId);
    List<TaskAssignment> findAllByAssigneeIdInAndIsDeletedIsFalse(Collection<Integer> assigneeIdCollection);


    /* assignDate */
    List<TaskAssignment> findAllByAssignDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<TaskAssignment> findAllByAssignDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<TaskAssignment> findAllByAssignDateBetweenAndIsDeletedIsFalse(Instant fromDate, Instant toDate);
}
