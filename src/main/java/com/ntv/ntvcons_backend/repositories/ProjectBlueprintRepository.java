package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Post;
import com.ntv.ntvcons_backend.entities.ProjectBlueprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectBlueprintRepository extends JpaRepository<ProjectBlueprint, Integer> {
    List<ProjectBlueprint> findAllByIsDeletedFalse();


    /* Id */
    Optional<ProjectBlueprint> findByBlueprintIdAndIsDeletedIsFalse(int blueprintId);
    List<ProjectBlueprint> findAllByBlueprintIdInAndIsDeletedIsFalse(Collection<Integer> blueprintIdCollection);


    /* blueprintName */
    Optional<ProjectBlueprint> findByBlueprintNameAndIsDeletedIsFalse(String blueprintName);
    List<ProjectBlueprint> findAllByBlueprintNameLikeAndIsDeletedIsFalse(String blueprintName);


    /* designerId */
    List<ProjectBlueprint> findAllByDesignerIdAndIsDeletedIsFalse(int designerId);
    List<ProjectBlueprint> findAllByDesignerIdInAndIsDeletedIsFalse(Collection<Integer> designerIdCollection);


    /* estimatedCost */
    List<ProjectBlueprint> findAllByEstimatedCostGreaterThanEqualAndIsDeletedIsFalse(double minCost);
    List<ProjectBlueprint> findAllByEstimatedCostLessThanEqualAndIsDeletedIsFalse(double maxCost);
    List<ProjectBlueprint> findAllByEstimatedCostBetweenAndIsDeletedIsFalse(double minCost, double maxCost);
}
