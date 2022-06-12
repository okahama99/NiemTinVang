package com.ntv.ntvcons_backend.controllers;

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

    @GetMapping(value = "/v1/read", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParams(@RequestParam String taskName) {
        try {
            List<TaskReadDTO> taskDTOList = taskService.getAllDTOByTaskNameContains(taskName);

            if (taskDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Task found with name contains: " + taskName);
            }

            return ResponseEntity.ok().body(taskDTOList);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Task with name contains: " + taskName,
                            e.getMessage()));
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
