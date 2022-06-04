package com.ntv.ntvcons_backend.services.projectBlueprint;

import com.ntv.ntvcons_backend.entities.ProjectBlueprint;
import com.ntv.ntvcons_backend.entities.ProjectBlueprintModels.ShowProjectBlueprintModel;

import java.util.Collection;
import java.util.List;

public interface ProjectBlueprintService {
    /* CREATE */
    ProjectBlueprint createProjectBlueprint(String projectBlueprintName, int designerId,
                                            double projectBlueprintCost);

    /* READ */
    List<ShowProjectBlueprintModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ProjectBlueprint> getAllByIdIn(Collection<Integer> projectBlueprintIdCollection);

    List<ProjectBlueprint> getAllByProjectBlueprintNameLike(String projectBlueprintName);

    List<ProjectBlueprint> getAllByProjectBlueprintCostBetween(double from, double to);

    ProjectBlueprint getByDesignerId(int designerId);

    ProjectBlueprint getById(int projectBlueprintId);

    /* UPDATE */
    boolean updateProjectBlueprint(ShowProjectBlueprintModel showProjectBlueprintModel);

    /* DELETE */
    boolean deleteProjectBlueprint(int projectBlueprintId);
}
