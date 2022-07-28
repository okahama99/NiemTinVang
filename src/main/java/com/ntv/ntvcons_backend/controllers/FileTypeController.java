package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeUpdateDTO;
import com.ntv.ntvcons_backend.services.fileType.FileTypeService;
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
@RequestMapping("/fileType")
public class FileTypeController {
    @Autowired
    private FileTypeService fileTypeService;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @PostMapping(value = "/v1/createFileType", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createFileType(@Valid @RequestBody FileTypeCreateDTO fileTypeDTO) {
        try {
            FileTypeReadDTO newFileTypeDTO = fileTypeService.createFileTypeByDTO(fileTypeDTO);

            return ResponseEntity.ok().body(newFileTypeDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (createdBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating FileType", e.getMessage()));
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
            List<FileTypeReadDTO> fileTypeDTOList =
                    fileTypeService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (fileTypeDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No FileType found");
            }

            return ResponseEntity.ok().body(fileTypeDTOList);

        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for FileType", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.FILE_TYPE searchType) {
        try {
            FileTypeReadDTO fileTypeDTO;

            switch (searchType) {
                case BY_ID:
                    fileTypeDTO = fileTypeService.getDTOById(Long.parseLong(searchParam));

                    if (fileTypeDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No FileType found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME:
                    fileTypeDTO = fileTypeService.getDTOByFileTypeName(searchParam);

                    if (fileTypeDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No FileType found with name: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity FileType");
            }

            return ResponseEntity.ok().body(fileTypeDTO);
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
            String errorMsg = "Error searching for FileType with ";

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

    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_FILE_TYPE searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<FileTypeReadDTO> fileTypeDTOList;

            switch (searchType) {
                case BY_NAME_CONTAINS:
                    fileTypeDTOList =
                            fileTypeService.getAllDTOInPagingByFileTypeNameContains(paging, searchParam);

                    if (fileTypeDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No FileType found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity FileType");
            }

            return ResponseEntity.ok().body(fileTypeDTOList);
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
            String errorMsg = "Error searching for FileType with ";

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
    @PutMapping(value = "/v1/updateFileType", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateFileType(@Valid @RequestBody FileTypeUpdateDTO fileTypeDTO) {
        try {
            FileTypeReadDTO updatedFileTypeDTO = fileTypeService.updateFileTypeByDTO(fileTypeDTO);

            if (updatedFileTypeDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No FileType found with Id: '" + fileTypeDTO.getFileTypeId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedFileTypeDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (updatedBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating FileType with Id: '" + fileTypeDTO.getFileTypeId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54','24','14','44')")
    @DeleteMapping(value = "/v1/deleteFileType/{fileTypeId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteFileType(@PathVariable(name = "fileTypeId") long fileTypeId) {
        try {
            if (!fileTypeService.deleteFileType(fileTypeId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No FileType found with Id: '" + fileTypeId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted FileType with Id: '" + fileTypeId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting FileType with Id: '" + fileTypeId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
