package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
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
    Page<Project> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByProjectIdAndStatusNotIn
            (long projectId, Collection<Status> statusCollection);
    Optional<Project> findByProjectIdAndStatusNotIn
            (long projectId, Collection<Status> statusCollection);
    Page<Project> findAllByProjectIdAndStatusNotIn
            (long projectId, Collection<Status> statusCollection, Pageable paging);
    boolean existsAllByProjectIdInAndStatusNotIn
            (Collection<Long> projectIdCollection, Collection<Status> statusCollection);
    List<Project> findAllByProjectIdInAndStatusNotIn
            (Collection<Long> projectIdCollection, Collection<Status> statusCollection);
    Page<Project> findAllByProjectIdInAndStatusNotIn
            (Collection<Long> projectIdCollection, Collection<Status> statusCollection, Pageable paging);
    /* Id & projectName */
    /** Check duplicate for Update */
    boolean existsByProjectNameAndProjectIdIsNotAndStatusNotIn(
            String projectName, long projectId, Collection<Status> statusCollection);


    /* projectName */
    /** Check duplicate for Update */
    boolean existsByProjectNameAndStatusNotIn(
            String projectName, Collection<Status> statusCollection);
    Optional<Project> findByProjectNameAndStatusNotIn(
            String projectName, Collection<Status> statusCollection);
    Project getByProjectNameAndStatusNotIn(
            String projectName, Collection<Status> statusCollection); /* Redundant, use above with .orElse() */
    List<Project> findAllByProjectNameContainsAndStatusNotIn(
            String projectName, Collection<Status> statusCollection);
    Page<Project> findAllByProjectNameContainsAndStatusNotIn(
            String projectName, Collection<Status> statusCollection, Pageable paging);


    /* locationId */
    List<Project> findAllByLocationIdAndStatusNotIn(
            long locationId, Collection<Status> statusCollection);
    Page<Project> findAllByLocationIdAndStatusNotIn(
            long locationId, Collection<Status> statusCollection, Pageable paging);
    List<Project> findAllByLocationIdInAndStatusNotIn(
            Collection<Long> locationIdCollection, Collection<Status> statusCollection);
    Page<Project> findAllByLocationIdInAndStatusNotIn(
            Collection<Long> locationIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* planStartDate */
    List<Project> findAllByPlanStartDateAfterAndStatusNotIn(
            LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Project> findAllByPlanStartDateBeforeAndStatusNotIn(
            LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Project> findAllByPlanStartDateBetweenAndStatusNotIn(
            LocalDateTime from, LocalDateTime to, Collection<Status> statusCollection);
    /* planEndDate */
    List<Project> findAllByPlanEndDateAfterAndStatusNotIn(
            LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Project> findAllByPlanEndDateBeforeAndStatusNotIn(
            LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Project> findAllByPlanEndDateBetweenAndStatusNotIn(
            LocalDateTime from, LocalDateTime to, Collection<Status> statusCollection);
    /* planStartDate & planEndDate in range */
    List<Project> findAllByPlanEndDateAfterAndPlanEndDateBeforeAndStatusNotIn(
            LocalDateTime afterDate, LocalDateTime beforeDate, Collection<Status> statusCollection);


    /* actualStartDate */
    List<Project> findAllByActualStartDateAfterAndStatusNotIn(
            LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Project> findAllByActualStartDateBeforeAndStatusNotIn(
            LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Project> findAllByActualStartDateBetweenAndStatusNotIn(
            LocalDateTime from, LocalDateTime to, Collection<Status> statusCollection);
    /* actualEndDate */
    List<Project> findAllByActualEndDateAfterAndStatusNotIn(
            LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Project> findAllByActualEndDateBeforeAndStatusNotIn(
            LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Project> findAllByActualEndDateBetweenAndStatusNotIn(
            LocalDateTime from, LocalDateTime to, Collection<Status> statusCollection);
    /* actualStartDate & ActualEndDate in range */
    List<Project> findAllByActualEndDateAfterAndActualEndDateBeforeAndStatusNotIn(
            LocalDateTime afterDate, LocalDateTime beforeDate, Collection<Status> statusCollection);


    /* estimatedCost */
    List<Project> findAllByEstimatedCostGreaterThanEqualAndStatusNotIn(
            double minCost, Collection<Status> statusCollection);
    List<Project> findAllByEstimatedCostLessThanEqualAndStatusNotIn(
            double maxCost, Collection<Status> statusCollection);
    List<Project> findAllByEstimatedCostBetweenAndStatusNotIn(
            double minCost, double maxCost, Collection<Status> statusCollection);


    /* actualCost */
    List<Project> findAllByActualCostGreaterThanEqualAndStatusNotIn(
            double minCost, Collection<Status> statusCollection);
    List<Project> findAllByActualCostLessThanEqualAndStatusNotIn(
            double maxCost, Collection<Status> statusCollection);
    List<Project> findAllByActualCostBetweenAndStatusNotIn(
            double minCost, double maxCost, Collection<Status> statusCollection);
}
