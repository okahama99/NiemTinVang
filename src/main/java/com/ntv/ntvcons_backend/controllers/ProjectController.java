package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    
    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/v1/createProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createProject(/*@RequestBody ProjectCreateDTO projectDTO*/){
//        String result = projectService.createProject(projectName, createProjectModel,
//                createBluePrintModel, planStartDate, planEndDate,
//                actualStartDate, actualEndDate, estimateCost, actualCost);
//        return result;
//        TODO: create project
         return null;
    }

    /* READ */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestBody int pageNo, 
                                         @RequestBody int pageSize, 
                                         @RequestBody String sortBy, 
                                         @RequestBody boolean sortType) {
        try {
            List<ProjectModel> projects = projectService.getAll(pageNo, pageSize, sortBy, sortType);

            if (projects == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Project found");
            }

            return ResponseEntity.ok().body(projects);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Project", e.getMessage()));
        }
    }

    /* UPDATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProject(/*@RequestBody ProjectUpdateDTO projectDTO*/){
//        String result = projectService.updateProject(projectModel);
//        return result;
//        TODO: update project
        return null;
    }

    /* DELETE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/v1/deleteProject/{projectId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteProject(@PathVariable(name = "projectId") int projectId){
        try {
            if (!projectService.deleteProject(projectId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Project found with Id: " + projectId);
            }

            return ResponseEntity.ok().body("Deleted Project with Id: " + projectId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Project with Id: " + projectId, e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
