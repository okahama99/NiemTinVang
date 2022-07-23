package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.report.ReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportUpdateDTO;
import com.ntv.ntvcons_backend.services.report.ReportService;
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
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ThanhUtil thanhUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54')")
    @PostMapping(value = "/v1/createReport", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createReport(@Valid @RequestBody ReportCreateDTO reportDTO){
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
    @PreAuthorize("hasAnyAuthority('54','64','24','14')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ReportReadDTO> reportDTOList =
                    reportService.getAllDTOInPaging(
                            thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

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

    @PreAuthorize("hasAnyAuthority('54','64','24','14')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.REPORT searchType) {
        try {
            ReportReadDTO reportDTO;

            switch (searchType) {
                case BY_ID:
                    reportDTO = reportService.getDTOById(Long.parseLong(searchParam));

                    if (reportDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Report found with Id: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Report");
            }

            return ResponseEntity.ok().body(reportDTO);
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
            String errorMsg = "Error searching for Report with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;

            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14','64')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_REPORT searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<ReportReadDTO> reportDTOList;

            switch (searchType) {
                case BY_PROJECT_ID:
                    reportDTOList = reportService.getAllDTOInPagingByProjectId(paging, Long.parseLong(searchParam));

                    if (reportDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Report found with projectId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_REPORTER_ID:
                    reportDTOList = reportService.getAllDTOInPagingByReporterId(paging, Long.parseLong(searchParam));

                    if (reportDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Report found with reporterId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_REPORT_TYPE_ID:
                    reportDTOList = reportService.getAllDTOInPagingByReportTypeId(paging, Long.parseLong(searchParam));

                    if (reportDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Report found with reportTypeId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME:
                    reportDTOList = reportService.getAllDTOInPagingByReportName(paging, searchParam);

                    if (reportDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Report found with name: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME_CONTAINS:
                    reportDTOList = reportService.getAllDTOInPagingByReportNameContains(paging, searchParam);

                    if (reportDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Report found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_REPORT_DATE:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("This searchType is not yet supported: '" + searchParam + "'. ");

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Report");
            }

            return ResponseEntity.ok().body(reportDTOList);
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
            String errorMsg = "Error searching for Report with ";

            switch (searchType) {
                case BY_PROJECT_ID:
                    errorMsg += "projectId: '" + searchParam + "'. ";
                    break;

                case BY_REPORTER_ID:
                    errorMsg += "reporterId: '" + searchParam + "'. ";
                    break;

                case BY_REPORT_TYPE_ID:
                    errorMsg += "reportTypeId: '" + searchParam + "'. ";
                    break;

                case BY_NAME:
                    errorMsg += "name: '" + searchParam + "'. ";
                    break;

                case BY_NAME_CONTAINS:
                    errorMsg += "name contains: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54')")
    @PutMapping(value = "/v1/updateReport", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateReport(@Valid @RequestBody ReportUpdateDTO reportDTO){
        try {
            ReportReadDTO updatedReportDTO = reportService.updateReportByDTO(reportDTO);

            if (updatedReportDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Report found with Id: '" + reportDTO.getReportId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedReportDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User/ReportType by respective Id (if changed), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Report with Id: '" + reportDTO.getReportId() + "'. ",
                            e.getMessage()));
        }
    }
    
    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54','64')")
    @DeleteMapping(value = "/v1/deleteReport/{reportId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteReport(@PathVariable(name = "reportId") long reportId){
        try {
            if (!reportService.deleteReport(reportId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Report found with Id: '" + reportId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Report with Id: '" + reportId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Report with Id: '" + reportId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
