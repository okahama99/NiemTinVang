package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.task.TaskDTO;
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
    @PostMapping("/v1/create")
    public ResponseEntity<Object> createTask(@RequestBody TaskDTO taskDTO) {
        try {
            TaskDTO newTaskDTO = taskService.createTaskByDTO(taskDTO);

            return ResponseEntity.ok().body(newTaskDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating Task");
        }
    }

    /* READ */
    @GetMapping(value = "/v1/read/all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestBody int pageNo,
                                         @RequestBody int pageSize,
                                         @RequestBody String sortBy,
                                         @RequestBody boolean sortType) {
//        List<TaskDTO> taskDTOList = taskService.getAllDTO(pageNo, pageSize, sortBy, sortType);
//        return ResponseEntity.ok().body(taskDTOList);
        return null;
    }

    /* UPDATE */
    @PutMapping(value = "/v1/update/{taskId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateTask(@PathVariable(name = "taskId") long taskId,
                                             @RequestBody TaskDTO taskDTO) {
        if (taskId != taskDTO.getTaskId()) {
            return ResponseEntity.badRequest().body("Mismatch Id");
        }

        try {
            TaskDTO updatedTaskDTO = taskService.updateTaskByDTO(taskDTO);

            if (updatedTaskDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Task found with Id: " + taskId);
            }

            return ResponseEntity.ok().body(updatedTaskDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating Task with Id: " + taskId);
        }
    }

    /* DELETE */
    @DeleteMapping("/v1/delete/{taskId}")
    public ResponseEntity<Object> deleteTask(@PathVariable(name = "taskId") long taskId) {
        try {
            if (taskService.deleteTask(taskId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Task found with Id: " + taskId);
            }

            return ResponseEntity.ok().body("Deleted Task with Id: " + taskId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting Task with Id: " + taskId);
        }

    }
    /* ================================================ Ver 1 ================================================ */

}
