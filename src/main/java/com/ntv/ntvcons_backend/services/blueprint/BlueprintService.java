package com.ntv.ntvcons_backend.services.blueprint;

import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.ProjectBlueprintModels.ShowProjectBlueprintModel;

import java.util.Collection;
import java.util.List;

public interface BlueprintService {
    /* CREATE */
    Blueprint createProjectBlueprint(String projectBlueprintName, int designerId,
                                     double projectBlueprintCost);

    /* READ */
    List<ShowProjectBlueprintModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Blueprint> getAllByIdIn(Collection<Integer> projectBlueprintIdCollection);

    List<Blueprint> getAllByProjectBlueprintNameLike(String projectBlueprintName);

    List<Blueprint> getAllByProjectBlueprintCostBetween(double from, double to);

    Blueprint getByDesignerId(int designerId);

    Blueprint getById(int projectBlueprintId);

    /* UPDATE */
    boolean updateProjectBlueprint(ShowProjectBlueprintModel showProjectBlueprintModel);

    /* DELETE */
    boolean deleteProjectBlueprint(int projectBlueprintId);
}
