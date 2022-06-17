package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByIsDeletedIsFalse(Pageable pageable);

    Project getByProjectNameAndIsDeletedIsFalse(String projectName);

    /* Id */
    Page<Project> findAllByProjectIdAndIsDeletedIsFalse(long projectId, Pageable pageable);


    /* projectName */
    Optional<Project> findByProjectNameAndIsDeletedIsFalse(String projectName);
    List<Project> findAllByProjectNameContainsAndIsDeletedIsFalse(String projectName);


    /* locationId */
    List<Project> findAllByLocationIdAndIsDeletedIsFalse(long locationId);
    List<Project> findAllByLocationIdInAndIsDeletedIsFalse(Collection<Long> locationIdCollection);


    /* planStartDate */
    List<Project> findAllByPlanStartDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Project> findAllByPlanStartDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Project> findAllByPlanStartDateBetweenAndIsDeletedIsFalse(LocalDateTime from, LocalDateTime to);
    /* planEndDate */
    List<Project> findAllByPlanEndDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Project> findAllByPlanEndDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Project> findAllByPlanEndDateBetweenAndIsDeletedIsFalse(LocalDateTime from, LocalDateTime to);
    /* planStartDate & planEndDate in range */
    List<Project> findAllByPlanEndDateAfterAndPlanEndDateBeforeAndIsDeletedIsFalse
            (LocalDateTime afterDate, LocalDateTime beforeDate);


    /* actualStartDate */
    List<Project> findAllByActualStartDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Project> findAllByActualStartDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Project> findAllByActualStartDateBetweenAndIsDeletedIsFalse(LocalDateTime from, LocalDateTime to);
    /* actualEndDate */
    List<Project> findAllByActualEndDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Project> findAllByActualEndDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Project> findAllByActualEndDateBetweenAndIsDeletedIsFalse(LocalDateTime from, LocalDateTime to);
    /* actualStartDate & ActualEndDate in range */
    List<Project> findAllByActualEndDateAfterAndActualEndDateBeforeAndIsDeletedIsFalse
            (LocalDateTime afterDate, LocalDateTime beforeDate);


    /* estimatedCost */
    List<Project> findAllByEstimatedCostGreaterThanEqualAndIsDeletedIsFalse(double minCost);
    List<Project> findAllByEstimatedCostLessThanEqualAndIsDeletedIsFalse(double maxCost);
    List<Project> findAllByEstimatedCostBetweenAndIsDeletedIsFalse(double minCost, double maxCost);


    /* actualCost */
    List<Project> findAllByActualCostGreaterThanEqualAndIsDeletedIsFalse(double minCost);
    List<Project> findAllByActualCostLessThanEqualAndIsDeletedIsFalse(double maxCost);
    List<Project> findAllByActualCostBetweenAndIsDeletedIsFalse(double minCost, double maxCost);
}
