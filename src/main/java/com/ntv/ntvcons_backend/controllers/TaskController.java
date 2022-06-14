package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import com.ntv.ntvcons_backend.dtos.task.*;
import com.ntv.ntvcons_backend.services.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PostMapping(value = "/v1/create", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createTask(@RequestBody TaskCreateDTO taskDTO) {
        try {
            TaskReadDTO newTaskDTO = taskService.createTaskByDTO(taskDTO);

            return ResponseEntity.ok().body(newTaskDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Task", e.getMessage()));
        }
    }

    /* READ */
    @GetMapping(value = "/v1/read/all", produces = "application/json;charset=UTF-8")
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
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Task", e.getMessage()));
        }
    }

    /* TODO: search by date (plan, actual); separate func or mod this one */
    @GetMapping(value = "/v1/read", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParams(@RequestParam String searchParam,
                                              /*@RequestBody(required = false) Object searchParams,*/
                                              @RequestParam("searchType") SearchType searchType) {
        try {
            List<TaskReadDTO> taskDTOList;

            switch (searchType) {
                case TASK_BY_NAME_CONTAINS:
                    taskDTOList = taskService.getAllDTOByTaskNameContains(searchParam);

                    if (taskDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Task found with name contains: " + searchParam);
                    }
                    break;

                case TASK_BY_PROJECT_ID:
                    taskDTOList = taskService.getAllDTOByProjectId(Long.parseLong(searchParam));

                    if (taskDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Task found with projectId: " + searchParam);
                    }
                    break;

                default:
                    return ResponseEntity.badRequest().body("Wrong search type for entity Task");
            }

            return ResponseEntity.ok().body(taskDTOList);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid param type for searchType: " + searchType
                                    + "\nExpecting param of type: Long",
                            nFE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "";

            switch (searchType) {
                case FILE_TYPE_BY_NAME_CONTAINS:
                    errorMsg += "Error searching for Task with name contains: " + searchParam;
                    break;

                case FILE_TYPE_BY_EXTENSION_CONTAINS:
                    errorMsg += "Error searching for Task with projectId: " + searchParam;
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PutMapping(value = "/v1/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateTask(@RequestBody TaskUpdateDTO taskDTO) {
        try {
            TaskReadDTO updatedTaskDTO = taskService.updateTaskByDTO(taskDTO);

            if (updatedTaskDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Task found with Id: " + taskDTO.getTaskId());
            }

            return ResponseEntity.ok().body(updatedTaskDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Task with Id: " + taskDTO.getTaskId(), e.getMessage()));
        }
    }

    /* DELETE */
    @DeleteMapping(value ="/v1/delete/{taskId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteTask(@PathVariable(name = "taskId") long taskId) {
        try {
            if (!taskService.deleteTask(taskId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Task found with Id: " + taskId);
            }

            return ResponseEntity.ok().body("Deleted Task with Id: " + taskId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Task with Id: " + taskId, e.getMessage()));
        }

    }
    /* ================================================ Ver 1 ================================================ */

}
