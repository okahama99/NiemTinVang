package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.services.reportDetail.ReportDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reportDetail")
public class ReportDetailController {
    @Autowired
    private ReportDetailService reportDetailService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyRole('Engineer')")
    @PostMapping(value = "/v1/createReportDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createReportDetail(@RequestBody ReportDetailCreateDTO reportDetailDTO){
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
    @PreAuthorize("hasAnyRole('Engineer','Admin','Staff','Customer')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<ReportDetailReadDTO> reportDetailDTOList =
                    reportDetailService.getAllDTO(pageNo, pageSize, sortBy, sortType);

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

    /* UPDATE */
    @PreAuthorize("hasAnyRole('Engineer')")
    @PutMapping(value = "/v1/updateReportDetail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateReportDetail(@RequestBody ReportDetailUpdateDTO reportDetailDTO){
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
    @PreAuthorize("hasAnyRole('Engineer','Admin')")
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
