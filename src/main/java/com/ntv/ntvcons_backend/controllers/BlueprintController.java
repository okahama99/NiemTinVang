package com.ntv.ntvcons_backend.controllers;


import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.BlueprintModels.ShowBlueprintModel;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ProjectBlueprint")
public class BlueprintController {
    @Autowired
    BlueprintService blueprintService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody List<ShowBlueprintModel> getAll(@RequestBody int pageNo,
                                                         @RequestBody int pageSize,
                                                         @RequestBody String sortBy,
                                                         @RequestBody boolean sortType) {
        List<ShowBlueprintModel> projects = blueprintService.getAll(pageNo, pageSize, sortBy, sortType);
        return projects;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteBlueprint/{blueprintId}", produces = "application/json;charset=UTF-8")
    public HttpStatus deleteProjectBlueprint(@PathVariable(name = "blueprintId") int projectBlueprintId) {
        if (blueprintService.deleteProjectBlueprint(projectBlueprintId)) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}

