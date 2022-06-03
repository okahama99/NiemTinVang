package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.Task;
import com.ntv.ntvcons_backend.entities.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByIsDeletedFalse();


    /* Id */
    Optional<Task> findByTaskIdAndIsDeletedIsFalse(int taskId);
    List<Task> findAllByTaskIdInAndIsDeletedIsFalse(Collection<Integer> taskIdCollection);


    /* projectId */
    List<Task> findAllByProjectIdAndIsDeletedIsFalse(int projectId);
    List<Task> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Integer> projectIdCollection);


    /* creatorId */
    List<Task> findAllByCreatorIdAndIsDeletedIsFalse(int creatorId);
    List<Task> findAllByCreatorIdInAndIsDeletedIsFalse(Collection<Integer> creatorIdCollection);


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
    List<Task> findAllByPlanEndDateAfterAndPlanEndDateBeforeAndIsDeletedIsFalse
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
    List<Task> findAllByActualEndDateAfterAndActualEndDateBeforeAndIsDeletedIsFalse
            (Instant afterDate, Instant beforeDate);
}
