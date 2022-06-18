package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.entities.UserModels.ListUserIDAndName;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    
    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/v1/createProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createProject(@RequestBody CreateProjectModel createProjectModel){
        if(projectService.checkDuplicate(createProjectModel.getProjectName())) {
            return ResponseEntity.badRequest().body("Tên dự án đã tồn tại.");
        } else {
            boolean result = projectService.createProject(createProjectModel);

            if (result) {
                return ResponseEntity.ok().body("Tạo thành công.");
            }
            return ResponseEntity.badRequest().body("Tạo thất bại.");
        }
    }

    /* READ */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
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

    @GetMapping(value = "/v1/getAllById", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ProjectModel> getAllById(@RequestParam long projectId,
                                  @RequestParam int pageNo,
                                  @RequestParam int pageSize,
                                  @RequestParam String sortBy,
                                  @RequestParam boolean sortType) {
        List<ProjectModel> projects = projectService.getAllById(projectId, pageNo, pageSize, sortBy, sortType);
        return projects;
    }

    /* UPDATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProject(@RequestBody ProjectModel projectModel) {
        boolean result = projectService.updateProject(projectModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    /* DELETE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/v1/deleteProject/{projectId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteProject(@PathVariable(name = "projectId") int projectId) {
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

    @GetMapping(value = "/v1/getUserForDropdown", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ListUserIDAndName> getUserForDropdown() {
        List<ListUserIDAndName> listUser = projectService.getUserForDropdownSelection();
        return listUser;
    }
    /* ================================================ Ver 1 ================================================ */

}
