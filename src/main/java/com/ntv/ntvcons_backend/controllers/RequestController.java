package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.request.RequestCreateDTO;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestModels.CreateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.ShowRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestVerifierModel;
import com.ntv.ntvcons_backend.repositories.ProjectRepository;
import com.ntv.ntvcons_backend.repositories.RequestTypeRepository;
import com.ntv.ntvcons_backend.services.request.RequestService;
import com.ntv.ntvcons_backend.services.requestDetail.RequestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
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
        // TODO:
        return null;
    }

    //@PreAuthorize("hasAnyRole('Engineer')")
    @PostMapping(value = "/v1/addRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addRequestDetail(@RequestBody CreateRequestDetailModel createRequestDetailModel){
                boolean result = requestDetailService.createRequest(createRequestDetailModel);
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
        // TODO:
        return null;
    }

    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.REQUEST searchType) {
        // TODO:
        return null;
    }

    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_REQUEST searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        // TODO:
        return null;
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



    @PutMapping(value = "/v1/updateVerifier", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateVerifier(@RequestBody UpdateRequestVerifierModel updateRequestVerifierModel) {
        boolean result = requestService.updateVerifier(updateRequestVerifierModel);

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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Request found with Id: '" + requestId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Request with Id: '" + requestId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Request with Id: '" + requestId + "'. ", e.getMessage()));
        }
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
}
