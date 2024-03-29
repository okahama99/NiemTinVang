package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailUpdateDTO;
import com.ntv.ntvcons_backend.entities.RequestDetail;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.ShowRequestDetailModel;
import com.ntv.ntvcons_backend.services.requestDetail.RequestDetailService;
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
@RequestMapping("/RequestDetail")
public class RequestDetailController {
    @Autowired
    private RequestDetailService requestDetailService;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /*CREATE*/
    @PreAuthorize("hasAnyAuthority('44','54','14','24')")
    @PostMapping(value = "/v1/createRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRequestDetail(@RequestBody @Valid RequestDetailCreateDTO requestDetailDTO) {
        try {
            RequestDetailReadDTO newRequestDetailDTO = 
                    requestDetailService.createRequestDetailByDTO(requestDetailDTO);

            return ResponseEntity.ok().body(newRequestDetailDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (createdBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating RequestDetail", e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('44','54','14','24')")
    @GetMapping(value = "/v1/getAllRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllRequestDetail(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowRequestDetailModel> requests = 
                    requestDetailService.getAllAvailableRequestDetail(pageNo, pageSize, sortBy, sortTypeAsc);

            if (requests == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No RequestDetail found");
            }

            return ResponseEntity.ok().body(requests);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for RequestDetail", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54','14','24')")
    @GetMapping(value = "/v1/getAllById", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<RequestDetail> getAllById(@RequestParam Long requestId) {
        List<RequestDetail> requestDetail = requestDetailService.getRequestDetailByRequestId(requestId);
        return requestDetail;
    }

    @PreAuthorize("hasAnyAuthority('44','54','14','24')")
    @GetMapping(value = "/v1.1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<RequestDetailReadDTO> requestDetailDTOList =
                    requestDetailService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (requestDetailDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No RequestDetail found");
            }

            return ResponseEntity.ok().body(requestDetailDTOList);

        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for RequestDetail", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54','14','24')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.REQUEST_DETAIL searchType) {
        try {
            RequestDetailReadDTO requestDetailDTO;

            switch (searchType) {
                case BY_ID:
                    requestDetailDTO = requestDetailService.getDTOById(Long.parseLong(searchParam));

                    if (requestDetailDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No RequestDetail found with Id: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity RequestDetail");
            }

            return ResponseEntity.ok().body(requestDetailDTO);
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
            String errorMsg = "Error searching for RequestDetail with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54','14','24')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_REQUEST_DETAIL searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<RequestDetailReadDTO> requestDetailDTOList;

            switch (searchType) {
                case BY_REQUEST_ID:
                    requestDetailDTOList =
                            requestDetailService.getAllDTOInPagingByRequestId(paging, Long.parseLong(searchParam));

                    if (requestDetailDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No RequestDetail found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity RequestDetail");
            }

            return ResponseEntity.ok().body(requestDetailDTOList);
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
            String errorMsg = "Error searching for RequestDetail with ";

            switch (searchType) {
                case BY_REQUEST_ID:
                    errorMsg += "requestId: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1/updateRequestDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRequestDetail(@RequestBody @Valid RequestDetailUpdateDTO requestDetailDTO) {
        try {
            RequestDetailReadDTO updatedRequestDetailDTO = 
                    requestDetailService.updateRequestDetailByDTO(requestDetailDTO);

            if (updatedRequestDetailDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No RequestDetail found with Id: '" + requestDetailDTO.getRequestDetailId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedRequestDetailDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (updatedBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating RequestDetail with Id: '" + requestDetailDTO.getRequestDetailId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('44','54')")
    @DeleteMapping(value = "/v1/deleteRequestDetail/{requestDetailId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteRequestDetail(@PathVariable long requestDetailId) {
        try {
            if (!requestDetailService.deleteRequestDetail(requestDetailId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No RequestDetail found with Id: '" + requestDetailId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted RequestDetail with Id: '" + requestDetailId + "'. ");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting RequestDetail with Id: '" + requestDetailId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */
}
