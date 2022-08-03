package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileUpdateDTO;
import com.ntv.ntvcons_backend.services.externalFile.ExternalFileService;
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
@RequestMapping("/externalFile")
public class ExternalFileController {
    @Autowired
    private ExternalFileService externalFileService;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @PostMapping(value = "/v1/createExternalFile", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createExternalFile(@RequestBody @Valid ExternalFileCreateDTO externalFileDTO) {
        try {
            ExternalFileReadDTO newExternalFileDTO =
                    externalFileService.createExternalFileByDTO(externalFileDTO);

            return ResponseEntity.ok().body(newExternalFileDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (createdBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating ExternalFile", e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ExternalFileReadDTO> externalFileDTOList =
                    externalFileService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (externalFileDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ExternalFile found");
            }

            return ResponseEntity.ok().body(externalFileDTOList);

        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for ExternalFile", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.EXTERNAL_FILE searchType) {
        try {
            ExternalFileReadDTO externalFileDTO;

            switch (searchType) {
                case BY_ID:
                    externalFileDTO = externalFileService.getDTOById(Long.parseLong(searchParam));

                    if (externalFileDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ExternalFile found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME:
                    externalFileDTO = externalFileService.getDTOByFileName(searchParam);

                    if (externalFileDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ExternalFile found with name: '" + searchParam + "'. ");
                    }
                    break;

                case BY_LINK:
                    externalFileDTO = externalFileService.getDTOByFileLink(searchParam);

                    if (externalFileDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ExternalFile found with link: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity ExternalFile");
            }

            return ResponseEntity.ok().body(externalFileDTO);
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
            String errorMsg = "Error searching for ExternalFile with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;

                case BY_NAME:
                    errorMsg += "name: '" + searchParam + "'. ";
                    break;

                case BY_LINK:
                    errorMsg += "link: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_EXTERNAL_FILE searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<ExternalFileReadDTO> externalFileDTOList;

            switch (searchType) {
                case BY_FILETYPE:
                    externalFileDTOList =
                            externalFileService
                                    .getAllDTOInPagingByFileType(paging, FileType.fromStringValue(searchParam));

                    if (externalFileDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ExternalFile found with fileType: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME_CONTAINS:
                    externalFileDTOList =
                            externalFileService.getAllDTOInPagingByFileNameContains(paging, searchParam);

                    if (externalFileDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ExternalFile found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_LINK_CONTAINS:
                    externalFileDTOList =
                            externalFileService.getAllDTOInPagingByFileLinkContains(paging, searchParam);

                    if (externalFileDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No ExternalFile found with link contains: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity ExternalFile");
            }

            return ResponseEntity.ok().body(externalFileDTOList);
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
            String errorMsg = "Error searching for ExternalFile with ";

            switch (searchType) {
                case BY_NAME_CONTAINS:
                    errorMsg += "name contains: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @PutMapping(value = "/v1/updateExternalFile", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateExternalFile(@RequestBody @Valid ExternalFileUpdateDTO externalFileDTO) {
        try {
            ExternalFileReadDTO updatedExternalFileDTO = externalFileService.updateExternalFileByDTO(externalFileDTO);

            if (updatedExternalFileDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ExternalFile found with Id: '" + externalFileDTO.getFileId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedExternalFileDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (updatedBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating ExternalFile with Id: '" + externalFileDTO.getFileId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @DeleteMapping(value = "/v1/deleteExternalFile/{externalFileId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteExternalFile(@PathVariable long externalFileId) {
        try {
            if (!externalFileService.deleteExternalFile(externalFileId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No ExternalFile found with Id: '" + externalFileId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted ExternalFile with Id: '" + externalFileId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting ExternalFile with Id: '" + externalFileId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
