package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportUpdateDTO;
import com.ntv.ntvcons_backend.services.misc.FileCombineService;
import com.ntv.ntvcons_backend.services.report.ReportService;
import com.ntv.ntvcons_backend.utils.JwtUtil;
import com.ntv.ntvcons_backend.utils.MiscUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private FileCombineService fileCombineService;
    @Autowired
    private MiscUtil miscUtil;
    @Autowired
    private JwtUtil jwtUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1/createReport", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createReport(
            @RequestBody @Valid ReportCreateDTO reportDTO,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            reportDTO.setReporterId(userId);
            reportDTO.setCreatedBy(userId);

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

    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1/createReport/withFile",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createReportWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    ReportCreateDTO reportDTO,
            @RequestPart(required = false) @Size(min = 1) List<MultipartFile> reportDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            reportDTO.setReporterId(userId);
            reportDTO.setCreatedBy(userId);

            ReportReadDTO newReportDTO = reportService.createReportByDTO(reportDTO);

            if (reportDocList != null) {
                long reportId = newReportDTO.getReportId();

                fileCombineService.saveAllFileInDBAndFirebase(
                        reportDocList, FileType.REPORT_DOC, reportId, EntityType.REPORT_ENTITY, userId);

                /* Get again after file created & save */
                newReportDTO = reportService.getDTOById(reportId);
            }

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

    @PreAuthorize("hasAnyAuthority('44')")
    @PostMapping(value = "/v1/addFile/{reportId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addFileToReportById(
            @PathVariable long reportId,
            @RequestPart @Size(min = 1) List<MultipartFile> reportDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            ReportReadDTO reportDTO = reportService.getDTOById(reportId);

            if (reportDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Report found with Id: '" + reportId + "' to add file.");

            fileCombineService.saveAllFileInDBAndFirebase(
                    reportDocList, FileType.REPORT_DOC, reportId, EntityType.REPORT_ENTITY, userId);

            /* Get again after file created & save */
            reportDTO = reportService.getDTOById(reportId);

            return ResponseEntity.ok().body(reportDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error adding file to Report with Id: '" + reportId + "'. ",
                            e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('44','54','24','14')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ReportReadDTO> reportDTOList =
                    reportService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

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

    @PreAuthorize("hasAnyAuthority('44','54','24','14')")
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

    @PreAuthorize("hasAnyAuthority('44','24','14','54')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_REPORT searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

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
    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1/updateReport", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateReport(
            @RequestBody @Valid ReportUpdateDTO reportDTO,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            reportDTO.setUpdatedBy(userId);

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

    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1/updateReport/withFile",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateReportWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    ReportUpdateDTO reportDTO,
            @RequestPart(required = false) @Size(min = 1) List<MultipartFile> reportDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            reportDTO.setUpdatedBy(userId);

            ReportReadDTO updatedReportDTO = reportService.updateReportByDTO(reportDTO);

            if (updatedReportDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Report found with Id: '" + reportDTO.getReportId() + "'. ");

            if (reportDocList != null) {
                long reportId = updatedReportDTO.getReportId();

                /* Deleted old report file */
                List<ExternalFileReadDTO> fileDTOList = updatedReportDTO.getFileList();
                if (fileDTOList != null)
                    fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);

                fileCombineService.saveAllFileInDBAndFirebase(
                        reportDocList, FileType.REPORT_DOC, reportId, EntityType.REPORT_ENTITY, userId);

                /* Get again after file created & save */
                updatedReportDTO = reportService.getDTOById(reportId);
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

    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1/replaceFile/{reportId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> replaceFileOfReportById(
            @PathVariable long reportId,
            @RequestParam @Size(min = 1) List<Long> removeFileIdList,
            @RequestPart @Size(min = 1) List<MultipartFile> reportDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            ReportReadDTO reportDTO = reportService.getDTOById(reportId);

            if (reportDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Report found with Id: '" + reportId + "' to replace file.");

            List<ExternalFileReadDTO> fileDTOList = reportDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Report with Id: '" + reportId + "' has no file to replace. "
                                + "Try using 'POST:../addFile/{reportId}' instead");
            } else {
                Set<Long> oldFileIdSet =
                        fileDTOList.stream()
                                .map(ExternalFileReadDTO::getFileId)
                                .collect(Collectors.toSet());

                StringBuilder errorMsg = new StringBuilder();
                for (Long removeFileId : removeFileIdList) {
                    if (!oldFileIdSet.contains(removeFileId)) {
                        errorMsg.append("Report with Id: '")
                                .append(reportId).append("' has no File with Id: '")
                                .append(removeFileId).append("' to remove. ");
                    }
                }

                if (!errorMsg.toString().trim().isEmpty())
                    throw new IllegalArgumentException(errorMsg.toString());

                List<ExternalFileReadDTO> removeFileDTOList = new ArrayList<>();

                for (ExternalFileReadDTO fileDTO : fileDTOList) {
                    if (removeFileIdList.contains(fileDTO.getFileId())) {
                        removeFileDTOList.add(fileDTO);
                    }
                }

                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(removeFileDTOList);
            }

            fileCombineService.saveAllFileInDBAndFirebase(
                    reportDocList, FileType.REPORT_DOC, reportId, EntityType.REPORT_ENTITY, userId);

            /* Get again after file created & save */
            reportDTO = reportService.getDTOById(reportId);

            return ResponseEntity.ok().body(reportDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error replacing file of Report with Id: '" + reportId + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44')")
    @PutMapping(value = "/v1/replaceAllFile/{reportId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> replaceAllFileOfReportById(
            @PathVariable long reportId,
            @RequestPart @Size(min = 1) List<MultipartFile> reportDocList,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) {
        try {
            String jwt = jwtUtil.getAndValidateJwt(token);
            Long userId = jwtUtil.getUserIdFromJWT(jwt);
            if (userId == null)
                throw new IllegalArgumentException("Invalid jwt.");

            ReportReadDTO reportDTO = reportService.getDTOById(reportId);

            if (reportDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Report found with Id: '" + reportId + "' to replace file.");

            List<ExternalFileReadDTO> fileDTOList = reportDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Report with Id: '" + reportId + "' has no file to replace. "
                                + "Try using 'POST:../addFile/{reportId}' instead");
            } else {
                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
            }

            fileCombineService.saveAllFileInDBAndFirebase(
                    reportDocList, FileType.REPORT_DOC, reportId, EntityType.REPORT_ENTITY, userId);

            /* Get again after file created & save */
            reportDTO = reportService.getDTOById(reportId);

            return ResponseEntity.ok().body(reportDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error replacing file of Report with Id: '" + reportId + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('44','54')")
    @DeleteMapping(value = "/v1/deleteReport/{reportId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteReport(@PathVariable long reportId) {
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

    @PreAuthorize("hasAnyAuthority('44','54')")
    @DeleteMapping(value = "/v1/deleteFile/{reportId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteFileOfReportById(
            @PathVariable long reportId,
            @RequestParam @Size(min = 1) List<Long> removeFileIdList) {
        try {
            ReportReadDTO reportDTO = reportService.getDTOById(reportId);

            if (reportDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Report found with Id: '" + reportId + "' to delete file.");

            List<ExternalFileReadDTO> fileDTOList = reportDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Report with Id: '" + reportId + "' has no file to delete. ");
            } else {
                Set<Long> oldFileIdSet =
                        fileDTOList.stream()
                                .map(ExternalFileReadDTO::getFileId)
                                .collect(Collectors.toSet());

                StringBuilder errorMsg = new StringBuilder();
                for (Long removeFileId : removeFileIdList) {
                    if (!oldFileIdSet.contains(removeFileId)) {
                        errorMsg.append("Report with Id: '")
                                .append(reportId).append("' has no File with Id: '")
                                .append(removeFileId).append("' to remove. ");
                    }
                }

                if (!errorMsg.toString().trim().isEmpty())
                    throw new IllegalArgumentException(errorMsg.toString());

                List<ExternalFileReadDTO> removeFileDTOList = new ArrayList<>();

                for (ExternalFileReadDTO fileDTO : fileDTOList) {
                    if (removeFileIdList.contains(fileDTO.getFileId())) {
                        removeFileDTOList.add(fileDTO);
                    }
                }

                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(removeFileDTOList);
            }

            /* Get again after file delete & save */
            reportDTO = reportService.getDTOById(reportId);

            return ResponseEntity.ok().body(reportDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error deleting file of Report with Id: '" + reportId + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('44','54')")
    @DeleteMapping(value = "/v1/deleteAllFile/{reportId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteAllFileOfReportById(@PathVariable long reportId) {
        try {
            ReportReadDTO reportDTO = reportService.getDTOById(reportId);

            if (reportDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Report found with Id: '" + reportId + "' to delete file.");

            List<ExternalFileReadDTO> fileDTOList = reportDTO.getFileList();
            if (fileDTOList == null) {
                return ResponseEntity.badRequest()
                        .body("Report with Id: '" + reportId + "' has no file to delete. ");
            } else {
                fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
            }

            /* Get again after file created & save */
            reportDTO = reportService.getDTOById(reportId);

            return ResponseEntity.ok().body(reportDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid input */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Error deleting file of Report with Id: '" + reportId + "'. ",
                            e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
