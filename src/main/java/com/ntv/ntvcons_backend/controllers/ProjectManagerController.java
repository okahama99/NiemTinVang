package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerUpdateDTO;
import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projectManager")
public class ProjectManagerController {
    @Autowired
    private ProjectManagerService projectManagerService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */

    /* READ */

    /* UPDATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateProjectManager", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProjectManager(@RequestBody ProjectManagerUpdateDTO projectManagerDTO){
        try {

            ProjectManagerReadDTO updatedProjectManagerDTO =
                    projectManagerService.updateProjectManagerByDTO(projectManagerDTO);

            if (updatedProjectManagerDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ProjectManager found with Id: " + projectManagerDTO.getProjectManagerId());
            }

            return ResponseEntity.ok().body(updatedProjectManagerDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating ProjectManager with Id: " + projectManagerDTO.getProjectManagerId(),
                            e.getMessage()));
        }
    }

    /* DELETE */

    /* ================================================ Ver 1 ================================================ */
}
