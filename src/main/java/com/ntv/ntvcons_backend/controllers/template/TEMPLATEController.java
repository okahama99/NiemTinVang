package com.ntv.ntvcons_backend.controllers.template;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temPlate")
public class TEMPLATEController {
//    @Autowired
//    private TEMPLATEService temPlateService;
//
//    /* ================================================ Ver 1 ================================================ */
//    /* CREATE */
//    @PostMapping(value = "/v1/createTEMPLATE", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> createTEMPLATE(@RequestBody @Valid TEMPLATECreateDTO temPlateDTO) {
//        try {
//            TEMPLATEReadDTO newTEMPLATEDTO = temPlateService.createTEMPLATEByDTO(temPlateDTO);
//
//            return ResponseEntity.ok().body(newTEMPLATEDTO);
//
//        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
//            /* Catch not found .../... by respective Id, which violate FK constraint */
//            return ResponseEntity.badRequest().body(
//                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body(
//                    new ErrorResponse("Error creating TEMPLATE", e.getMessage()));
//        }
//    }
//
//    /* READ */
//    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
//                                         @RequestParam int pageSize,
//                                         @RequestParam String sortBy,
//                                         @RequestParam boolean sortTypeAsc) {
//        try {
//            List<TEMPLATEReadDTO> temPlateDTOList =
//                    temPlateService.getAllDTO(pageNo, pageSize, sortBy, sortTypeAsc);
//
//            if (temPlateDTOList == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No TEMPLATE found");
//            }
//
//            return ResponseEntity.ok().body(temPlateDTOList);
//        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
//            /* Catch invalid sortBy */
//            return ResponseEntity.badRequest().body(
//                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body(
//                    new ErrorResponse("Error searching for TEMPLATE", e.getMessage()));
//        }
//    }
//
//    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
//                                                @RequestParam SearchType searchType,
//                                                @RequestParam int pageNo,
//                                                @RequestParam int pageSize,
//                                                @RequestParam String sortBy,
//                                                @RequestParam boolean sortTypeAsc) {
//        try {
//            List<TEMPLATEReadDTO> temPlateDTOList;
//
//            switch (searchType) {
//                case TEMPLATE_BY_NAME_CONTAINS:
//                    temPlateDTOList = temPlateService.getAllDTOByTEMPLATENameContains(searchParam);
//                    if (temPlateDTOList == null) {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                                .body("No TEMPLATE found with name contains: '" + searchParam + "'. ");
//                    }
//                    break;
//                case TEMPLATE_BY_PROJECT_ID:
//                    temPlateDTOList = temPlateService.getAllDTOByProjectId(Long.parseLong(searchParam));
//
//                    if (temPlateDTOList == null) {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                                .body("No TEMPLATE found with projectId: '" + searchParam + "'. ");
//                    }
//                    break;
//                default:
//                    throw new IllegalArgumentException("Invalid SearchType used for entity TEMPLATE");
//            }
//
//            return ResponseEntity.ok().body(temPlateDTOList);
//        } catch (NumberFormatException nFE) {
//            return ResponseEntity.badRequest().body(
//                  new ErrorResponse(
//                          "Invalid parameter type for searchType: '" + searchType
//                              + "'. Expecting parameter of type: Long",
//                          nFE.getMessage()));
//        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
//            /* Catch invalid sortBy/searchType */
//            return ResponseEntity.badRequest().body(
//                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
//        } catch (Exception e) {
//            e.printStackTrace();
//            String errorMsg = "Error searching for TEMPLATE with ";
//
//            switch (searchType) {
//                case TEMPLATE_BY_NAME_CONTAINS:
//                    errorMsg += "name contains: '" + searchParam + "'. ";
//                    break;
//
//                case TEMPLATE_BY_PROJECT_ID:
//                    errorMsg += "projectId: '" + searchParam + "'. ";
//                    break;
//            }
//
//            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
//        }
//    }
//
//    /* UPDATE */
//    @PutMapping(value = "/v1/updateTEMPLATE", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> updateTEMPLATE(@RequestBody @Valid TEMPLATEUpdateDTO temPlateDTO) {
//        try {
//            TEMPLATEReadDTO updatedTEMPLATEDTO = temPlateService.updateTEMPLATEByDTO(temPlateDTO);
//
//            if (updatedTEMPLATEDTO == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("No TEMPLATE found with Id: '" + temPlateDTO.getTEMPLATEId() + "'. ");
//            }
//
//            return ResponseEntity.ok().body(updatedTEMPLATEDTO);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body(
//                    new ErrorResponse("Error updating TEMPLATE with Id: '" + temPlateDTO.getTEMPLATEId() + "'. ",
//                            e.getMessage()));
//        }
//    }
//
//    /* DELETE */
//    @DeleteMapping(value = "/v1/deleteTEMPLATE/{temPlateId}", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> deleteTEMPLATE(@PathVariable long temPlateId) {
//        try {
//            if (!temPlateService.deleteTEMPLATE(temPlateId)) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("No TEMPLATE found with Id: '" + temPlateId + "'. ");
//            }
//
//            return ResponseEntity.ok().body("Deleted TEMPLATE with Id: '" + temPlateId + "'. ");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body(
//                    new ErrorResponse("Error deleting TEMPLATE with Id: '" + temPlateId + "'. ", e.getMessage()));
//        }
//    }
//    /* ================================================ Ver 1 ================================================ */

}