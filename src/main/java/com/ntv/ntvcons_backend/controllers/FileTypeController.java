package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeUpdateDTO;
import com.ntv.ntvcons_backend.services.fileType.FileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fileType")
public class FileTypeController {
    @Autowired
    FileTypeService fileTypeService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    //@PreAuthorize("hasFileType('ROLE_ADMIN')")
    @PostMapping(value = "/v1/createFileType", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> insertFileType(@RequestBody FileTypeCreateDTO fileTypeDTO) {
        try {
            FileTypeReadDTO newFileTypeDTO = fileTypeService.createFileTypeByDTO(fileTypeDTO);

            return ResponseEntity.ok().body(newFileTypeDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating FileType", e.getMessage()));
        }
    }

    /* READ */
    //@PreAuthorize("hasFileType('ROLE_ADMIN')")
    @GetMapping(value = "/v1/read/all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<FileTypeReadDTO> fileTypeDTOList =
                    fileTypeService.getAllDTO(pageNo, pageSize, sortBy, sortType);

            if (fileTypeDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No FileType found");
            }

            return ResponseEntity.ok().body(fileTypeDTOList);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for FileType", e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/read", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParams(@RequestParam String searchParam,
                                              @RequestParam("searchType") SearchType searchType) {
        try {
            List<FileTypeReadDTO> fileTypeDTOList;

            switch (searchType) {
                case FILE_TYPE_BY_NAME_CONTAINS:
                    fileTypeDTOList = fileTypeService.getAllDTOByFileTypeNameContains(searchParam);

                    if (fileTypeDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No FileType found with name contains: " + searchParam);
                    }
                    break;

                case FILE_TYPE_BY_EXTENSION_CONTAINS:
                    fileTypeDTOList = fileTypeService.getAllDTOByFileTypeExtensionContains(searchParam);

                    if (fileTypeDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No FileType found with name contains: " + searchParam);
                    }
                    break;

                default:
                    return ResponseEntity.badRequest().body("Wrong search type for entity FileType");
            }

            return ResponseEntity.ok().body(fileTypeDTOList);
        } catch (Exception e) {
            String errorMsg = "";

            switch (searchType) {
                case FILE_TYPE_BY_NAME_CONTAINS:
                    errorMsg += "Error searching for FileType with name contains: " + searchParam;
                    break;

                case FILE_TYPE_BY_EXTENSION_CONTAINS:
                    errorMsg += "Error searching for FileType with extension contains: " + searchParam;
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    //@PreAuthorize("hasFileType('ROLE_ADMIN')")
    @PutMapping(value = "/v1/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateFileType(@RequestBody FileTypeUpdateDTO fileTypeDTO) {
        try {
            FileTypeReadDTO updatedFileTypeDTO = fileTypeService.updateFileTypeByDTO(fileTypeDTO);

            if (updatedFileTypeDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No FileType found with Id: " + fileTypeDTO.getFileTypeId());
            }

            return ResponseEntity.ok().body(updatedFileTypeDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating FileType with Id: " + fileTypeDTO.getFileTypeId(),
                            e.getMessage()));
        }
    }

    /* DELETE */
    //@PreAuthorize("hasFileType('ROLE_ADMIN')")
    @DeleteMapping(value = "/v1/delete/{fileTypeId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteFileType(@PathVariable(name = "fileTypeId") long fileTypeId) {
        try {
            if (!fileTypeService.deleteFileType(fileTypeId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No FileType found with Id: " + fileTypeId);
            }

            return ResponseEntity.ok().body("Deleted FileType with Id: " + fileTypeId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting FileType with Id: " + fileTypeId, e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
