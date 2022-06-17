package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeUpdateDTO;
import com.ntv.ntvcons_backend.services.requestType.RequestTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requestType")
public class RequestTypeController {
    @Autowired
    private RequestTypeService requestTypeService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    //@PreAuthorize("hasRequestType('ROLE_ADMIN')")
    @PostMapping(value = "/v1/createRequestType", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRequestType(@RequestBody RequestTypeCreateDTO requestTypeDTO){
        try {
            RequestTypeReadDTO newRequestTypeDTO = requestTypeService.createRequestTypeByDTO(requestTypeDTO);

            return ResponseEntity.ok().body(newRequestTypeDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating RequestType", e.getMessage()));
        }
    }

    /* READ */
    //@PreAuthorize("hasRequestType('ROLE_ADMIN')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<RequestTypeReadDTO> requestTypeDTOList =
                    requestTypeService.getAllDTO(pageNo, pageSize, sortBy, sortType);

            if (requestTypeDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No RequestType found");
            }

            return ResponseEntity.ok().body(requestTypeDTOList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for RequestType", e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam(name = "searchType") SearchType searchType) {
        try {
            List<RequestTypeReadDTO> requestTypeDTOList;

            /* switch just in case expand later */
            switch (searchType) {
                case REQUEST_TYPE_BY_NAME_CONTAINS:
                    requestTypeDTOList = requestTypeService.getAllDTOByRequestTypeNameContains(searchParam);
                    if (requestTypeDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No RequestType found with name contains: " + searchParam);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity RequestType");
            }

            return ResponseEntity.ok().body(requestTypeDTOList);
        }  catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for RequestType with ";

            /* switch just in case expand later */
            switch (searchType) {
                case REQUEST_TYPE_BY_NAME_CONTAINS:
                    errorMsg += "name contains: " + searchParam;
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    //@PreAuthorize("hasRequestType('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateRequestType", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequestType(@RequestBody RequestTypeUpdateDTO requestTypeDTO){
        try {
            RequestTypeReadDTO updatedRequestTypeDTO = requestTypeService.updateRequestTypeByDTO(requestTypeDTO);

            if (updatedRequestTypeDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No RequestType found with Id: " + requestTypeDTO.getRequestTypeId());
            }

            return ResponseEntity.ok().body(updatedRequestTypeDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating RequestType with Id: " + requestTypeDTO.getRequestTypeId(),
                            e.getMessage()));
        }
    }

    /* DELETE */
    //@PreAuthorize("hasRequestType('ROLE_ADMIN')")
    @DeleteMapping(value = "/v1/deleteRequestType/{requestTypeId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteRequestType(@PathVariable(name = "requestTypeId") long requestTypeId){
        try {
            if (!requestTypeService.deleteRequestType(requestTypeId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No RequestType found with Id: " + requestTypeId);
            }

            return ResponseEntity.ok().body("Deleted RequestType with Id: " + requestTypeId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting RequestType with Id: " + requestTypeId, e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}