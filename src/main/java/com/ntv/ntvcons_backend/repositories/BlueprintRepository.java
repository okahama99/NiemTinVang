package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.Blueprint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlueprintRepository extends JpaRepository<Blueprint, Long> {
    Page<Blueprint> findAllByStatusNotIn(Collection<Status> statusCollection, Pageable paging);

    Blueprint getByBlueprintNameAndStatusNotIn(String bluePrintName, Collection<Status> statusCollection);

    /* Id */
    boolean existsByBlueprintIdAndStatusNotIn(long blueprintId, Collection<Status> statusCollection);
    Optional<Blueprint> findByBlueprintIdAndStatusNotIn(long blueprintId, Collection<Status> statusCollection);
    List<Blueprint> findAllByBlueprintIdInAndStatusNotIn
            (Collection<Long> blueprintIdCollection, Collection<Status> statusCollection);
    /* Id & (projectId || blueprintName) */
    /** Check duplicate for Update */
    boolean existsByProjectIdOrBlueprintNameAndBlueprintIdIsNotAndStatusNotIn
            (long projectId, String blueprintName, long blueprintId, Collection<Status> statusCollection);


    /* projectId */
    Optional<Blueprint> findByProjectIdAndStatusNotIn(long projectId, Collection<Status> statusCollection);
    List<Blueprint> findAllByProjectIdInAndStatusNotIn
            (Collection<Long> projectIdCollection, Collection<Status> statusCollection);


    /* blueprintName */
    Optional<Blueprint> findByBlueprintNameAndStatusNotIn
            (String blueprintName, Collection<Status> statusCollection);
    List<Blueprint> findAllByBlueprintNameContainsAndStatusNotIn
            (String blueprintName, Collection<Status> statusCollection);
    Page<Blueprint> findAllByBlueprintNameContainsAndStatusNotIn
            (String blueprintName, Collection<Status> statusCollection, Pageable paging);
    /* projectId || blueprintName */
    /** Check duplicate for Create */
    boolean existsByProjectIdOrBlueprintNameAndStatusNotIn
            (long projectId, String blueprintName, Collection<Status> statusCollection);


    /* designerName */
    List<Blueprint> findAllByDesignerNameAndStatusNotIn
            (String designerName, Collection<Status> statusCollection);
    Page<Blueprint> findAllByDesignerNameAndStatusNotIn
            (String designerName, Collection<Status> statusCollection, Pageable paging);
    List<Blueprint> findAllByDesignerNameContainsAndStatusNotIn
            (String designerName, Collection<Status> statusCollection);
    Page<Blueprint> findAllByDesignerNameContainsAndStatusNotIn
            (String designerName, Collection<Status> statusCollection, Pageable paging);


    /* estimatedCost */
    List<Blueprint> findAllByEstimatedCostGreaterThanEqualAndStatusNotIn
            (double minCost, Collection<Status> statusCollection);
    List<Blueprint> findAllByEstimatedCostLessThanEqualAndStatusNotIn
            (double maxCost, Collection<Status> statusCollection);
    List<Blueprint> findAllByEstimatedCostBetweenAndStatusNotIn
            (double minCost, double maxCost, Collection<Status> statusCollection);
}
