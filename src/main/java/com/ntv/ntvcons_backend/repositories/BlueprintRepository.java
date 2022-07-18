package com.ntv.ntvcons_backend.repositories;

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
    Page<Blueprint> findAllByIsDeletedIsFalse(Pageable paging);

    Blueprint getByBlueprintNameAndIsDeletedIsFalse(String bluePrintName);

    /* Id */
    boolean existsByBlueprintIdAndIsDeletedIsFalse(long blueprintId);
    Optional<Blueprint> findByBlueprintIdAndIsDeletedIsFalse(long blueprintId);
    List<Blueprint> findAllByBlueprintIdInAndIsDeletedIsFalse(Collection<Long> blueprintIdCollection);
    /* Id & projectId & blueprintName */
    boolean existsByProjectIdOrBlueprintNameAndBlueprintIdIsNotAndIsDeletedIsFalse
            (long projectId, String blueprintName, long blueprintId);


    /* projectId */
    Optional<Blueprint> findByProjectIdAndIsDeletedIsFalse(long projectId);
    List<Blueprint> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection);


    /* blueprintName */
    Optional<Blueprint> findByBlueprintNameAndIsDeletedIsFalse(String blueprintName);
    List<Blueprint> findAllByBlueprintNameContainsAndIsDeletedIsFalse(String blueprintName);
    Page<Blueprint> findAllByBlueprintNameContainsAndIsDeletedIsFalse(String blueprintName, Pageable paging);
    /* projectId & blueprintName */
    boolean existsByProjectIdOrBlueprintNameAndIsDeletedIsFalse(long projectId, String blueprintName);


    /* designerName */
    List<Blueprint> findAllByDesignerNameAndIsDeletedIsFalse(String designerName);
    Page<Blueprint> findAllByDesignerNameAndIsDeletedIsFalse(String designerName, Pageable paging);
    List<Blueprint> findAllByDesignerNameContainsAndIsDeletedIsFalse(String designerName);
    Page<Blueprint> findAllByDesignerNameContainsAndIsDeletedIsFalse(String designerName, Pageable paging);


    /* estimatedCost */
    List<Blueprint> findAllByEstimatedCostGreaterThanEqualAndIsDeletedIsFalse(double minCost);
    List<Blueprint> findAllByEstimatedCostLessThanEqualAndIsDeletedIsFalse(double maxCost);
    List<Blueprint> findAllByEstimatedCostBetweenAndIsDeletedIsFalse(double minCost, double maxCost);
}
