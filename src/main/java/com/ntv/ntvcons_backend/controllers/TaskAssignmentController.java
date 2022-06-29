package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentUpdateDTO;
import com.ntv.ntvcons_backend.services.taskAssignment.TaskAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/taskAssignment")
public class TaskAssignmentController {
    @Autowired
    private TaskAssignmentService taskAssignmentService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PostMapping(value = "/v1/createTaskAssignment", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createTaskAssignment(@Valid @RequestBody TaskAssignmentCreateDTO taskAssignmentDTO){
        try {
            TaskAssignmentReadDTO newTaskAssignmentDTO =
                    taskAssignmentService.createTaskAssignmentByDTO(taskAssignmentDTO);

            return ResponseEntity.ok().body(newTaskAssignmentDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Task/User by respective Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating TaskAssignment", e.getMessage()));
        }
    }

    /* READ */
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<TaskAssignmentReadDTO> taskAssignmentDTOList =
                    taskAssignmentService.getAllDTOInPaging(pageNo, pageSize, sortBy, sortType);

            if (taskAssignmentDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No TaskAssignment found");
            }

            return ResponseEntity.ok().body(taskAssignmentDTOList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for TaskAssignment", e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                             @RequestParam(name = "searchType") SearchType searchType) {
        try {
            List<TaskAssignmentReadDTO> taskAssignmentDTOList;

            switch (searchType) {
                case TASK_ASSIGNMENT_BY_ID:
                    TaskAssignmentReadDTO taskAssignmentDTO =
                            taskAssignmentService.getDTOById(Long.parseLong(searchParam));

                    if (taskAssignmentDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No TaskAssignment found with assignmentId: '" + searchParam + "'. ");
                    }

                    taskAssignmentDTOList = new ArrayList<>(Collections.singletonList(taskAssignmentDTO));
                    break;

                case TASK_ASSIGNMENT_BY_ASSIGNER_ID:
                    taskAssignmentDTOList =
                            taskAssignmentService.getAllDTOByAssignerId(Long.parseLong(searchParam));

                    if (taskAssignmentDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No TaskAssignment found with assignerId: '" + searchParam + "'. ");
                    }
                    break;

                case TASK_ASSIGNMENT_BY_ASSIGNEE_ID:
                    taskAssignmentDTOList =
                            taskAssignmentService.getAllDTOByAssignerId(Long.parseLong(searchParam));

                    if (taskAssignmentDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No TaskAssignment found with assigneeId: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity TaskAssignment");
            }

            return ResponseEntity.ok().body(taskAssignmentDTOList);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for TaskAssignment with ";

            switch (searchType) {
                case TASK_ASSIGNMENT_BY_ID:
                    errorMsg += "assignmentId: '" + searchParam + "'. ";
                    break;

                case TASK_ASSIGNMENT_BY_ASSIGNER_ID:
                    errorMsg += "assignerId: '" + searchParam + "'. ";
                    break;

                case TASK_ASSIGNMENT_BY_ASSIGNEE_ID:
                    errorMsg += "assigneeId: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PutMapping(value = "/v1/updateTaskAssignment", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateTaskAssignment(@Valid @RequestBody TaskAssignmentUpdateDTO taskAssignmentDTO){
        try {
            TaskAssignmentReadDTO updatedTaskAssignmentDTO =
                    taskAssignmentService.updateTaskAssignmentByDTO(taskAssignmentDTO);

            if (updatedTaskAssignmentDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No TaskAssignment found with Id: '" + taskAssignmentDTO.getAssignmentId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedTaskAssignmentDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating TaskAssignment with Id: '" + taskAssignmentDTO.getAssignmentId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @DeleteMapping(value = "/v1/deleteTaskAssignment/{assignmentId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteTaskAssignment(@PathVariable(name = "assignmentId") long assignmentId){
        try {
            if (!taskAssignmentService.deleteTaskAssignment(assignmentId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No TaskAssignment found with Id: '" + assignmentId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted TaskAssignment with Id: '" + assignmentId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating TaskAssignment with Id: '" + assignmentId + "'. ",
                            e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */
}