package com.ntv.ntvcons_backend.controllers;


import com.ntv.ntvcons_backend.entities.ProjectBlueprint;
import com.ntv.ntvcons_backend.entities.ProjectBlueprintModels.ShowProjectBlueprintModel;
import com.ntv.ntvcons_backend.services.projectBlueprint.ProjectBlueprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ProjectBlueprint")
public class ProjectBlueprintController {
    @Autowired
    ProjectBlueprintService projectBlueprintService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/createProjectBlueprint", produces = "application/json;charset=UTF-8")
    public HttpStatus createProjectBlueprint(@RequestBody String projectBlueprintName,
                                             @RequestBody int designerId,
                                             @RequestBody double projectBlueprintCost){

        ProjectBlueprint result = projectBlueprintService.createProjectBlueprint(projectBlueprintName, designerId, projectBlueprintCost);
        if(result!=null){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/updateProjectBlueprint", produces = "application/json;charset=UTF-8")
    public HttpStatus updateProjectBlueprint(@RequestBody ShowProjectBlueprintModel showProjectBlueprintModel){
        boolean result = projectBlueprintService.updateProjectBlueprint(showProjectBlueprintModel);
        if(result){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowProjectBlueprintModel> getAll(@RequestBody int pageNo,
                                  @RequestBody int pageSize,
                                  @RequestBody String sortBy,
                                  @RequestBody boolean sortType) {
        List<ShowProjectBlueprintModel> projects = projectBlueprintService.getAll(pageNo, pageSize, sortBy, sortType);
        return projects;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteProjectBlueprint/{projectBlueprintId}", produces = "application/json;charset=UTF-8")
    public HttpStatus deleteProjectBlueprint(@PathVariable(name = "projectBlueprintId") int projectBlueprintId){
        if(projectBlueprintService.deleteProjectBlueprint(projectBlueprintId))
        {
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }
}

