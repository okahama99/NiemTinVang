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
    Page<TaskAssignment> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<TaskAssignment> findByAssignmentIdAndIsDeletedIsFalse(long assignmentId);
    List<TaskAssignment> findAllByAssignmentIdInAndIsDeletedIsFalse(Collection<Long> assignmentIdCollection);


    /* taskId */
    List<TaskAssignment> findAllByTaskIdAndIsDeletedIsFalse(long taskId);
    List<TaskAssignment> findAllByTaskIdInAndIsDeletedIsFalse(Collection<Long> taskIdCollection);


    /* assignerId */
    List<TaskAssignment> findAllByAssignerIdAndIsDeletedIsFalse(long assignerId);
    List<TaskAssignment> findAllByAssignerIdInAndIsDeletedIsFalse(Collection<Long> assignerIdCollection);


    /* assigneeId */
    List<TaskAssignment> findAllByAssigneeIdAndIsDeletedIsFalse(long assigneeId);
    List<TaskAssignment> findAllByAssigneeIdInAndIsDeletedIsFalse(Collection<Long> assigneeIdCollection);


    /* assignDate */
    List<TaskAssignment> findAllByAssignDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<TaskAssignment> findAllByAssignDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<TaskAssignment> findAllByAssignDateBetweenAndIsDeletedIsFalse(LocalDateTime fromDate, LocalDateTime toDate);
}
