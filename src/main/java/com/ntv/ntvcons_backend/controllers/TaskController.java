package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskCreateDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskUpdateDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskCreateDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskUpdateDTO;
import com.ntv.ntvcons_backend.services.misc.FileCombineService;
import com.ntv.ntvcons_backend.services.task.TaskService;
import com.ntv.ntvcons_backend.utils.JwtUtil;
import com.ntv.ntvcons_backend.utils.MiscUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private MiscUtil miscUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private FileCombineService fileCombineService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54')")
    @PostMapping(value = "/v1/createTask", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createTask(@RequestBody @Valid TaskCreateDTO taskDTO,
                                             @RequestHeader(name = "Authorization") String token) {
        try {
            /* TODO: jwtUtil get jwt auto */
            Long userId = jwtUtil.getUserIdFromJWT(token.substring(7));
            if (userId != null) {
                taskDTO.setCreatedBy(userId);
            }

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

    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1/createTask/withFile", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createTaskWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    TaskCreateDTO taskDTO,
            @RequestPart(required = false) @Size(min = 1) List<MultipartFile> taskDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            /* TODO: jwtUtil get jwt auto */
            Long userId = jwtUtil.getUserIdFromJWT(token.substring(7));
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            taskDTO.setCreatedBy(userId);

            TaskReadDTO newTaskDTO = taskService.createTaskByDTO(taskDTO);

            long taskId = newTaskDTO.getTaskId();

            fileCombineService.saveAllFileInDBAndFirebase(
                    taskDocList, FileType.TASK_DOC, taskId, EntityType.TASK_ENTITY, userId);

            /* Get again after file created & save */
            newTaskDTO = taskService.getDTOById(taskId);

            return ResponseEntity.ok().body(newTaskDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User/TaskType by respective Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Task", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1/addFile/{taskId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addFileToTaskById(
            @PathVariable long taskId,
            @RequestPart @Size(min = 1) List<MultipartFile> taskDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            /* TODO: jwtUtil get jwt auto */
            Long userId = jwtUtil.getUserIdFromJWT(token.substring(7));
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            TaskReadDTO taskDTO = taskService.getDTOById(taskId);

            if (taskDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Task found with Id: '" + taskId + "' to add file.");

            fileCombineService.saveAllFileInDBAndFirebase(
                    taskDocList, FileType.TASK_DOC, taskId, EntityType.TASK_ENTITY, userId);

            /* Get again after file created & save */
            taskDTO = taskService.getDTOById(taskId);

            return ResponseEntity.ok().body(taskDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error adding file to Task with Id: '" + taskId + "'. ",
                            e.getMessage()));
        }
    }
    
    /* READ */
    @PreAuthorize("hasAnyAuthority('54','14','24','44')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<TaskReadDTO> taskDTOList = 
                    taskService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

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

    @PreAuthorize("hasAnyAuthority('54','14','24','44')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.TASK searchType) {
        try {
            TaskReadDTO taskDTO;

            switch (searchType) {
                case BY_ID:
                    taskDTO = taskService.getDTOById(Long.parseLong(searchParam));

                    if (taskDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Task found with Id: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Task");
            }

            return ResponseEntity.ok().body(taskDTO);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for Task with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','14','24','44')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_TASK searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<TaskReadDTO> taskDTOList;

            switch (searchType) {
                case BY_NAME:
                    taskDTOList =
                            taskService.getAllDTOInPagingByTaskName(paging, searchParam);

                    if (taskDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Task found with name: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME_CONTAINS:
                    taskDTOList =
                            taskService.getAllDTOInPagingByTaskNameContains(paging, searchParam);

                    if (taskDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Task found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_PROJECT_ID:
                    taskDTOList =
                            taskService.getAllDTOInPagingByProjectId(paging, Long.parseLong(searchParam));

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
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy/searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for Task with ";

            switch (searchType) {
                case BY_NAME:
                    errorMsg += "name: '" + searchParam + "'. ";
                    break;

                case BY_NAME_CONTAINS:
                    errorMsg += "name contains: '" + searchParam + "'. ";
                    break;

                case BY_PROJECT_ID:
                    errorMsg += "projectId: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54')")
    @PutMapping(value = "/v1/updateTask", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateTask(
            @RequestBody @Valid TaskUpdateDTO taskDTO,
            @RequestHeader(name = "Authorization") String token) {
        try {
            /* TODO: jwtUtil get jwt auto */
            /* TODO: jwtUtil get jwt auto */
            Long userId = jwtUtil.getUserIdFromJWT(token.substring(7));
            if (userId != null) {
                taskDTO.setUpdatedBy(userId);
            }

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


    @PreAuthorize("hasAnyAuthority('54')")
    @PutMapping(value = "/v1/updateTask/withFile", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateTaskWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    TaskUpdateDTO taskDTO,
            @RequestPart(required = false) @Size(min = 1) List<MultipartFile> taskDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            /* TODO: jwtUtil get jwt auto */
            Long userId = jwtUtil.getUserIdFromJWT(token.substring(7));
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            taskDTO.setUpdatedBy(userId);

            TaskReadDTO updatedTaskDTO = taskService.updateTaskByDTO(taskDTO);

            if (updatedTaskDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Task found with Id: '" + taskDTO.getTaskId() + "'. ");

            long taskId = updatedTaskDTO.getTaskId();

            /* Deleted old task file */
            List<ExternalFileReadDTO> fileDTOList = updatedTaskDTO.getFileList();
            if (fileDTOList != null)
                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);

            fileCombineService.saveAllFileInDBAndFirebase(
                    taskDocList, FileType.TASK_DOC, taskId, EntityType.TASK_ENTITY, userId);

            /* Get again after file created & save */
            updatedTaskDTO = taskService.getDTOById(taskId);

            return ResponseEntity.ok().body(updatedTaskDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User/TaskType by respective Id (if changed), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Task with Id: '" + taskDTO.getTaskId() + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54')")
    @PutMapping(value = "/v1/replaceFile/{taskId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> replaceFileOfTaskById(
            @PathVariable long taskId,
            @RequestParam @Size(min = 1) List<Long> removeFileIdList,
            @RequestPart @Size(min = 1) List<MultipartFile> taskDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            /* TODO: jwtUtil get jwt auto */
            Long userId = jwtUtil.getUserIdFromJWT(token.substring(7));
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            TaskReadDTO taskDTO = taskService.getDTOById(taskId);

            if (taskDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Task found with Id: '" + taskId + "' to add file.");

            List<ExternalFileReadDTO> fileDTOList = taskDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Task with Id: '" + taskId + "' has no file to replace. "
                                + "Try using 'POST:../addFile/{taskId}' instead");
            } else {
                Set<Long> oldFileIdSet =
                        fileDTOList.stream()
                                .map(ExternalFileReadDTO::getFileId)
                                .collect(Collectors.toSet());

                StringBuilder errorMsg = new StringBuilder();
                for (Long removeFileId : removeFileIdList) {
                    if (!oldFileIdSet.contains(removeFileId)) {
                        errorMsg.append("Task with Id: '")
                                .append(taskId).append("' has no File with Id: '")
                                .append(removeFileId).append("' to remove. ");
                    }
                }

                if (!errorMsg.toString().trim().isEmpty())
                    throw new IllegalArgumentException(errorMsg.toString());

                List<ExternalFileReadDTO> removeFileDTOList = new ArrayList<>();

                for (ExternalFileReadDTO fileDTO : fileDTOList) {
                    if (removeFileIdList.contains(fileDTO.getFileId())) {
                        removeFileDTOList.add(fileDTO);
                    }
                }

                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(removeFileDTOList);
            }

            fileCombineService.saveAllFileInDBAndFirebase(
                    taskDocList, FileType.TASK_DOC, taskId, EntityType.TASK_ENTITY, userId);

            /* Get again after file created & save */
            taskDTO = taskService.getDTOById(taskId);

            return ResponseEntity.ok().body(taskDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error replacing file of Task with Id: '" + taskId + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54')")
    @PutMapping(value = "/v1/replaceAllFile/{taskId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> replaceAllFileOfTaskById(
            @PathVariable long taskId,
            @RequestPart @Size(min = 1) List<MultipartFile> taskDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            /* TODO: jwtUtil get jwt auto */
            Long userId = jwtUtil.getUserIdFromJWT(token.substring(7));
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            TaskReadDTO taskDTO = taskService.getDTOById(taskId);

            if (taskDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Task found with Id: '" + taskId + "' to add file.");

            List<ExternalFileReadDTO> fileDTOList = taskDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Task with Id: '" + taskId + "' has no file to replace. "
                                + "Try using 'POST:../addFile/{taskId}' instead");
            } else {
                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
            }

            fileCombineService.saveAllFileInDBAndFirebase(
                    taskDocList, FileType.TASK_DOC, taskId, EntityType.TASK_ENTITY, userId);

            /* Get again after file created & save */
            taskDTO = taskService.getDTOById(taskId);

            return ResponseEntity.ok().body(taskDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error replacing file of Task with Id: '" + taskId + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value ="/v1/deleteTask/{taskId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteTask(@PathVariable long taskId) {
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

    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deleteFile/{taskId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteFileOfTaskById(
            @PathVariable long taskId,
            @RequestParam @Size(min = 1) List<Long> removeFileIdList) {
        try {
            TaskReadDTO taskDTO = taskService.getDTOById(taskId);

            if (taskDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Task found with Id: '" + taskId + "' to add file.");

            List<ExternalFileReadDTO> fileDTOList = taskDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Task with Id: '" + taskId + "' has no file to delete. ");
            } else {
                Set<Long> oldFileIdSet =
                        fileDTOList.stream()
                                .map(ExternalFileReadDTO::getFileId)
                                .collect(Collectors.toSet());

                StringBuilder errorMsg = new StringBuilder();
                for (Long removeFileId : removeFileIdList) {
                    if (!oldFileIdSet.contains(removeFileId)) {
                        errorMsg.append("Task with Id: '")
                                .append(taskId).append("' has no File with Id: '")
                                .append(removeFileId).append("' to remove. ");
                    }
                }

                if (!errorMsg.toString().trim().isEmpty())
                    throw new IllegalArgumentException(errorMsg.toString());

                List<ExternalFileReadDTO> removeFileDTOList = new ArrayList<>();

                for (ExternalFileReadDTO fileDTO : fileDTOList) {
                    if (removeFileIdList.contains(fileDTO.getFileId())) {
                        removeFileDTOList.add(fileDTO);
                    }
                }

                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(removeFileDTOList);
            }

            /* Get again after file delete & save */
            taskDTO = taskService.getDTOById(taskId);

            return ResponseEntity.ok().body(taskDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error deleting file of Task with Id: '" + taskId + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deleteAllFile/{taskId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteAllFileOfTaskById(@PathVariable long taskId) {
        try {
            TaskReadDTO taskDTO = taskService.getDTOById(taskId);

            if (taskDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Task found with Id: '" + taskId + "' to add file.");

            List<ExternalFileReadDTO> fileDTOList = taskDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Task with Id: '" + taskId + "' has no file to delete. ");
            } else {
                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
            }

            /* Get again after file created & save */
            taskDTO = taskService.getDTOById(taskId);

            return ResponseEntity.ok().body(taskDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error deleting file of Task with Id: '" + taskId + "'. ",
                            e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}