package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectCreateDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectReadDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectUpdateDTO;
import com.ntv.ntvcons_backend.entities.ProjectManager;
import com.ntv.ntvcons_backend.entities.ProjectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.UpdateProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectWorker;
import com.ntv.ntvcons_backend.entities.UserModels.ListUserIDAndName;
import com.ntv.ntvcons_backend.services.location.LocationService;
import com.ntv.ntvcons_backend.services.misc.FileCombineService;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
import com.ntv.ntvcons_backend.services.projectWorker.ProjectWorkerService;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ProjectManagerService projectManagerService;
    @Autowired
    private ProjectWorkerService projectWorkerService;
    @Autowired
    private FileCombineService fileCombineService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @Deprecated
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/createProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createProject(@RequestBody CreateProjectModel createProjectModel) {
        try {
            if(projectService.checkDuplicate(createProjectModel.getProjectName())) {
                return ResponseEntity.badRequest().body("Tên dự án đã tồn tại.");
            } else {
                if(!locationService.checkCoordinate(createProjectModel.getCoordinate())) {
                    boolean result = projectService.createProject(createProjectModel);

                    if (result) {
                        return ResponseEntity.ok().body("Tạo thành công.");
                    }
                    return ResponseEntity.badRequest().body("Tạo thất bại.");
                } else {
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
    public ResponseEntity<Object> createProjectAlt1(
            @RequestBody @Valid ProjectCreateDTO projectDTO,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            projectDTO.setCreatedBy(userId);

            List<Long> managerIdList = projectDTO.getManagerIdList();
            if (managerIdList == null) {
                projectDTO.setManagerIdList(
                        new ArrayList<>(Collections.singletonList(userId)));
            } else {
                if (!managerIdList.contains(userId)) {
                    managerIdList.add(userId);
                    projectDTO.setManagerIdList(managerIdList);
                }
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

    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/createProject/withFile",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createProjectWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    ProjectCreateDTO projectDTO,
            @RequestPart(required = false) @Size(min = 1) List<MultipartFile> projectDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            projectDTO.setCreatedBy(userId);

            List<Long> managerIdList = projectDTO.getManagerIdList();
            if (managerIdList == null) {
                projectDTO.setManagerIdList(
                        new ArrayList<>(Collections.singletonList(userId)));
            } else {
                if (!managerIdList.contains(userId)) {
                    managerIdList.add(userId);
                    projectDTO.setManagerIdList(managerIdList);
                }
            }

            ProjectReadDTO newProjectDTO = projectService.createProjectByDTO(projectDTO);

            long projectId = newProjectDTO.getProjectId();

            if (projectDocList != null) {
                fileCombineService.saveAllFileInDBAndFirebase(
                        projectDocList, FileType.PROJECT_DOC, projectId, EntityType.PROJECT_ENTITY, userId);

                /* Get again after file created & save */
                newProjectDTO = projectService.getDTOById(projectId);
            }

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

    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/addFile/{projectId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addFileToProjectById(
            @PathVariable long projectId,
            @RequestPart @Size(min = 1) List<MultipartFile> projectDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            ProjectReadDTO projectDTO = projectService.getDTOById(projectId);

            if (projectDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Project found with Id: '" + projectId + "' to add file.");

            fileCombineService.saveAllFileInDBAndFirebase(
                    projectDocList, FileType.PROJECT_DOC, projectId, EntityType.PROJECT_ENTITY, userId);

            /* Get again after file created & save */
            projectDTO = projectService.getDTOById(projectId);

            return ResponseEntity.ok().body(projectDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error adding file to Project with Id: '" + projectId + "'. ",
                            e.getMessage()));
        }
    }

    /* READ */
    @Deprecated
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

            Set<Long> projectIdSet;

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

                case BY_MANAGER_ID:
                    List<ProjectManager> projectManagerList =
                            projectManagerService.getAllByManagerId(Long.parseLong(searchParam));

                    if (projectManagerList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Project found with userId (managerId): '" + searchParam + "'. ");
                    }

                    projectIdSet =
                            projectManagerList.stream()
                                    .map(ProjectManager::getProjectId)
                                    .collect(Collectors.toSet());

                    projectDTOList = projectService.getAllDTOInPagingByIdIn(paging, projectIdSet);

                    if (projectDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Project found with userId (managerId): '" + searchParam + "'. ");
                    }
                    break;

                case BY_WORKER_ID:
                    List<ProjectWorker> projectWorkerList =
                            projectWorkerService.getAllByWorkerId(Long.parseLong(searchParam));

                    if (projectWorkerList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Project found with workerId: '" + searchParam + "'. ");
                    }

                    projectIdSet =
                            projectWorkerList.stream()
                                    .map(ProjectWorker::getProjectId)
                                    .collect(Collectors.toSet());

                    projectDTOList = projectService.getAllDTOInPagingByIdIn(paging, projectIdSet);

                    if (projectDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Project found with workerId: '" + searchParam + "'. ");
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
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy/searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
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

    @Deprecated
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
    @Deprecated
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
    public ResponseEntity<Object> updateProjectAlt1(
            @RequestBody @Valid ProjectUpdateDTO projectDTO,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            projectDTO.setUpdatedBy(userId);

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

    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1.1/updateProject/withFile",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProjectWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    ProjectUpdateDTO projectDTO,
            @RequestPart(required = false) @Size(min = 1) List<MultipartFile> projectDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            projectDTO.setUpdatedBy(userId);

            ProjectReadDTO updatedProjectDTO = projectService.updateProjectByDTO(projectDTO);

            if (updatedProjectDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Project found with Id: '" + projectDTO.getProjectId() + "'. ");
            }

            long projectId = updatedProjectDTO.getProjectId();

            if (projectDocList != null) {
                /* Deleted old project file */
                List<ExternalFileReadDTO> fileDTOList = updatedProjectDTO.getFileList();
                if (fileDTOList != null)
                    fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);

                fileCombineService.saveAllFileInDBAndFirebase(
                        projectDocList, FileType.PROJECT_DOC, projectId, EntityType.PROJECT_ENTITY, userId);

                /* Get again after file created & save */
                updatedProjectDTO = projectService.getDTOById(projectId);
            }

//            if (blueprintDoc != null) {
//                BlueprintReadDTO blueprintDTO = updatedProjectDTO.getBlueprint();
//
//                if (blueprintDTO == null)
//                    throw new IllegalArgumentException("Blueprint needed before adding blueprintDoc");
//
//                long blueprintId = blueprintDTO.getBlueprintId();
//
//                /* Deleted old blueprint file */
//                ExternalFileReadDTO fileDTO = blueprintDTO.getFile();
//                if (fileDTO != null)
//                    fileCombineService.deleteFileInDBAndFirebaseByFileDTO(fileDTO);
//
//                fileCombineService.saveFileInDBAndFirebase(
//                        blueprintDoc, FileType.BLUEPRINT_DOC, blueprintId, EntityType.BLUEPRINT_ENTITY, userId);
//
//                addedFile = true;
//            }

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

    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/replaceFile/{projectId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> replaceFileOfProjectById(
            @PathVariable long projectId,
            @RequestParam @Size(min = 1) List<Long> removeFileIdList,
            @RequestPart @Size(min = 1) List<MultipartFile> projectDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            ProjectReadDTO projectDTO = projectService.getDTOById(projectId);

            if (projectDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Project found with Id: '" + projectId + "' to replace file.");

            List<ExternalFileReadDTO> fileDTOList = projectDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Project with Id: '" + projectId + "' has no file to replace. "
                                + "Try using 'POST:../addFile/{projectId}' instead");
            } else {
                Set<Long> oldFileIdSet =
                        fileDTOList.stream()
                                .map(ExternalFileReadDTO::getFileId)
                                .collect(Collectors.toSet());

                StringBuilder errorMsg = new StringBuilder();
                for (Long removeFileId : removeFileIdList) {
                    if (!oldFileIdSet.contains(removeFileId)) {
                        errorMsg.append("Project with Id: '")
                                .append(projectId).append("' has no File with Id: '")
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
                    projectDocList, FileType.PROJECT_DOC, projectId, EntityType.PROJECT_ENTITY, userId);

            /* Get again after file created & save */
            projectDTO = projectService.getDTOById(projectId);

            return ResponseEntity.ok().body(projectDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error replacing file of Project with Id: '" + projectId + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/replaceAllFile/{projectId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> replaceAllFileOfProjectById(
            @PathVariable long projectId,
            @RequestPart @Size(min = 1) List<MultipartFile> projectDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            ProjectReadDTO projectDTO = projectService.getDTOById(projectId);

            if (projectDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Project found with Id: '" + projectId + "' to replace file.");

            List<ExternalFileReadDTO> fileDTOList = projectDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Project with Id: '" + projectId + "' has no file to replace. "
                                + "Try using 'POST:../addFile/{projectId}' instead");
            } else {
                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
            }

            fileCombineService.saveAllFileInDBAndFirebase(
                    projectDocList, FileType.PROJECT_DOC, projectId, EntityType.PROJECT_ENTITY, userId);

            /* Get again after file created & save */
            projectDTO = projectService.getDTOById(projectId);

            return ResponseEntity.ok().body(projectDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error replacing file of Project with Id: '" + projectId + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deleteProject/{projectId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteProject(@PathVariable int projectId) {
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

    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deleteFile/{projectId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteFileOfProjectById(
            @PathVariable long projectId,
            @RequestParam @Size(min = 1) List<Long> removeFileIdList) {
        try {
            ProjectReadDTO projectDTO = projectService.getDTOById(projectId);

            if (projectDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Project found with Id: '" + projectId + "' to delete file.");

            List<ExternalFileReadDTO> fileDTOList = projectDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Project with Id: '" + projectId + "' has no file to delete. ");
            } else {
                Set<Long> oldFileIdSet =
                        fileDTOList.stream()
                                .map(ExternalFileReadDTO::getFileId)
                                .collect(Collectors.toSet());

                StringBuilder errorMsg = new StringBuilder();
                for (Long removeFileId : removeFileIdList) {
                    if (!oldFileIdSet.contains(removeFileId)) {
                        errorMsg.append("Project with Id: '")
                                .append(projectId).append("' has no File with Id: '")
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
            projectDTO = projectService.getDTOById(projectId);

            return ResponseEntity.ok().body(projectDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error deleting file of Project with Id: '" + projectId + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deleteAllFile/{projectId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteAllFileOfProjectById(@PathVariable long projectId) {
        try {
            ProjectReadDTO projectDTO = projectService.getDTOById(projectId);

            if (projectDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Project found with Id: '" + projectId + "' to delete file.");

            List<ExternalFileReadDTO> fileDTOList = projectDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Project with Id: '" + projectId + "' has no file to delete. ");
            } else {
                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
            }

            /* Get again after file created & save */
            projectDTO = projectService.getDTOById(projectId);

            return ResponseEntity.ok().body(projectDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error deleting file of Project with Id: '" + projectId + "'. ",
                            e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
