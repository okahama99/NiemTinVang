package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.project.ProjectCreateDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectReadDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectUpdateDTO;
import com.ntv.ntvcons_backend.entities.ProjectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.UpdateProjectModel;
import com.ntv.ntvcons_backend.entities.UserModels.ListUserIDAndName;
import com.ntv.ntvcons_backend.services.location.LocationService;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import com.ntv.ntvcons_backend.utils.JwtUtil;
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
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private MiscUtil miscUtil;
    @Autowired
    private JwtUtil jwtUtil;
    
    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/createProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createProject(@RequestBody CreateProjectModel createProjectModel){
        try {
            if(projectService.checkDuplicate(createProjectModel.getProjectName())) {
                return ResponseEntity.badRequest().body("Tên dự án đã tồn tại.");
            } else {
                if(!locationService.checkCoordinate(createProjectModel.getCoordinate()))
                {
                    boolean result = projectService.createProject(createProjectModel);

                    if (result) {
                        return ResponseEntity.ok().body("Tạo thành công.");
                    }
                    return ResponseEntity.badRequest().body("Tạo thất bại.");
                }else{
                    return ResponseEntity.badRequest().body("Coordinate đã tồn tại.");
                }
            }
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error creating Project", e.getMessage()));
        }
    }

    /** Alternate create project by Thanh, with check FK */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1.1/createProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createProjectAlt1(@Valid @RequestBody ProjectCreateDTO projectDTO,
                                                    @RequestHeader(name = "Authorization") String token) {
        try {
            /* TODO: jwtUtil get jwt auto */
            Long userId = jwtUtil.getUserIdFromJWT(token.substring(7));
            if (userId != null) {
                projectDTO.setCreatedBy(userId);
            }

            ProjectReadDTO newProjectDTO = projectService.createProjectByDTO(projectDTO);

            return ResponseEntity.ok().body(newProjectDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error creating Project", e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ProjectModel> projects = projectService.getAll(pageNo, pageSize, sortBy, sortTypeAsc);

            if (projects == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Project found");
            }

            return ResponseEntity.ok().body(projects);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Project", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1.1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllAlt1(@RequestParam int pageNo,
                                             @RequestParam int pageSize,
                                             @RequestParam String sortBy,
                                             @RequestParam boolean sortTypeAsc) {
        try {
            List<ProjectReadDTO> projects = 
                    projectService.getAllInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (projects == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Project found");
            }

            return ResponseEntity.ok().body(projects);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Project", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.PROJECT searchType) {
        try {
            ProjectReadDTO projectDTO;

            switch (searchType) {
                case BY_ID:
                    projectDTO = projectService.getDTOById(Long.parseLong(searchParam));

                    if (projectDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Project found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME:
                    projectDTO = projectService.getDTOByProjectName(searchParam);

                    if (projectDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Project found with name: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Project");
            }

            return ResponseEntity.ok().body(projectDTO);
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
            String errorMsg = "Error searching for Project with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;

                case BY_NAME:
                    errorMsg += "name: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_PROJECT searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<ProjectReadDTO> projectDTOList;

            switch (searchType) {
                case BY_LOCATION_ID:
                    projectDTOList = projectService.getAllDTOInPagingByLocationId(paging, Long.parseLong(searchParam));

                    if (projectDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Project found with designerName: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME_CONTAINS:
                    projectDTOList = projectService.getAllDTOInPagingByProjectNameContains(paging, searchParam);

                    if (projectDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Project found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Project");
            }

            return ResponseEntity.ok().body(projectDTOList);
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
            String errorMsg = "Error searching for Project with ";

            switch (searchType) {
                case BY_LOCATION_ID:
                    errorMsg += "locationId: '" + searchParam + "'. ";
                    break;

                case BY_NAME_CONTAINS:
                    errorMsg += "name contains: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1/getAllById", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ProjectModel> getAllById(@RequestParam long projectId,
                                  @RequestParam int pageNo,
                                  @RequestParam int pageSize,
                                  @RequestParam String sortBy,
                                  @RequestParam boolean sortTypeAsc) {
        List<ProjectModel> projects = projectService.getAllById(projectId, pageNo, pageSize, sortBy, sortTypeAsc);
        return projects;
    }

    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1/getUserForDropdown", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ListUserIDAndName> getUserForDropdown() {
        List<ListUserIDAndName> listUser = projectService.getUserForDropdownSelection();
        return listUser;
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updateProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProject(@RequestBody UpdateProjectModel updateProjectModel) {
        boolean result = projectService.updateProject(updateProjectModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    /** Alternate update project by Thanh, with check FK */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1.1/updateProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProjectAlt1(@Valid @RequestBody ProjectUpdateDTO projectDTO,
                                                    @RequestHeader(name = "Authorization") String token){
        try {
            /* TODO: jwtUtil get jwt auto */
            Long userId = jwtUtil.getUserIdFromJWT(token.substring(7));
            if (userId != null) {
                projectDTO.setUpdatedBy(userId);
            }

            ProjectReadDTO updatedProjectDTO = projectService.updateProjectByDTO(projectDTO);

            if (updatedProjectDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Project found with Id: '" + projectDTO.getProjectId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedProjectDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error Updating Project", e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deleteProject/{projectId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteProject(@PathVariable(name = "projectId") int projectId) {
        try {
            if (!projectService.deleteProject(projectId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Project found with Id: '" + projectId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Project with Id: '" + projectId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Project with Id: '" + projectId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
