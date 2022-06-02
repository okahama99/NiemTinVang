package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.projectModels.ProjectModel;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/Project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/createProject", produces = "application/json;charset=UTF-8")
    public HttpStatus createProject(@RequestBody String projectName,
                                    @RequestBody int locationId,
                                    @RequestBody Timestamp startDate,
                                    @RequestBody Timestamp endDate,
                                    @RequestBody int blueprintId,
                                    @RequestBody Double estimateCost){

        Project result = projectService.createProject(projectName, locationId, startDate, endDate, blueprintId, estimateCost);
        if(result!=null){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/updateProject", produces = "application/json;charset=UTF-8")
    public HttpStatus updateProject(@RequestBody ProjectModel projectModel){
        boolean result = projectService.updateProject(projectModel);
        if(result){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<Project> getAll() {
        List<Project> projects = projectService.getAll();
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
