package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerCreateDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerReadDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerUpdateDTO;
import com.ntv.ntvcons_backend.services.misc.FileCombineService;
import com.ntv.ntvcons_backend.services.worker.WorkerService;
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

import javax.activation.MimetypesFileTypeMap;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/worker")
public class WorkerController {
    @Autowired
    private WorkerService workerService;
    @Autowired
    private MiscUtil miscUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private FileCombineService fileCombineService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/createWorker", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createWorker(
            @RequestBody @Valid WorkerCreateDTO workerDTO,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            workerDTO.setCreatedBy(userId);

            WorkerReadDTO newWorkerDTO = workerService.createWorkerByDTO(workerDTO);

            return ResponseEntity.ok().body(newWorkerDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Worker", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/createWorker/withFile",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createWorkerWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    WorkerCreateDTO workerDTO,
            @RequestPart(required = false) MultipartFile workerAvatar,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            workerDTO.setCreatedBy(userId);

            /* Create to get Id */
            WorkerReadDTO newWorkerDTO =
                    workerService.createWorkerByDTO(workerDTO);

            long workerId = newWorkerDTO.getWorkerId();

            if (workerAvatar != null) {
                String fileName = workerAvatar.getOriginalFilename();
                if (fileName == null || fileName.isEmpty())
                    throw new IllegalArgumentException(
                            "Invalid file name, could not assert file type from the file name given. ");

                String extension = miscUtil.getExtension(fileName);
                String type = new MimetypesFileTypeMap().getContentType(fileName)
                        .split("/")[0];
                if (extension.isEmpty() || !type.equals("image")) {
                    throw new IllegalArgumentException("Invalid file type for worker avatar. ");
                }

                fileCombineService.saveFileInDBAndFirebase(
                        workerAvatar, FileType.WORKER_AVATAR, workerId, EntityType.WORKER_ENTITY, userId);

                /* Get again after file created & save */
                newWorkerDTO = workerService.getDTOById(workerId);
            }

            return ResponseEntity.ok().body(newWorkerDTO);
        } catch (IllegalArgumentException iAE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Worker", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/addFile/{workerId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addFileToWorkerById(
            @PathVariable long workerId,
            @RequestPart MultipartFile workerAvatar,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            WorkerReadDTO workerDTO =
                    workerService.getDTOById(workerId);

            if (workerDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Worker found with Id: '" + workerId + "' to add file.");

            String fileName = workerAvatar.getOriginalFilename();
            if (fileName == null || fileName.isEmpty())
                throw new IllegalArgumentException(
                        "Invalid file name, could not assert file type from the file name given. ");

            String extension = miscUtil.getExtension(fileName);
            String type = new MimetypesFileTypeMap().getContentType(fileName)
                    .split("/")[0];
            if (extension.isEmpty() || !type.equals("image")) {
                throw new IllegalArgumentException("Invalid file type for worker avatar. ");
            }

            fileCombineService.saveFileInDBAndFirebase(
                    workerAvatar, FileType.WORKER_AVATAR, workerId, EntityType.WORKER_ENTITY, userId);

            /* Get again after file created & save */
            workerDTO = workerService.getDTOById(workerId);

            return ResponseEntity.ok().body(workerDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error adding file to Worker with Id: '" + workerId + "'. ",
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
            List<WorkerReadDTO> workerDTOList =
                    workerService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (workerDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Worker found");
            }

            return ResponseEntity.ok().body(workerDTOList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Worker", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.WORKER searchType) {
        try {
            WorkerReadDTO workerDTO;

            switch (searchType) {
                case BY_ID:
                    workerDTO = workerService.getDTOById(Long.parseLong(searchParam));

                    if (workerDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_CITIZEN_ID:
                    workerDTO = workerService.getDTOByCitizenId(searchParam);

                    if (workerDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with citizenId: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Worker");
            }

            return ResponseEntity.ok().body(workerDTO);
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
            String errorMsg = "Error searching for Worker with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;

                case BY_CITIZEN_ID:
                    errorMsg += "citizenId: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_WORKER searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<WorkerReadDTO> workerDTOList;

            switch (searchType) {
                case BY_FULL_NAME:
                    workerDTOList =
                            workerService.getAllDTOInPagingByFullName(paging, searchParam);

                    if (workerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with fullName : '" + searchParam + "'. ");
                    }
                    break;

                case BY_FULL_NAME_CONTAINS:
                    workerDTOList =
                            workerService.getAllDTOInPagingByFullNameContains(paging, searchParam);

                    if (workerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with fullName contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_CITIZEN_ID_CONTAINS:
                    workerDTOList =
                            workerService.getAllDTOInPagingByCitizenIdContains(paging, searchParam);

                    if (workerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with citizenId contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_ADDRESS_ID:
                    workerDTOList =
                            workerService.getAllDTOInPagingByAddressId(paging, Long.parseLong(searchParam));

                    if (workerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with locationId (addressId): '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Worker");
            }

            return ResponseEntity.ok().body(workerDTOList);
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
            String errorMsg = "Error searching for Worker with ";

            switch (searchType) {
                case BY_FULL_NAME:
                    errorMsg += "fullName: '" + searchParam + "'. ";
                    break;

                case BY_FULL_NAME_CONTAINS:
                    errorMsg += "fullName contains: '" + searchParam + "'. ";
                    break;

                case BY_CITIZEN_ID_CONTAINS:
                    errorMsg += "citizenId contains: '" + searchParam + "'. ";
                    break;

                case BY_ADDRESS_ID:
                    errorMsg += "locationId (addressId): '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updateWorker", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateWorker(@RequestBody @Valid WorkerUpdateDTO workerDTO) {
        try {
            WorkerReadDTO updatedWorkerDTO = workerService.updateWorkerByDTO(workerDTO);

            if (updatedWorkerDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Worker found with Id: '" + workerDTO.getWorkerId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedWorkerDTO);
        } catch (IllegalArgumentException iAE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Worker with Id: '" + workerDTO.getWorkerId() + "'. ",
                            e.getMessage()));
        }
    }
    
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updateWorker/withFile",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateWorkerWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    WorkerUpdateDTO workerDTO,
            @RequestPart(required = false) MultipartFile workerAvatar,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            workerDTO.setUpdatedBy(userId);

            WorkerReadDTO updatedWorkerDTO = workerService.updateWorkerByDTO(workerDTO);

            if (updatedWorkerDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Worker found with Id: '" + workerDTO.getWorkerId() + "'. ");

            long workerId = updatedWorkerDTO.getWorkerId();

            if (workerAvatar != null) {
                String fileName = workerAvatar.getOriginalFilename();
                if (fileName == null || fileName.isEmpty())
                    throw new IllegalArgumentException(
                            "Invalid file name, could not assert file type from the file name given. ");

                String extension = miscUtil.getExtension(fileName);
                String type = new MimetypesFileTypeMap().getContentType(fileName)
                        .split("/")[0];
                if (extension.isEmpty() || !type.equals("image")) {
                    throw new IllegalArgumentException("Invalid file type for worker avatar. ");
                }

                fileCombineService.saveFileInDBAndFirebase(
                        workerAvatar, FileType.WORKER_AVATAR, workerId, EntityType.WORKER_ENTITY, userId);

                /* Get again after file created & save */
                updatedWorkerDTO = workerService.getDTOById(workerId);
            }

            return ResponseEntity.ok().body(updatedWorkerDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Worker with Id: '" + workerDTO.getWorkerId() + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/replaceFile/{workerId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> replaceFileOfWorkerById(
            @PathVariable long workerId,
            @RequestPart MultipartFile workerAvatar,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            WorkerReadDTO workerDTO =
                    workerService.getDTOById(workerId);

            if (workerDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Worker found with Id: '" + workerId + "' to replace file.");

            ExternalFileReadDTO fileDTO = workerDTO.getFile();
            if (fileDTO == null) {
                return ResponseEntity.badRequest()
                        .body("Worker with Id: '" + workerId + "' has no file to replace. "
                                + "Try using 'POST:../addFile/{workerId}' instead");
            } else {
                fileCombineService.deleteFileInDBAndFirebaseByFileDTO(fileDTO);
            }

            String fileName = workerAvatar.getOriginalFilename();
            if (fileName == null || fileName.isEmpty())
                throw new IllegalArgumentException(
                        "Invalid file name, could not assert file type from the file name given. ");

            String extension = miscUtil.getExtension(fileName);
            String type = new MimetypesFileTypeMap().getContentType(fileName)
                    .split("/")[0];
            if (extension.isEmpty() || !type.equals("image")) {
                throw new IllegalArgumentException("Invalid file type for worker avatar. ");
            }

            fileCombineService.saveFileInDBAndFirebase(
                    workerAvatar, FileType.WORKER_AVATAR, workerId, EntityType.WORKER_ENTITY, userId);

            /* Get again after file created & save */
            workerDTO = workerService.getDTOById(workerId);

            return ResponseEntity.ok().body(workerDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error replacing file of Worker with Id: '" + workerId + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @DeleteMapping(value = "/v1/deleteWorker/{workerId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteWorker(@PathVariable long workerId) {
        try {
            if (!workerService.deleteWorker(workerId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Worker found with Id: '" + workerId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Worker with Id: '" + workerId + "'. ");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Worker with Id: '" + workerId + "'. ", e.getMessage()));
        }
    }
    
    @PreAuthorize("hasAnyAuthority('54','24')")
    @DeleteMapping(value = "/v1/deleteFile/{workerId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteFileOfWorkerById(@PathVariable long workerId) {
        try {
            /* Get by Id */
            WorkerReadDTO workerDTO =
                    workerService.getDTOById(workerId);

            if (workerDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Worker found with Id: '" + workerId + "' to replace file.");

            /* Delete old file */
            ExternalFileReadDTO fileDTO = workerDTO.getFile();
            if (fileDTO != null) {
                fileCombineService.deleteFileInDBAndFirebaseByFileDTO(fileDTO);
            } else {
                return ResponseEntity.badRequest()
                        .body("Worker with Id: '" + workerId + "' has no file to delete. ");
            }

            /* Get again after file deleted */
            workerDTO = workerService.getDTOById(workerId);

            return ResponseEntity.ok().body(workerDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/Worker by respective Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting file of Worker with Id: '" + workerId + "'. ",
                            e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */
}
