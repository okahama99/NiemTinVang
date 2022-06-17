package com.ntv.ntvcons_backend.services.blueprint;

import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.ShowBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.UpdateBlueprintModel;

import java.util.Collection;
import java.util.List;

public interface BlueprintService { /* TODO: throws Exception for controller to handle */
    /* CREATE */
    void createProjectBlueprint(CreateBlueprintModel createBluePrintModel);

    /* READ */
    List<ShowBlueprintModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    Blueprint getById(long blueprintId);

    List<Blueprint> getAllByIdIn(Collection<Integer> projectBlueprintIdCollection);

    List<Blueprint> getAllByBlueprintNameContains(String projectBlueprintName);

    List<Blueprint> getAllByBlueprintCostBetween(double from, double to);

    String checkDuplicate(String blueprintName);

    /* UPDATE */
    void updateBlueprint(UpdateBlueprintModel updateBlueprintModel);

    /* DELETE */

    boolean deleteBlueprint(long blueprintId);
}
