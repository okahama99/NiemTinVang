package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentUpdateDTO;
import com.ntv.ntvcons_backend.services.taskAssignment.TaskAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taskAssignment")
public class TaskAssignmentController {
    @Autowired
    private TaskAssignmentService taskAssignmentService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */

    /* READ */

    /* UPDATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateTaskAssignment", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProjectManager(@RequestBody TaskAssignmentUpdateDTO taskAssignmentDTO){
        try {

            TaskAssignmentReadDTO updatedTaskAssignmentDTO =
                    taskAssignmentService.updateTaskAssignmentByDTO(taskAssignmentDTO);

            if (updatedTaskAssignmentDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No TaskAssignment found with Id: " + taskAssignmentDTO.getAssignmentId());
            }

            return ResponseEntity.ok().body(updatedTaskAssignmentDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating TaskAssignment with Id: " + taskAssignmentDTO.getAssignmentId(),
                            e.getMessage()));
        }
    }

    /* DELETE */

    /* ================================================ Ver 1 ================================================ */
}
