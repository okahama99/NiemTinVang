package com.ntv.ntvcons_backend.controllers;


import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintCreateDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintReadDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintUpdateDTO;
import com.ntv.ntvcons_backend.entities.BlueprintModels.ShowBlueprintModel;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintService;
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
@RequestMapping("/blueprint")
public class BlueprintController {
    @Autowired
    private BlueprintService blueprintService;
    @Autowired
    private ThanhUtil thanhUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/createBlueprint", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createBlueprint(@Valid @RequestBody BlueprintCreateDTO blueprintDTO) {
        try {
            BlueprintReadDTO newBlueprintDTO = blueprintService.createBlueprintByDTO(blueprintDTO);

            return ResponseEntity.ok().body(newBlueprintDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User by respective Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Blueprint", e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('54','24','14')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowBlueprintModel> blueprints = blueprintService.getAll(pageNo, pageSize, sortBy, sortTypeAsc);

            if (blueprints == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Blueprint found");
            }

            return ResponseEntity.ok().body(blueprints);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Blueprint", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14')")
    @GetMapping(value = "/v1.1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllAlt1(@RequestParam int pageNo,
                                             @RequestParam int pageSize,
                                             @RequestParam String sortBy,
                                             @RequestParam boolean sortTypeAsc) {
        try {
            List<BlueprintReadDTO> blueprintList =
                    blueprintService.getAllDTOInPaging(
                            thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (blueprintList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Blueprint found");
            }

            return ResponseEntity.ok().body(blueprintList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Blueprint", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.BLUEPRINT searchType) {
        try {
            BlueprintReadDTO blueprintDTO;

            switch (searchType) {
                case BY_ID:
                    blueprintDTO = blueprintService.getDTOById(Long.parseLong(searchParam));

                    if (blueprintDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Blueprint found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_PROJECT_ID:
                    blueprintDTO = blueprintService.getDTOByProjectId(Long.parseLong(searchParam));

                    if (blueprintDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Blueprint found with projectId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_NAME:
                    blueprintDTO = blueprintService.getDTOByBlueprintName(searchParam);

                    if (blueprintDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Blueprint found with name: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Blueprint");
            }

            return ResponseEntity.ok().body(blueprintDTO);
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
            String errorMsg = "Error searching for Blueprint with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;

                case BY_PROJECT_ID:
                    errorMsg += "projectId: '" + searchParam + "'. ";
                    break;

                case BY_NAME:
                    errorMsg += "name: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_BLUEPRINT searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<BlueprintReadDTO> blueprintDTOList;

            switch (searchType) {
                case BY_NAME_CONTAINS:
                    blueprintDTOList = blueprintService.getAllDTOInPagingByBlueprintNameContains(paging, searchParam);

                    if (blueprintDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Blueprint found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_DESIGNER_NAME:
                    blueprintDTOList = blueprintService.getAllDTOInPagingByDesignerName(paging, searchParam);

                    if (blueprintDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Blueprint found with designerName: '" + searchParam + "'. ");
                    }
                    break;

                case BY_DESIGNER_NAME_CONTAINS:
                    blueprintDTOList = blueprintService.getAllDTOInPagingByDesignerNameContains(paging, searchParam);

                    if (blueprintDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Blueprint found with designerName contains: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Blueprint");
            }

            return ResponseEntity.ok().body(blueprintDTOList);
        } /* When have long param, use
        catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } */
        catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for Blueprint with ";

            switch (searchType) {
                case BY_NAME_CONTAINS:
                    errorMsg += "name contains: '" + searchParam + "'. ";
                    break;

                case BY_DESIGNER_NAME:
                    errorMsg += "designerName: '" + searchParam + "'. ";
                    break;

                case BY_DESIGNER_NAME_CONTAINS:
                    errorMsg += "designerName contains: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14')")
    @GetMapping(value = "/v1/checkDuplicate", produces = "application/json;charset=UTF-8")
    public @ResponseBody String checkDuplicate(@RequestParam String blueprintName) {
        return blueprintService.checkDuplicate(blueprintName);
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updateBlueprint", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateBlueprint(@Valid @RequestBody BlueprintUpdateDTO blueprintDTO) {
        try {
            BlueprintReadDTO updatedBlueprintDTO = blueprintService.updateBlueprintByDTO(blueprintDTO);

            if (updatedBlueprintDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Blueprint found with Id: '" + blueprintDTO.getBlueprintId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedBlueprintDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User by respective Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Blueprint", e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deleteBlueprint/{blueprintId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteBlueprint(@PathVariable(name = "blueprintId") long blueprintId) {
        try {
            if (!blueprintService.deleteBlueprint(blueprintId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Blueprint found with Id: '" + blueprintId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Blueprint with Id: '" + blueprintId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Blueprint with Id: '" + blueprintId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}

