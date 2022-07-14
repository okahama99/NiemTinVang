package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
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
import com.ntv.ntvcons_backend.services.request.RequestService;
import com.ntv.ntvcons_backend.services.requestDetail.RequestDetailService;
import com.ntv.ntvcons_backend.utils.ThanhUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private RequestService requestService;
    @Autowired
    private ThanhUtil thanhUtil;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RequestTypeRepository requestTypeRepository;
    @Autowired
    private RequestDetailService requestDetailService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    //@PreAuthorize("hasAnyRole('Engineer')")
    @PostMapping(value = "/v1/createRequest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRequest(@RequestBody CreateRequestModel createRequestModel){
        if(!projectRepository.existsById(createRequestModel.getProjectId())){
            return ResponseEntity.ok().body("ProjectId không tồn tại.");
        }else{
            if(!requestTypeRepository.existsById(createRequestModel.getRequestTypeId())){
                return ResponseEntity.ok().body("RequestTypeId không tồn tại.");
            }else{
                boolean result = requestService.createRequest(createRequestModel);
                if (result) {
                    return ResponseEntity.ok().body("Tạo thành công.");
                }
                return ResponseEntity.badRequest().body("Tạo thất bại.");
            }
        }

    }
    
    @PostMapping(value = "/v1.1/createRequest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRequestAlt1(@Valid @RequestBody RequestCreateDTO requestDTO) {
        try {
            RequestReadDTO newRequestDTO = requestService.createRequestByDTO(requestDTO);

            return ResponseEntity.ok().body(newRequestDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (createdBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Request", e.getMessage()));
        }
    }

    //@PreAuthorize("hasAnyRole('Engineer')")
    @PostMapping(value = "/v1/addRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addRequestDetail(@RequestBody CreateRequestDetailModel createRequestDetailModel){
        boolean result = requestDetailService.createRequestDetail(createRequestDetailModel);
        if (result) {
            return ResponseEntity.ok().body("Tạo thành công.");
        }
        return ResponseEntity.badRequest().body("Tạo thất bại.");
    }

    /* READ */
    //@PreAuthorize("hasAnyRole('Engineer','Admin','Customer','Staff')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowRequestModel> requests = requestService.getAllAvailableRequest(pageNo, pageSize, sortBy, sortTypeAsc);

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

    @GetMapping(value = "/v1.1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllAlt1(@RequestParam int pageNo, 
                                             @RequestParam int pageSize, 
                                             @RequestParam String sortBy, 
                                             @RequestParam boolean sortTypeAsc) {
        try {
            List<RequestReadDTO> requestList =
                    requestService.getAllDTOInPaging(
                            thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

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

                case BY_NAME:
                    requestDTO = requestService.getDTOByRequestName(searchParam);

                    if (requestDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Request found with name: '" + searchParam + "'. ");
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

                case BY_NAME:
                    errorMsg += "name: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_REQUEST searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

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

                case BY_NAME_CONTAINS:
                    errorMsg += "name contains: '" + searchParam + "'. ";
                    break;

            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    //@PreAuthorize("hasAnyRole('Engineer','Admin','Staff','Customer')")
    @GetMapping(value = "/v1/getByProjectId", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByProjectId(@RequestParam Long projectId,
                                         @RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowRequestModel> requests = requestService.getByProjectId(projectId, pageNo, pageSize, sortBy, sortTypeAsc);

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

    //@PreAuthorize("hasAnyRole('Engineer','Admin','Staff','Customer')")
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
    //@PreAuthorize("hasAnyRole('Engineer')")
    @PutMapping(value = "/v1/updateRequest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequest(@RequestBody UpdateRequestModel updateRequestModel) {
        boolean result = requestService.updateRequest(updateRequestModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }
    
    @PutMapping(value = "/v1.1/updateRequest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequestAlt1(@RequestBody RequestUpdateDTO requestDTO) {
        try {
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

    @PutMapping(value = "/v1/updateVerifier", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateVerifier(@RequestBody UpdateRequestVerifierModel updateRequestVerifierModel) {
        boolean result = requestService.updateVerifier(updateRequestVerifierModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    //@PreAuthorize("hasAnyRole('Admin')")
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
    //@PreAuthorize("hasAnyRole('Engineer','Admin')")
    @DeleteMapping(value = "/v1/deleteRequest/{requestId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteRequest(@PathVariable(name = "requestId") Long requestId) {
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

}
