package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findAllByIsDeletedFalse();


    /* Id */
    Optional<Project> findByProjectIdAndIsDeletedIsFalse(int projectId);
    List<Project> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Integer> projectIdCollection);


    /* projectName */
    Optional<Project> findByProjectNameAndIsDeletedIsFalse(String projectName);
    List<Project> findAllByProjectNameLikeAndIsDeletedIsFalse(String projectName);


    /* locationId */
    List<Project> findAllByLocationIdAndIsDeletedIsFalse(int locationId);
    List<Project> findAllByLocationIdInAndIsDeletedIsFalse(Collection<Integer> locationIdCollection);


    /* planStartDate */
    List<Project> findAllByPlanStartDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Project> findAllByPlanStartDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Project> findAllByPlanStartDateBetweenAndIsDeletedIsFalse(Instant from, Instant to);
    /* planEndDate */
    List<Project> findAllByPlanEndDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Project> findAllByPlanEndDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Project> findAllByPlanEndDateBetweenAndIsDeletedIsFalse(Instant from, Instant to);
    /* planStartDate & planEndDate in range */
    List<Project> findAllByPlanEndDateAfterAndPlanEndDateBeforeAndIsDeletedIsFalse
            (Instant afterDate, Instant beforeDate);


    /* actualStartDate */
    List<Project> findAllByActualStartDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Project> findAllByActualStartDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Project> findAllByActualStartDateBetweenAndIsDeletedIsFalse(Instant from, Instant to);
    /* actualEndDate */
    List<Project> findAllByActualEndDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Project> findAllByActualEndDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Project> findAllByActualEndDateBetweenAndIsDeletedIsFalse(Instant from, Instant to);
    /* actualStartDate & ActualEndDate in range */
    List<Project> findAllByActualEndDateAfterAndActualEndDateBeforeAndIsDeletedIsFalse
            (Instant afterDate, Instant beforeDate);


    /* estimatedCost */
    List<Project> findAllByEstimatedCostGreaterThanEqualAndIsDeletedIsFalse(double minCost);
    List<Project> findAllByEstimatedCostLessThanEqualAndIsDeletedIsFalse(double maxCost);
    List<Project> findAllByEstimatedCostBetweenAndIsDeletedIsFalse(double minCost, double maxCost);


    /* actualCost */
    List<Project> findAllByActualCostGreaterThanEqualAndIsDeletedIsFalse(double minCost);
    List<Project> findAllByActualCostLessThanEqualAndIsDeletedIsFalse(double maxCost);
    List<Project> findAllByActualCostBetweenAndIsDeletedIsFalse(double minCost, double maxCost);
}
