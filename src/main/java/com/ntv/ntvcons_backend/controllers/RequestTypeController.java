package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.requestType.*;
import com.ntv.ntvcons_backend.services.requestType.RequestTypeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping(value = "/v1/create", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> insertRequestType(@RequestBody RequestTypeCreateDTO requestTypeDTO){
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
    @GetMapping(value = "/v1/read/all", produces = "application/json;charset=UTF-8")
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

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for RequestType", e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/read", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParams(@RequestParam String requestTypeName) {
        try {
            List<RequestTypeReadDTO> requestTypeDTOList =
                    requestTypeService.getAllDTOByRequestTypeNameContains(requestTypeName);

            if (requestTypeDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No RequestType found with name contains: " + requestTypeName);
            }

            return ResponseEntity.ok().body(requestTypeDTOList);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for RequestType with name contains: " + requestTypeName,
                            e.getMessage()));
        }
    }

    /* UPDATE */
    //@PreAuthorize("hasRequestType('ROLE_ADMIN')")
    @PutMapping(value = "/v1/update", produces = "application/json;charset=UTF-8")
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
    @DeleteMapping(value = "/v1/delete/{requestTypeId}", produces = "application/json;charset=UTF-8")
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
