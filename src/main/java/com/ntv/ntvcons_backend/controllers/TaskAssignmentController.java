package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentUpdateDTO;
import com.ntv.ntvcons_backend.services.taskAssignment.TaskAssignmentService;
import com.ntv.ntvcons_backend.utils.MiscUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/taskAssignment")
public class TaskAssignmentController {
    @Autowired
    private TaskAssignmentService taskAssignmentService;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54')")
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
    @PreAuthorize("hasAnyAuthority('54','14','24','44')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<TaskAssignmentReadDTO> taskAssignmentDTOList =
                    taskAssignmentService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

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

    @PreAuthorize("hasAnyAuthority('54','14','24','44')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.TASK_ASSIGNMENT searchType) {
        try {
            TaskAssignmentReadDTO taskAssignmentDTO;

            switch (searchType) {
                case BY_ID:
                    taskAssignmentDTO = taskAssignmentService.getDTOById(Long.parseLong(searchParam));

                    if (taskAssignmentDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No TaskAssignment found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_TASK_ID:
                    taskAssignmentDTO = taskAssignmentService.getDTOByTaskId(Long.parseLong(searchParam));

                    if (taskAssignmentDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No TaskAssignment found with name: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity TaskAssignment");
            }

            return ResponseEntity.ok().body(taskAssignmentDTO);
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
            String errorMsg = "Error searching for TaskAssignment with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;

                case BY_TASK_ID:
                    errorMsg += "taskId: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','14','24','44')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_TASK_ASSIGNMENT searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<TaskAssignmentReadDTO> taskAssignmentDTOList;

            switch (searchType) {
                case BY_ASSIGNER_ID:
                    taskAssignmentDTOList =
                            taskAssignmentService.getAllDTOInPagingByAssignerId(paging, Long.parseLong(searchParam));

                    if (taskAssignmentDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No TaskAssignment found with assignerId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_ASSIGNEE_ID:
                    taskAssignmentDTOList =
                            taskAssignmentService.getAllDTOInPagingByAssignerId(paging, Long.parseLong(searchParam));

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
                case BY_ASSIGNER_ID:
                    errorMsg += "assignerId: '" + searchParam + "'. ";
                    break;

                case BY_ASSIGNEE_ID:
                    errorMsg += "assigneeId: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54')")
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
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (updatedBy / assignerId / assigneeId), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating TaskAssignment with Id: '" + taskAssignmentDTO.getAssignmentId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54')")
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
