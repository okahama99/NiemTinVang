package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.task.TaskCreateDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskUpdateDTO;
import com.ntv.ntvcons_backend.services.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyRole('Admin')")
    @PostMapping(value = "/v1/createTask", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createTask(@RequestBody TaskCreateDTO taskDTO) {
        try {
            TaskReadDTO newTaskDTO = taskService.createTaskByDTO(taskDTO);

            return ResponseEntity.ok().body(newTaskDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project by Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Task", e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyRole('Admin','Customer','Staff','Engineer')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<TaskReadDTO> taskDTOList = taskService.getAllDTO(pageNo, pageSize, sortBy, sortType);

            if (taskDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Task found");
            }

            return ResponseEntity.ok().body(taskDTOList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Task", e.getMessage()));
        }
    }

    /* TODO"'. " search by date (plan, actual); separate func or mod this one */
    @PreAuthorize("hasAnyRole('Admin','Customer','Staff','Engineer')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                             @RequestParam(name = "searchType") SearchType searchType) {
        try {
            List<TaskReadDTO> taskDTOList;

            switch (searchType) {
                case TASK_BY_ID:
                    TaskReadDTO taskDTO = taskService.getDTOById(Long.parseLong(searchParam));

                    if (taskDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Task found with taskId: '" + searchParam + "'. ");
                    }

                    taskDTOList = new ArrayList<>(Collections.singletonList(taskDTO));
                    break;

                case TASK_BY_NAME_CONTAINS:
                    taskDTOList = taskService.getAllDTOByTaskNameContains(searchParam);

                    if (taskDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Task found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                case TASK_BY_PROJECT_ID:
                    taskDTOList = taskService.getAllDTOByProjectId(Long.parseLong(searchParam));

                    if (taskDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Task found with projectId: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Task");
            }

            return ResponseEntity.ok().body(taskDTOList);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } catch (IllegalArgumentException iAE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for Task with ";

            switch (searchType) {
                case TASK_BY_ID:
                    errorMsg += "taskId: '" + searchParam + "'. ";
                    break;

                case TASK_BY_NAME_CONTAINS:
                    errorMsg += "name contains: '" + searchParam + "'. ";
                    break;

                case TASK_BY_PROJECT_ID:
                    errorMsg += "projectId: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyRole('Admin')")
    @PutMapping(value = "/v1/updateTask", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateTask(@RequestBody TaskUpdateDTO taskDTO) {
        try {
            TaskReadDTO updatedTaskDTO = taskService.updateTaskByDTO(taskDTO);

            if (updatedTaskDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Task found with Id: '" + taskDTO.getTaskId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedTaskDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project by Id (if changed), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Task with Id: '" + taskDTO.getTaskId() + "'. ", e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyRole('Admin')")
    @DeleteMapping(value ="/v1/deleteTask/{taskId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteTask(@PathVariable(name = "taskId") long taskId) {
        try {
            if (!taskService.deleteTask(taskId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Task found with Id: '" + taskId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Task with Id: '" + taskId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Task with Id: '" + taskId + "'. ", e.getMessage()));
        }

    }
    /* ================================================ Ver 1 ================================================ */

}