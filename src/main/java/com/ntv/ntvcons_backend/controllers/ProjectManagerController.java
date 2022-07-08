package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerCreateDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerUpdateDTO;
import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/projectManager")
public class ProjectManagerController {
    @Autowired
    private ProjectManagerService projectManagerService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PostMapping(value = "/v1/createProjectManager", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createProjectManager(@Valid @RequestBody ProjectManagerCreateDTO projectManagerDTO) {
        // TODO:
        return null;
    }

    /* READ */
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        // TODO:
        return null;
    }
    
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.PROJECT_MANAGER searchType) {
        // TODO:
        return null;
    }

    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_PROJECT_MANAGER searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        // TODO:
        return null;
    }
    
    /* UPDATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateProjectManager", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProjectManager(@Valid @RequestBody ProjectManagerUpdateDTO projectManagerDTO){
        try {

            ProjectManagerReadDTO updatedProjectManagerDTO =
                    projectManagerService.updateProjectManagerByDTO(projectManagerDTO);

            if (updatedProjectManagerDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ProjectManager found with Id: '" + projectManagerDTO.getProjectManagerId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedProjectManagerDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating ProjectManager with Id: '" + projectManagerDTO.getProjectManagerId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @DeleteMapping(value = "/v1/deleteProjectManager/{projectManagerId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteProjectManager(@PathVariable(name = "projectManagerId") long projectManagerId){
        // TODO:
        return null;
    }

    /* ================================================ Ver 1 ================================================ */
}
