package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBluePrintModel;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/Project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/createProject", produces = "application/json;charset=UTF-8")
    public String createProject(@RequestBody String projectName,
                                    @RequestBody CreateLocationModel createLocationModel,
                                    @RequestBody CreateBluePrintModel createBluePrintModel,
                                    @RequestBody Instant planStartDate,
                                    @RequestBody Instant planEndDate,
                                    @RequestBody Instant actualStartDate,
                                    @RequestBody Instant actualEndDate,
                                    @RequestBody double estimateCost,
                                    @RequestBody double actualCost){

        String result = projectService.createProject(projectName, createLocationModel,
                createBluePrintModel, planStartDate, planEndDate,
                actualStartDate, actualEndDate, estimateCost, actualCost);
        return result;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/updateProject", produces = "application/json;charset=UTF-8")
    public String updateProject(@RequestBody ProjectModel projectModel){
        String result = projectService.updateProject(projectModel);
        return result;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ProjectModel> getAll(@RequestBody int pageNo,
                              @RequestBody int pageSize,
                              @RequestBody String sortBy,
                              @RequestBody boolean sortType) {
        List<ProjectModel> projects = projectService.getAll(pageNo, pageSize, sortBy, sortType);
        return projects;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteProject/{projectId}", produces = "application/json;charset=UTF-8")
    public HttpStatus deleteProject(@PathVariable(name = "projectId") int projectId){
        if(projectService.deleteProject(projectId))
        {
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }
}
