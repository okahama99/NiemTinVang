package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.RequestDetail;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.ShowRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.UpdateRequestDetailModel;
import com.ntv.ntvcons_backend.services.requestDetail.RequestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/RequestDetail")
public class RequestDetailController {

    @Autowired
    RequestDetailService requestDetailService;

    /* CREATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/v1/createRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRequestDetail(@RequestBody CreateRequestDetailModel createRequestDetailModel){
        boolean result = requestDetailService.createRequest(createRequestDetailModel);
        if (result) {
            return ResponseEntity.ok().body("Tạo thành công.");
        }
        return ResponseEntity.badRequest().body("Tạo thất bại.");
    }

    /* READ */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/v1/getAllRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllRequestDetail(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<ShowRequestDetailModel> requests = requestDetailService.getAllAvailableRequestDetail(pageNo, pageSize, sortBy, sortType);

            if (requests == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No RequestDetail found");
            }

            return ResponseEntity.ok().body(requests);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for RequestDetail", e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/getAllById", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<RequestDetail> getAllById(@RequestParam Long requestId) {
        List<RequestDetail> requestDetail = requestDetailService.getRequestDetailByRequestId(requestId);
        return requestDetail;
    }

    /* UPDATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequestDetail(@RequestBody UpdateRequestDetailModel updateRequestDetailModel) {
        boolean result = requestDetailService.updateRequestDetail(updateRequestDetailModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    /* DELETE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/v1/deleteRequestDetail/{requestDetailId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteRequestDetail(@PathVariable(name = "requestDetailId") Long requestDetailId) {
        try {
            if (!requestDetailService.deleteRequestDetail(requestDetailId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No RequestDetail found with Id: " + requestDetailId);
            }

            return ResponseEntity.ok().body("Deleted RequestDetail with Id: " + requestDetailId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting RequestDetail with Id: " + requestDetailId, e.getMessage()));
        }
    }
}
