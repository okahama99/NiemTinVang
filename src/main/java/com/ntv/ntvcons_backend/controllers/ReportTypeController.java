package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeUpdateDTO;
import com.ntv.ntvcons_backend.services.reportType.ReportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportType")
public class ReportTypeController {
    @Autowired
    private ReportTypeService reportTypeService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    //@PreAuthorize("hasReportType('ROLE_ADMIN')")
    @PostMapping(value = "/v1/create", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> insertReportType(@RequestBody ReportTypeCreateDTO reportTypeDTO){
        try {
            ReportTypeReadDTO newReportTypeDTO = reportTypeService.createReportTypeByDTO(reportTypeDTO);

            return ResponseEntity.ok().body(newReportTypeDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating ReportType", e.getMessage()));
        }
    }

    /* READ */
    //@PreAuthorize("hasReportType('ROLE_ADMIN')")
    @GetMapping(value = "/v1/read/all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<ReportTypeReadDTO> reportTypeDTOList =
                    reportTypeService.getAllDTO(pageNo, pageSize, sortBy, sortType);

            if (reportTypeDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ReportType found");
            }

            return ResponseEntity.ok().body(reportTypeDTOList);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for ReportType", e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/read", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParams(@RequestParam String reportTypeName) {
        try {
            List<ReportTypeReadDTO> reportTypeDTOList =
                    reportTypeService.getAllDTOByReportTypeNameContains(reportTypeName);

            if (reportTypeDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ReportType found with name contains: " + reportTypeName);
            }

            return ResponseEntity.ok().body(reportTypeDTOList);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for ReportType with name contains: " + reportTypeName,
                            e.getMessage()));
        }
    }

    /* UPDATE */
    //@PreAuthorize("hasReportType('ROLE_ADMIN')")
    @PutMapping(value = "/v1/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateReportType(@RequestBody ReportTypeUpdateDTO reportTypeDTO){
        try {
            ReportTypeReadDTO updatedReportTypeDTO = reportTypeService.updateReportTypeByDTO(reportTypeDTO);

            if (updatedReportTypeDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ReportType found with Id: " + reportTypeDTO.getReportTypeId());
            }

            return ResponseEntity.ok().body(updatedReportTypeDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating ReportType with Id: " + reportTypeDTO.getReportTypeId(),
                            e.getMessage()));
        }
    }

    /* DELETE */
    //@PreAuthorize("hasReportType('ROLE_ADMIN')")
    @DeleteMapping(value = "/v1/delete/{reportTypeId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteReportType(@PathVariable(name = "reportTypeId") long reportTypeId){
        try {
            if (!reportTypeService.deleteReportType(reportTypeId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ReportType found with Id: " + reportTypeId);
            }

            return ResponseEntity.ok().body("Deleted ReportType with Id: " + reportTypeId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting ReportType with Id: " + reportTypeId, e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */


}
