package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerCreateDTO;
import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerUpdateDTO;
import com.ntv.ntvcons_backend.services.projectWorker.ProjectWorkerService;
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
@RequestMapping("/projectWorker")
public class ProjectWorkerController {
    @Autowired
    private ProjectWorkerService projectWorkerService;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/createProjectWorker", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createProjectWorker(@RequestBody @Valid ProjectWorkerCreateDTO projectWorkerDTO) {
        try {
            ProjectWorkerReadDTO newProjectWorkerDTO =
                    projectWorkerService.createProjectWorkerByDTO(projectWorkerDTO);

            return ResponseEntity.ok().body(newProjectWorkerDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/Worker by respective Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating ProjectWorker. ",
                            e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ProjectWorkerReadDTO> projectWorkerList =
                    projectWorkerService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (projectWorkerList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ProjectWorker found");
            }

            return ResponseEntity.ok().body(projectWorkerList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for ProjectWorker", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.PROJECT_WORKER searchType) {
        try {
            ProjectWorkerReadDTO projectWorkerDTO;

            switch (searchType) {
                case BY_ID:
                    projectWorkerDTO = projectWorkerService.getDTOById(Long.parseLong(searchParam));

                    if (projectWorkerDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ProjectWorker found with Id: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity ProjectWorker");
            }

            return ResponseEntity.ok().body(projectWorkerDTO);
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
            String errorMsg = "Error searching for ProjectWorker with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_PROJECT_WORKER searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<ProjectWorkerReadDTO> projectWorkerDTOList;

            switch (searchType) {
                case BY_PROJECT_ID:
                    projectWorkerDTOList =
                            projectWorkerService.getAllDTOInPagingByProjectId(paging, Long.parseLong(searchParam));

                    if (projectWorkerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ProjectWorker found with projectId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_WORKER_ID:
                    projectWorkerDTOList =
                            projectWorkerService.getAllDTOInPagingByWorkerId(paging, Long.parseLong(searchParam));

                    if (projectWorkerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ProjectWorker found with userId (managerId): '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity ProjectWorker");
            }

            return ResponseEntity.ok().body(projectWorkerDTOList);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        }  catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy/searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for ProjectWorker with ";

            switch (searchType) {
                case BY_PROJECT_ID:
                    errorMsg += "projectId: '" + searchParam + "'. ";
                    break;

                case BY_WORKER_ID:
                    errorMsg += "workerId: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }
    
    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updateProjectWorker", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProjectWorker(@RequestBody @Valid ProjectWorkerUpdateDTO projectWorkerDTO){
        try {
            ProjectWorkerReadDTO updatedProjectWorkerDTO =
                    projectWorkerService.updateProjectWorkerByDTO(projectWorkerDTO);

            if (updatedProjectWorkerDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ProjectWorker found with Id: '" + projectWorkerDTO.getProjectWorkerId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedProjectWorkerDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating ProjectWorker with Id: '" + projectWorkerDTO.getProjectWorkerId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @DeleteMapping(value = "/v1/deleteProjectWorker/{projectWorkerId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteProjectWorker(@PathVariable long projectWorkerId){
        try {
            if (!projectWorkerService.deleteProjectWorker(projectWorkerId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ProjectWorker found with Id: '" + projectWorkerId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted ProjectWorker with Id: '" + projectWorkerId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting ProjectWorker with Id: '" + projectWorkerId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */
}
