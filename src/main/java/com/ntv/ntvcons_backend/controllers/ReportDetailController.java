package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.services.reportDetail.ReportDetailService;
import com.ntv.ntvcons_backend.utils.ThanhUtil;
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
@RequestMapping("/reportDetail")
public class ReportDetailController {
    @Autowired
    private ReportDetailService reportDetailService;
    @Autowired
    private ThanhUtil thanhUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1/createReportDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createReportDetail(@Valid @RequestBody ReportDetailCreateDTO reportDetailDTO){
        try {
            ReportDetailReadDTO newReportDetailDTO = reportDetailService.createReportDetailByDTO(reportDetailDTO);

            return ResponseEntity.ok().body(newReportDetailDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Report by Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating ReportDetail", e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('44','54','14','24')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ReportDetailReadDTO> reportDetailDTOList =
                    reportDetailService.getAllDTOInPaging(
                            thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (reportDetailDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Report found");
            }

            return ResponseEntity.ok().body(reportDetailDTOList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Report", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54','24','14')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.REPORT_DETAIL searchType) {
        try {
            ReportDetailReadDTO reportDetailDTO;

            switch (searchType) {
                case BY_ID:
                    reportDetailDTO = reportDetailService.getDTOById(Long.parseLong(searchParam));

                    if (reportDetailDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ReportDetail found with Id: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity ReportDetail");
            }

            return ResponseEntity.ok().body(reportDetailDTO);
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
            String errorMsg = "Error searching for ReportDetail with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;

            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54','24','14')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_REPORT_DETAIL searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<ReportDetailReadDTO> reportDetailDTOList;

            switch (searchType) {
                case BY_REPORT_ID:
                    reportDetailDTOList =
                            reportDetailService.getAllDTOInPagingByReportId(paging, Long.parseLong(searchParam));

                    if (reportDetailDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ReportDetail found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity ReportDetail");
            }

            return ResponseEntity.ok().body(reportDetailDTOList);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy || searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for ReportDetail with ";

            switch (searchType) {
                case BY_REPORT_ID:
                    errorMsg += "reportId: '" + searchParam + "'. ";
                    break;

            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }
    
    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1/updateReportDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateReportDetail(@Valid @RequestBody ReportDetailUpdateDTO reportDetailDTO){
        try {
            ReportDetailReadDTO updatedReportDetailDTO = reportDetailService.updateReportDetailByDTO(reportDetailDTO);

            if (updatedReportDetailDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ReportDetail found with Id: '" + reportDetailDTO.getReportDetailId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedReportDetailDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Report by Id (if changed), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating ReportDetail with Id: '" + reportDetailDTO.getReportDetailId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('44','54')")
    @DeleteMapping(value = "/v1/deleteReportDetail/{reportDetailId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteReportDetail(@PathVariable(name = "reportDetailId") long reportDetailId){
        try {
            if (!reportDetailService.deleteReportDetail(reportDetailId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ReportDetail found with Id: '" + reportDetailId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted ReportDetail with Id: '" + reportDetailId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting ReportDetail with Id: '" + reportDetailId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
