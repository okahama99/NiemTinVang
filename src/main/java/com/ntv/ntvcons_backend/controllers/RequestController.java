package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.request.RequestCreateDTO;
import com.ntv.ntvcons_backend.dtos.request.RequestReadDTO;
import com.ntv.ntvcons_backend.dtos.request.RequestUpdateDTO;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestModels.CreateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.ShowRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestVerifierModel;
import com.ntv.ntvcons_backend.repositories.ProjectRepository;
import com.ntv.ntvcons_backend.repositories.RequestTypeRepository;
import com.ntv.ntvcons_backend.services.misc.FileCombineService;
import com.ntv.ntvcons_backend.services.request.RequestService;
import com.ntv.ntvcons_backend.services.requestDetail.RequestDetailService;
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
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private RequestService requestService;
    @Autowired
    private MiscUtil miscUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RequestTypeRepository requestTypeRepository;
    @Autowired
    private RequestDetailService requestDetailService;
    @Autowired
    private FileCombineService fileCombineService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1/createRequest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRequest(@RequestBody CreateRequestModel createRequestModel) {
        if(!projectRepository.existsById(createRequestModel.getProjectId())) {
            return ResponseEntity.ok().body("ProjectId không tồn tại.");
        } else {
            if(!requestTypeRepository.existsById(createRequestModel.getRequestTypeId())) {
                return ResponseEntity.ok().body("RequestTypeId không tồn tại.");
            } else {
                boolean result = requestService.createRequest(createRequestModel);
                if (result) {
                    return ResponseEntity.ok().body("Tạo thành công.");
                }
                return ResponseEntity.badRequest().body("Tạo thất bại.");
            }
        }

    }

    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1.1/createRequest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRequestAlt1(
            @RequestBody @Valid RequestCreateDTO requestDTO,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            requestDTO.setCreatedBy(userId);

            RequestReadDTO newRequestDTO = requestService.createRequestByDTO(requestDTO);

            return ResponseEntity.ok().body(newRequestDTO);
        } catch (IllegalArgumentException iAE) {
            iAE.printStackTrace();

            /* Catch not found User by Id (createdBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Request", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1/createRequest/withFile",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRequestWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    RequestCreateDTO requestDTO,
            @RequestPart(required = false) @Size(min = 1) List<MultipartFile> requestDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            requestDTO.setCreatedBy(userId);

            RequestReadDTO newRequestDTO = requestService.createRequestByDTO(requestDTO);

            if (requestDocList != null) {
                long requestId = newRequestDTO.getRequestId();

                fileCombineService.saveAllFileInDBAndFirebase(
                        requestDocList, FileType.REQUEST_DOC, requestId, EntityType.REQUEST_ENTITY, userId);

                /* Get again after file created & save */
                newRequestDTO = requestService.getDTOById(requestId);
            }

            return ResponseEntity.ok().body(newRequestDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User/RequestType by respective Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Request", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1/addFile/{requestId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addFileToRequestById(
            @PathVariable long requestId,
            @RequestPart @Size(min = 1) List<MultipartFile> requestDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            RequestReadDTO requestDTO = requestService.getDTOById(requestId);

            if (requestDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Request found with Id: '" + requestId + "' to add file.");

            fileCombineService.saveAllFileInDBAndFirebase(
                    requestDocList, FileType.REQUEST_DOC, requestId, EntityType.REQUEST_ENTITY, userId);

            /* Get again after file created & save */
            requestDTO = requestService.getDTOById(requestId);

            return ResponseEntity.ok().body(requestDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error adding file to Request with Id: '" + requestId + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1/addRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addRequestDetail(@RequestBody CreateRequestDetailModel createRequestDetailModel) {
        boolean result = requestDetailService.createRequestDetail(createRequestDetailModel);
        if (result) {
            return ResponseEntity.ok().body("Tạo thành công.");
        }
        return ResponseEntity.badRequest().body("Tạo thất bại.");
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('44','54','24','14')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowRequestModel> requests =
                    requestService.getAllAvailableRequest(pageNo, pageSize, sortBy, sortTypeAsc);

            if (requests == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Request found");
            }

            return ResponseEntity.ok().body(requests);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Request", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54','24','14')")
    @GetMapping(value = "/v1.1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllAlt1(@RequestParam int pageNo, 
                                             @RequestParam int pageSize, 
                                             @RequestParam String sortBy, 
                                             @RequestParam boolean sortTypeAsc) {
        try {
            List<RequestReadDTO> requestList =
                    requestService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (requestList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Request found");
            }

            return ResponseEntity.ok().body(requestList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Request", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54','24','14')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.REQUEST searchType) {
        try {
            RequestReadDTO requestDTO;

            switch (searchType) {
                case BY_ID:
                    requestDTO = requestService.getDTOById(Long.parseLong(searchParam));

                    if (requestDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Request found with Id: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Request");
            }

            return ResponseEntity.ok().body(requestDTO);
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
            String errorMsg = "Error searching for Request with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54','24','14')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_REQUEST searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<RequestReadDTO> requestDTOList;

            switch (searchType) {
                case BY_PROJECT_ID:
                    requestDTOList =
                            requestService.getAllDTOInPagingByProjectId(paging, Long.parseLong(searchParam));

                    if (requestDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Request found with projectId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_REQUEST_TYPE_ID:
                    requestDTOList =
                            requestService.getAllDTOInPagingByRequestTypeId(paging, Long.parseLong(searchParam));

                    if (requestDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Request found with requestTypeId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_REQUESTER_ID:
                    requestDTOList =
                            requestService.getAllDTOInPagingByRequesterId(paging, Long.parseLong(searchParam));

                    if (requestDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Request found with userId (requesterId): '" + searchParam + "'. ");
                    }
                    break;

                case BY_VERIFIER_ID:
                    requestDTOList =
                            requestService.getAllDTOInPagingByVerifierId(paging, Long.parseLong(searchParam));

                    if (requestDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Request found with userId (verifierId): '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME:
                    requestDTOList = requestService.getAllDTOInPagingByRequestName(paging, searchParam);

                    if (requestDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Request found with name: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME_CONTAINS:
                    requestDTOList =
                            requestService.getAllDTOInPagingByRequestNameContains(paging, searchParam);

                    if (requestDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Request found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_REQUEST_DATE:
                    throw new IllegalArgumentException("SearchType not yet supported");

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Request");
            }

            return ResponseEntity.ok().body(requestDTOList);
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
            String errorMsg = "Error searching for Request with ";

            switch (searchType) {
                case BY_PROJECT_ID:
                    errorMsg += "projectId: '" + searchParam + "'. ";
                    break;

                case BY_REQUEST_TYPE_ID:
                    errorMsg += "requestTypeId: '" + searchParam + "'. ";
                    break;

                case BY_REQUESTER_ID:
                    errorMsg += "userId (requesterId): '" + searchParam + "'. ";
                    break;

                case BY_VERIFIER_ID:
                    errorMsg += "userId (verifierId): '" + searchParam + "'. ";
                    break;

                case BY_NAME:
                    errorMsg += "name: '" + searchParam + "'. ";
                    break;

                case BY_NAME_CONTAINS:
                    errorMsg += "name contains: '" + searchParam + "'. ";
                    break;

            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54','14','24')")
    @GetMapping(value = "/v1/getByProjectId", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByProjectId(@RequestParam Long projectId,
                                         @RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowRequestModel> requests =
                    requestService.getByProjectId(projectId, pageNo, pageSize, sortBy, sortTypeAsc);

            if (requests == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Request found");
            }

            return ResponseEntity.ok().body(requests);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Request", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54','14','24')")
    @GetMapping(value = "/v1/getByRequestId", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByRequestId(@RequestParam Long requestId) {
        try {
            ShowRequestModel requests = requestService.getByRequestId(requestId);

            if (requests == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Request found");
            }

            return ResponseEntity.ok().body(requests);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Request", e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1/updateRequest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequest(@RequestBody UpdateRequestModel updateRequestModel) {
        boolean result = requestService.updateRequest(updateRequestModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1.1/updateRequest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequestAlt1(
            @RequestBody RequestUpdateDTO requestDTO,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            requestDTO.setUpdatedBy(userId);

            RequestReadDTO updatedRequestDTO = requestService.updateRequestByDTO(requestDTO);

            if (updatedRequestDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Request found with Id: '" + requestDTO.getRequestId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedRequestDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (updatedBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Request with Id: '" + requestDTO.getRequestId() + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1/updateRequest/withFile",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequestWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    RequestUpdateDTO requestDTO,
            @RequestPart(required = false) @Size(min = 1) List<MultipartFile> requestDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            requestDTO.setUpdatedBy(userId);

            RequestReadDTO updatedRequestDTO = requestService.updateRequestByDTO(requestDTO);

            if (updatedRequestDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Request found with Id: '" + requestDTO.getRequestId() + "'. ");

            if (requestDocList != null) {
                long requestId = updatedRequestDTO.getRequestId();

                /* Deleted old request file */
                List<ExternalFileReadDTO> fileDTOList = updatedRequestDTO.getFileList();
                if (fileDTOList != null)
                    fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);

                fileCombineService.saveAllFileInDBAndFirebase(
                        requestDocList, FileType.REQUEST_DOC, requestId, EntityType.REQUEST_ENTITY, userId);

                /* Get again after file created & save */
                updatedRequestDTO = requestService.getDTOById(requestId);
            }

            return ResponseEntity.ok().body(updatedRequestDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User/RequestType by respective Id (if changed), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Request with Id: '" + requestDTO.getRequestId() + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1/replaceFile/{requestId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> replaceFileOfRequestById(
            @PathVariable long requestId,
            @RequestParam @Size(min = 1) List<Long> removeFileIdList,
            @RequestPart @Size(min = 1) List<MultipartFile> requestDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            RequestReadDTO requestDTO = requestService.getDTOById(requestId);

            if (requestDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Request found with Id: '" + requestId + "' to replace file.");

            List<ExternalFileReadDTO> fileDTOList = requestDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Request with Id: '" + requestId + "' has no file to replace. "
                                + "Try using 'POST:../addFile/{requestId}' instead");
            } else {
                Set<Long> oldFileIdSet =
                        fileDTOList.stream()
                                .map(ExternalFileReadDTO::getFileId)
                                .collect(Collectors.toSet());

                StringBuilder errorMsg = new StringBuilder();
                for (Long removeFileId : removeFileIdList) {
                    if (!oldFileIdSet.contains(removeFileId)) {
                        errorMsg.append("Request with Id: '")
                                .append(requestId).append("' has no File with Id: '")
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
                    requestDocList, FileType.REQUEST_DOC, requestId, EntityType.REQUEST_ENTITY, userId);

            /* Get again after file created & save */
            requestDTO = requestService.getDTOById(requestId);

            return ResponseEntity.ok().body(requestDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error replacing file of Request with Id: '" + requestId + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1/replaceAllFile/{requestId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> replaceAllFileOfRequestById(
            @PathVariable long requestId,
            @RequestPart @Size(min = 1) List<MultipartFile> requestDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            RequestReadDTO requestDTO = requestService.getDTOById(requestId);

            if (requestDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Request found with Id: '" + requestId + "' to replace file.");

            List<ExternalFileReadDTO> fileDTOList = requestDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Request with Id: '" + requestId + "' has no file to replace. "
                                + "Try using 'POST:../addFile/{requestId}' instead");
            } else {
                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
            }

            fileCombineService.saveAllFileInDBAndFirebase(
                    requestDocList, FileType.REQUEST_DOC, requestId, EntityType.REQUEST_ENTITY, userId);

            /* Get again after file created & save */
            requestDTO = requestService.getDTOById(requestId);

            return ResponseEntity.ok().body(requestDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error replacing file of Request with Id: '" + requestId + "'. ",
                            e.getMessage()));
        }
    }
    
    @PreAuthorize("hasAnyAuthority('44','4','34','14','24')")
    @PutMapping(value = "/v1/updateVerifier", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateVerifier(@RequestBody UpdateRequestVerifierModel updateRequestVerifierModel) {
        boolean result = requestService.updateVerifier(updateRequestVerifierModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    @PreAuthorize("hasAnyAuthority('54')")
    @PutMapping(value = "/v1/approveRequest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> approveRequest(@RequestParam Long requestId,
                                                 @RequestParam Boolean decision) {
        boolean result = requestService.approveUpdate(requestId,decision);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }
    
    /* DELETE */
    @PreAuthorize("hasAnyAuthority('44','54')")
    @DeleteMapping(value = "/v1/deleteRequest/{requestId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteRequest(@PathVariable Long requestId) {
        try {
            if (!requestService.deleteRequest(requestId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Request found with Id: '" + requestId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Request with Id: '" + requestId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Request with Id: '" + requestId + "'. ", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54')")
    @DeleteMapping(value = "/v1/deleteFile/{requestId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteFileOfRequestById(
            @PathVariable long requestId,
            @RequestParam @Size(min = 1) List<Long> removeFileIdList) {
        try {
            RequestReadDTO requestDTO = requestService.getDTOById(requestId);

            if (requestDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Request found with Id: '" + requestId + "' to delete file.");

            List<ExternalFileReadDTO> fileDTOList = requestDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Request with Id: '" + requestId + "' has no file to delete. ");
            } else {
                Set<Long> oldFileIdSet =
                        fileDTOList.stream()
                                .map(ExternalFileReadDTO::getFileId)
                                .collect(Collectors.toSet());

                StringBuilder errorMsg = new StringBuilder();
                for (Long removeFileId : removeFileIdList) {
                    if (!oldFileIdSet.contains(removeFileId)) {
                        errorMsg.append("Request with Id: '")
                                .append(requestId).append("' has no File with Id: '")
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
            requestDTO = requestService.getDTOById(requestId);

            return ResponseEntity.ok().body(requestDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error deleting file of Request with Id: '" + requestId + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54')")
    @DeleteMapping(value = "/v1/deleteAllFile/{requestId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteAllFileOfRequestById(@PathVariable long requestId) {
        try {
            RequestReadDTO requestDTO = requestService.getDTOById(requestId);

            if (requestDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Request found with Id: '" + requestId + "' to delete file.");

            List<ExternalFileReadDTO> fileDTOList = requestDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Request with Id: '" + requestId + "' has no file to delete. ");
            } else {
                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
            }

            /* Get again after file created & save */
            requestDTO = requestService.getDTOById(requestId);

            return ResponseEntity.ok().body(requestDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error deleting file of Request with Id: '" + requestId + "'. ",
                            e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
