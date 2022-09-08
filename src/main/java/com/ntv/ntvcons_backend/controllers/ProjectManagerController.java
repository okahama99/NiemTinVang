package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerCreateDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerUpdateDTO;
import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
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
@RequestMapping("/projectManager")
public class ProjectManagerController {
    @Autowired
    private ProjectManagerService projectManagerService;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/createProjectManager", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createProjectManager(@RequestBody @Valid ProjectManagerCreateDTO projectManagerDTO) {
        try {
            ProjectManagerReadDTO newProjectManagerDTO =
                    projectManagerService.createProjectManagerByDTO(projectManagerDTO);

            return ResponseEntity.ok().body(newProjectManagerDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User by respective Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating ProjectManager. ",
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
            List<ProjectManagerReadDTO> projectManagerList =
                    projectManagerService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (projectManagerList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ProjectManager found");
            }

            return ResponseEntity.ok().body(projectManagerList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for ProjectManager", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.PROJECT_MANAGER searchType) {
        try {
            ProjectManagerReadDTO projectManagerDTO;

            switch (searchType) {
                case BY_ID:
                    projectManagerDTO = projectManagerService.getDTOById(Long.parseLong(searchParam));

                    if (projectManagerDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ProjectManager found with Id: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity ProjectManager");
            }

            return ResponseEntity.ok().body(projectManagerDTO);
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
            e.printStackTrace();
            String errorMsg = "Error searching for ProjectManager with ";

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
                                                @RequestParam SearchType.ALL_PROJECT_MANAGER searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<ProjectManagerReadDTO> projectManagerDTOList;

            switch (searchType) {
                case BY_PROJECT_ID:
                    projectManagerDTOList =
                            projectManagerService.getAllDTOInPagingByProjectId(paging, Long.parseLong(searchParam));

                    if (projectManagerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ProjectManager found with projectId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_MANAGER_ID:
                    projectManagerDTOList =
                            projectManagerService.getAllDTOInPagingByManagerId(paging, Long.parseLong(searchParam));

                    if (projectManagerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ProjectManager found with userId (managerId): '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity ProjectManager");
            }

            return ResponseEntity.ok().body(projectManagerDTOList);
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
            e.printStackTrace();
            String errorMsg = "Error searching for ProjectManager with ";

            switch (searchType) {
                case BY_PROJECT_ID:
                    errorMsg += "projectId: '" + searchParam + "'. ";
                    break;

                case BY_MANAGER_ID:
                    errorMsg += "userId (managerId): '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }
    
    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updateProjectManager", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProjectManager(@RequestBody @Valid ProjectManagerUpdateDTO projectManagerDTO) {
        try {
            ProjectManagerReadDTO updatedProjectManagerDTO =
                    projectManagerService.updateProjectManagerByDTO(projectManagerDTO);

            if (updatedProjectManagerDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ProjectManager found with Id: '" + projectManagerDTO.getProjectManagerId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedProjectManagerDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating ProjectManager with Id: '" + projectManagerDTO.getProjectManagerId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @DeleteMapping(value = "/v1/deleteProjectManager/{projectManagerId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteProjectManager(@PathVariable long projectManagerId) {
        try {
            if (!projectManagerService.deleteProjectManager(projectManagerId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ProjectManager found with Id: '" + projectManagerId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted ProjectManager with Id: '" + projectManagerId + "'. ");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting ProjectManager with Id: '" + projectManagerId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */
}
