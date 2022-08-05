package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeUpdateDTO;
import com.ntv.ntvcons_backend.services.reportType.ReportTypeService;
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
@RequestMapping("/reportType")
public class ReportTypeController {
    @Autowired
    private ReportTypeService reportTypeService;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54')")
    @PostMapping(value = "/v1/createReportType", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createReportType(@RequestBody @Valid ReportTypeCreateDTO reportTypeDTO) {
        try {
            ReportTypeReadDTO newReportTypeDTO = reportTypeService.createReportTypeByDTO(reportTypeDTO);

            return ResponseEntity.ok().body(newReportTypeDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating ReportType", e.getMessage()));
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
            List<ReportTypeReadDTO> reportTypeDTOList =
                    reportTypeService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (reportTypeDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ReportType found");
            }

            return ResponseEntity.ok().body(reportTypeDTOList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for ReportType", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','44')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.REPORT_TYPE searchType) {
        try {
            ReportTypeReadDTO reportTypeDTO;

            switch (searchType) {
                case BY_ID:
                    reportTypeDTO = reportTypeService.getDTOById(Long.parseLong(searchParam));

                    if (reportTypeDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ReportType found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME:
                    reportTypeDTO = reportTypeService.getDTOByReportTypeName(searchParam);

                    if (reportTypeDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ReportType found with name: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity ReportType");
            }

            return ResponseEntity.ok().body(reportTypeDTO);
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
            String errorMsg = "Error searching for ReportType with ";

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
                                                @RequestParam SearchType.ALL_REPORT_TYPE searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);
            
            List<ReportTypeReadDTO> reportTypeDTOList;

            switch (searchType) {
                case BY_NAME_CONTAINS:
                    reportTypeDTOList = 
                            reportTypeService.getAllDTOInPagingByReportTypeNameContains(paging, searchParam);

                    if (reportTypeDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ReportType found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity ReportType");
            }

            return ResponseEntity.ok().body(reportTypeDTOList);
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
            String errorMsg = "Error searching for ReportType with ";

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
    @PutMapping(value = "/v1/updateReportType", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateReportType(@RequestBody @Valid ReportTypeUpdateDTO reportTypeDTO) {
        try {
            ReportTypeReadDTO updatedReportTypeDTO = reportTypeService.updateReportTypeByDTO(reportTypeDTO);

            if (updatedReportTypeDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ReportType found with Id: '" + reportTypeDTO.getReportTypeId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedReportTypeDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating ReportType with Id: '" + reportTypeDTO.getReportTypeId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deleteReportType/{reportTypeId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteReportType(@PathVariable long reportTypeId) {
        try {
            if (!reportTypeService.deleteReportType(reportTypeId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ReportType found with Id: '" + reportTypeId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted ReportType with Id: '" + reportTypeId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting ReportType with Id: '" + reportTypeId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}