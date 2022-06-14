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
    Page<Blueprint> findAllByIsDeletedIsFalse(Pageable pageable);

    Blueprint getByBlueprintName(String bluePrintName);

    /* Id */
    Optional<Blueprint> findByBlueprintIdAndIsDeletedIsFalse(long blueprintId);
    List<Blueprint> findAllByBlueprintIdInAndIsDeletedIsFalse(Collection<Long> blueprintIdCollection);


    /* blueprintName */
    Optional<Blueprint> findByBlueprintNameAndIsDeletedIsFalse(String blueprintName);
    List<Blueprint> findAllByBlueprintNameContainsAndIsDeletedIsFalse(String blueprintName);


    /* designerId */
//    List<Blueprint> findAllByDesignerIdAndIsDeletedIsFalse(long designerId);
//    List<Blueprint> findAllByDesignerIdInAndIsDeletedIsFalse(Collection<Long> designerIdCollection);


    /* estimatedCost */
    List<Blueprint> findAllByEstimatedCostGreaterThanEqualAndIsDeletedIsFalse(double minCost);
    List<Blueprint> findAllByEstimatedCostLessThanEqualAndIsDeletedIsFalse(double maxCost);
    List<Blueprint> findAllByEstimatedCostBetweenAndIsDeletedIsFalse(double minCost, double maxCost);
}
