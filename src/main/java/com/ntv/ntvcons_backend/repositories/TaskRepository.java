package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
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
    List<Task> findAllByTaskNameLikeAndIsDeletedIsFalse(String taskName);


    /* planStartDate */
    List<Task> findAllByPlanStartDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Task> findAllByPlanStartDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Task> findAllByPlanStartDateBetweenAndIsDeletedIsFalse(Instant from, Instant to);
    /* planEndDate */
    List<Task> findAllByPlanEndDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Task> findAllByPlanEndDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Task> findAllByPlanEndDateBetweenAndIsDeletedIsFalse(Instant from, Instant to);
    /* planStartDate & planEndDate in range */
    List<Task> findAllByPlanStartDateAfterAndPlanEndDateBeforeAndIsDeletedIsFalse
            (Instant afterDate, Instant beforeDate);


    /* actualStartDate */
    List<Task> findAllByActualStartDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Task> findAllByActualStartDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Task> findAllByActualStartDateBetweenAndIsDeletedIsFalse(Instant from, Instant to);
    /* actualEndDate */
    List<Task> findAllByActualEndDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Task> findAllByActualEndDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Task> findAllByActualEndDateBetweenAndIsDeletedIsFalse(Instant from, Instant to);
    /* actualStartDate & ActualEndDate in range */
    List<Task> findAllByActualStartDateAfterAndActualEndDateBeforeAndIsDeletedIsFalse
            (Instant afterDate, Instant beforeDate);
}
