package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeUpdateDTO;
import com.ntv.ntvcons_backend.services.requestType.RequestTypeService;
import com.ntv.ntvcons_backend.utils.MiscUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/requestType")
public class RequestTypeController {
    @Autowired
    private RequestTypeService requestTypeService;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54')")
    @PostMapping(value = "/v1/createRequestType", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRequestType(@RequestBody @Valid RequestTypeCreateDTO requestTypeDTO){
        try {
            RequestTypeReadDTO newRequestTypeDTO = 
                    requestTypeService.createRequestTypeByDTO(requestTypeDTO);

            return ResponseEntity.ok().body(newRequestTypeDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (createdBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating RequestType", e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('54','44')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<RequestTypeReadDTO> requestTypeDTOList =
                    requestTypeService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

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


    @PreAuthorize("hasAnyAuthority('54','44')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.REQUEST_TYPE searchType) {
        try {
            RequestTypeReadDTO requestTypeDTO;

            switch (searchType) {
                case BY_ID:
                    requestTypeDTO = requestTypeService.getDTOById(Long.parseLong(searchParam));

                    if (requestTypeDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No RequestType found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME:
                    requestTypeDTO = requestTypeService.getDTOByRequestTypeName(searchParam);

                    if (requestTypeDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No RequestType found with name: '" + searchParam + "'. ");
                    }
                    break;
                    
                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity RequestType");
            }

            return ResponseEntity.ok().body(requestTypeDTO);
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
            String errorMsg = "Error searching for RequestType with ";

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

    @PreAuthorize("hasAnyAuthority('54','44')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_REQUEST_TYPE searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);
            
            List<RequestTypeReadDTO> requestTypeDTOList;

            switch (searchType) {
                case BY_NAME_CONTAINS:
                    requestTypeDTOList = 
                            requestTypeService.getAllDTOInPagingByRequestTypeNameContains(paging, searchParam);

                    if (requestTypeDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No RequestType found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity RequestType");
            }

            return ResponseEntity.ok().body(requestTypeDTOList);
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
            String errorMsg = "Error searching for RequestType with ";

            switch (searchType) {
                case BY_NAME_CONTAINS:
                    errorMsg += "name contains: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54')")
    @PutMapping(value = "/v1/updateRequestType", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequestType(@RequestBody @Valid RequestTypeUpdateDTO requestTypeDTO){
        try {
            RequestTypeReadDTO updatedRequestTypeDTO = requestTypeService.updateRequestTypeByDTO(requestTypeDTO);

            if (updatedRequestTypeDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No RequestType found with Id: '" + requestTypeDTO.getRequestTypeId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedRequestTypeDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating RequestType with Id: '" + requestTypeDTO.getRequestTypeId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deleteRequestType/{requestTypeId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteRequestType(@PathVariable long requestTypeId){
        try {
            if (!requestTypeService.deleteRequestType(requestTypeId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No RequestType found with Id: '" + requestTypeId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted RequestType with Id: '" + requestTypeId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting RequestType with Id: '" + requestTypeId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}