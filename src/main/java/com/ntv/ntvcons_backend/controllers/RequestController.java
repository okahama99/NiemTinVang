package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.RequestModels.CreateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.ShowRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestVerifierModel;
import com.ntv.ntvcons_backend.repositories.ProjectRepository;
import com.ntv.ntvcons_backend.repositories.RequestTypeRepository;
import com.ntv.ntvcons_backend.services.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Request")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RequestTypeRepository requestTypeRepository;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
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

    /* READ */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<ShowRequestModel> requests = requestService.getAllAvailableRequest(pageNo, pageSize, sortBy, sortType);

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

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/v1/getByProjectId", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByProjectId(@RequestParam Long projectId,
                                         @RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<ShowRequestModel> requests = requestService.getByProjectId(projectId, pageNo, pageSize, sortBy, sortType);

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

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
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
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateRequest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequest(@RequestBody UpdateRequestModel updateRequestModel) {
        boolean result = requestService.updateRequest(updateRequestModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateVerifier", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateVerifier(@RequestBody UpdateRequestVerifierModel updateRequestVerifierModel) {
        boolean result = requestService.updateVerifier(updateRequestVerifierModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    /* DELETE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
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

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
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
