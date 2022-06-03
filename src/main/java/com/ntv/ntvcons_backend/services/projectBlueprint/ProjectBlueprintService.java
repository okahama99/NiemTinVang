package com.ntv.ntvcons_backend.services.projectBlueprint;

import com.ntv.ntvcons_backend.entities.ProjectBlueprint;
import com.ntv.ntvcons_backend.entities.ProjectBlueprintModels.ProjectBlueprintModel;
import java.util.Collection;
import java.util.List;

public interface ProjectBlueprintService {
    /* CREATE */
    ProjectBlueprint createProjectBlueprint(String projectBlueprintName, int designerId,
                                            double projectBlueprintCost);

    /* READ */
    List<ProjectBlueprint> getAll();

    List<ProjectBlueprint> getAllByIdIn(Collection<Integer> projectBlueprintIdCollection);

    List<ProjectBlueprint> getAllByProjectBlueprintNameLike(String projectBlueprintName);

    List<ProjectBlueprint> getAllByProjectBlueprintCostBetween(double from, double to);

    ProjectBlueprint getByDesignerId(int designerId);

    ProjectBlueprint getById(int projectBlueprintId);

    /* UPDATE */
    boolean updateProjectBlueprint(ProjectBlueprintModel projectBlueprintModel);

    /* DELETE */
    boolean deleteProjectBlueprint(int projectBlueprintId);
}
