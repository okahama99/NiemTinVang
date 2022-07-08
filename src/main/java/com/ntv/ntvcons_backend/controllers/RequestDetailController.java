package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailUpdateDTO;
import com.ntv.ntvcons_backend.entities.RequestDetail;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.ShowRequestDetailModel;
import com.ntv.ntvcons_backend.services.requestDetail.RequestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/RequestDetail")
public class RequestDetailController {
    @Autowired
    private RequestDetailService requestDetailService;

    /* ================================================ Ver 1 ================================================ */
    /*CREATE*/
    @PostMapping(value = "/v1/createRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRequestDetail(@Valid @RequestBody RequestDetailCreateDTO requestDetailDTO) {
        // TODO:
        return null;
    }

    /* READ */
    @PreAuthorize("hasAnyRole('Engineer','Admin','Customer','Staff')")
    @GetMapping(value = "/v1/getAllRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllRequestDetail(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowRequestDetailModel> requests = requestDetailService.getAllAvailableRequestDetail(pageNo, pageSize, sortBy, sortTypeAsc);

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

    @PreAuthorize("hasAnyRole('Engineer','Admin','Customer','Staff')")
    @GetMapping(value = "/v1/getAllById", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<RequestDetail> getAllById(@RequestParam Long requestId) {
        List<RequestDetail> requestDetail = requestDetailService.getRequestDetailByRequestId(requestId);
        return requestDetail;
    }

    @GetMapping(value = "/v1.1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        // TODO:
        return null;
    }

    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.REQUEST_DETAIL searchType) {
        // TODO:
        return null;
    }

    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_REQUEST_DETAIL searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        // TODO:
        return null;
    }

    /* UPDATE */
    @PutMapping(value = "/v1/updateRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequestDetail(@Valid @RequestBody RequestDetailUpdateDTO requestDetailDTO) {
        // TODO:
        return null;
    }

    /* DELETE */
    @PreAuthorize("hasAnyRole('Engineer','Admin')")
    @DeleteMapping(value = "/v1/deleteRequestDetail/{requestDetailId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteRequestDetail(@PathVariable(name = "requestDetailId") long requestDetailId) {
        try {
            if (!requestDetailService.deleteRequestDetail(requestDetailId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No RequestDetail found with Id: '" + requestDetailId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted RequestDetail with Id: '" + requestDetailId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting RequestDetail with Id: '" + requestDetailId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */
}
