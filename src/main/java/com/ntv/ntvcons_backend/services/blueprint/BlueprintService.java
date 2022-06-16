package com.ntv.ntvcons_backend.services.blueprint;

import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBluePrintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.ShowBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.UpdateBlueprintModel;

import java.util.Collection;
import java.util.List;

public interface BlueprintService {
    /* CREATE */
    void createProjectBlueprint(CreateBluePrintModel createBluePrintModel);

    /* READ */
    List<ShowBlueprintModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Blueprint> getAllByIdIn(Collection<Integer> projectBlueprintIdCollection);

    List<Blueprint> getAllByProjectBlueprintNameContains(String projectBlueprintName);

    List<Blueprint> getAllByProjectBlueprintCostBetween(double from, double to);

    Blueprint getByDesignerId(int designerId);

    Blueprint getById(int projectBlueprintId);

    /* UPDATE */
    void updateProjectBlueprint(UpdateBlueprintModel updateBlueprintModel);

    /* DELETE */
    boolean deleteProjectBlueprint(long projectBlueprintId);

    String checkDuplicate(String blueprintName);
}
