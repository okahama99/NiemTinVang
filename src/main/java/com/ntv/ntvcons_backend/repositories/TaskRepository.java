package com.ntv.ntvcons_backend.repositories;

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
    Page<Task> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<Task> findByTaskIdAndIsDeletedIsFalse(long taskId);
    List<Task> findAllByTaskIdInAndIsDeletedIsFalse(Collection<Long> taskIdCollection);


    /* projectId */
    List<Task> findAllByProjectIdAndIsDeletedIsFalse(long projectId);
    List<Task> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection);


    /* taskName */
    Optional<Task> findByTaskNameAndIsDeletedIsFalse(String taskName);
    List<Task> findAllByTaskNameContainsAndIsDeletedIsFalse(String taskName);


    /* planStartDate */
    List<Task> findAllByPlanStartDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Task> findAllByPlanStartDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Task> findAllByPlanStartDateBetweenAndIsDeletedIsFalse(LocalDateTime from, LocalDateTime to);
    /* planEndDate */
    List<Task> findAllByPlanEndDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Task> findAllByPlanEndDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Task> findAllByPlanEndDateBetweenAndIsDeletedIsFalse(LocalDateTime from, LocalDateTime to);
    /* planStartDate & planEndDate in range */
    List<Task> findAllByPlanStartDateAfterAndPlanEndDateBeforeAndIsDeletedIsFalse
            (LocalDateTime afterDate, LocalDateTime beforeDate);


    /* actualStartDate */
    List<Task> findAllByActualStartDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Task> findAllByActualStartDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Task> findAllByActualStartDateBetweenAndIsDeletedIsFalse(LocalDateTime from, LocalDateTime to);
    /* actualEndDate */
    List<Task> findAllByActualEndDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Task> findAllByActualEndDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Task> findAllByActualEndDateBetweenAndIsDeletedIsFalse(LocalDateTime from, LocalDateTime to);
    /* actualStartDate & ActualEndDate in range */
    List<Task> findAllByActualStartDateAfterAndActualEndDateBeforeAndIsDeletedIsFalse
            (LocalDateTime afterDate, LocalDateTime beforeDate);
}
