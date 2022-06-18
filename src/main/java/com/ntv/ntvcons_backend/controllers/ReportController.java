package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.report.ReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportUpdateDTO;
import com.ntv.ntvcons_backend.services.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/v1/createReport", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createReport(@RequestBody ReportCreateDTO reportDTO){
        try {
            ReportReadDTO newReportDTO = reportService.createReportByDTO(reportDTO);

            return ResponseEntity.ok().body(newReportDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User/ReportType by respective Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Report", e.getMessage()));
        }
    }

    /* READ */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<ReportReadDTO> reportDTOList = reportService.getAllDTO(pageNo, pageSize, sortBy, sortType);

            if (reportDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Report found");
            }

            return ResponseEntity.ok().body(reportDTOList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Report", e.getMessage()));
        }
    }
    
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam, 
                                             @RequestParam(name = "searchType") SearchType searchType) {
        try {
            List<ReportReadDTO> reportDTOList;

            switch (searchType) {
                case REPORT_BY_ID:
                    ReportReadDTO reportDTO = reportService.getDTOById(Long.parseLong(searchParam));

                    if (reportDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Report found with reportId: " + searchParam);
                    }

                    reportDTOList = new ArrayList<>(Collections.singletonList(reportDTO));
                    break;

                case REPORT_BY_PROJECT_ID:
                    reportDTOList = reportService.getAllDTOByProjectId(Long.parseLong(searchParam));

                    if (reportDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Report found with projectId: " + searchParam);
                    }
                    break;

                case REPORT_BY_REPORTER_ID:
                    reportDTOList = reportService.getAllDTOByReporterId(Long.parseLong(searchParam));

                    if (reportDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Report found with reporterId: " + searchParam);
                    }
                    break;

                case REPORT_BY_REPORT_TYPE_ID:
                    reportDTOList = reportService.getAllDTOByReportTypeId(Long.parseLong(searchParam));

                    if (reportDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Report found with reportTypeId: " + searchParam);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Report");
            }

            return ResponseEntity.ok().body(reportDTOList);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: " + searchType 
                                    + "\nExpecting parameter of type: Long", 
                            nFE.getMessage()));
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for Report with ";

            switch (searchType) {
                case REPORT_BY_ID:
                    errorMsg += "reportId: " + searchParam;
                    break;

                case REPORT_BY_PROJECT_ID:
                    errorMsg += "projectId: " + searchParam;
                    break;

                case REPORT_BY_REPORTER_ID:
                    errorMsg += "reporterId: " + searchParam;
                    break;

                case REPORT_BY_REPORT_TYPE_ID:
                    errorMsg += "reportTypeId: " + searchParam;
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* TODO: example projectId & reporterId, projectId & reportTypeId,...
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/v1/getByMultiParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByMultiParam(@RequestBody Object searchParams,
                                                  @RequestParam(name = "searchType") SearchType searchType) {
        return null;
    }*/

    /* UPDATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateReport", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateReport(@RequestBody ReportUpdateDTO reportDTO){
        try {
            ReportReadDTO updatedReportDTO = reportService.updateReportByDTO(reportDTO);

            if (updatedReportDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Report found with Id: " + reportDTO.getReportId());
            }

            return ResponseEntity.ok().body(updatedReportDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User/ReportType by respective Id (if changed), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Report with Id: " + reportDTO.getReportId(),
                            e.getMessage()));
        }
    }
    
    /* DELETE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/v1/deleteReport/{reportId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteReport(@PathVariable(name = "reportId") long reportId){
        try {
            if (!reportService.deleteReport(reportId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Report found with Id: " + reportId);
            }

            return ResponseEntity.ok().body("Deleted Report with Id: " + reportId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Report with Id: " + reportId, e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
