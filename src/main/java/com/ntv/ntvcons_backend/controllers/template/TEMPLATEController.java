package com.ntv.ntvcons_backend.controllers.template;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
//import com.ntv.ntvcons_backend.dtos.temPlate.TEMPLATECreateDTO;
//import com.ntv.ntvcons_backend.dtos.temPlate.TEMPLATEReadDTO;
//import com.ntv.ntvcons_backend.dtos.temPlate.TEMPLATEUpdateDTO;
//import com.ntv.ntvcons_backend.services.temPlate.TEMPLATEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/temPlate")
public class TEMPLATEController {
//    @Autowired
//    private TEMPLATEService temPlateService;
//
//    /* ================================================ Ver 1 ================================================ */
//    /* CREATE */
//    //@PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping(value = "/v1/createTEMPLATE", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> createTEMPLATE(@RequestBody TEMPLATECreateDTO temPlateDTO){
//        try {
//            TEMPLATEReadDTO newTEMPLATEDTO = temPlateService.createTEMPLATEByDTO(temPlateDTO);
//
//            return ResponseEntity.ok().body(newTEMPLATEDTO);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(
//                    new ErrorResponse("Error creating TEMPLATE", e.getMessage()));
//        }
//    }
//
//    /* READ */
//    //@PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
//                                         @RequestParam int pageSize,
//                                         @RequestParam String sortBy,
//                                         @RequestParam boolean sortType) {
//        try {
//            List<TEMPLATEReadDTO> temPlateDTOList =
//                    temPlateService.getAllDTO(pageNo, pageSize, sortBy, sortType);
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
//            return ResponseEntity.internalServerError().body(
//                    new ErrorResponse("Error searching for TEMPLATE", e.getMessage()));
//        }
//    }
//
//    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
//                                             @RequestParam(name = "searchType") SearchType searchType) {
//        try {
//            List<TEMPLATEReadDTO> temPlateDTOList;
//
//            switch (searchType) {
//                case TEMPLATE_BY_NAME_CONTAINS:
//                    temPlateDTOList = temPlateService.getAllDTOByTEMPLATENameContains(searchParam);
//                    if (temPlateDTOList == null) {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                                .body("No TEMPLATE found with name contains: " + searchParam);
//                    }
//                    break;
//                case TEMPLATE_BY_PROJECT_ID:
//                    temPlateDTOList = temPlateService.getAllDTOByProjectId(Long.parseLong(searchParam));
//
//                    if (reportDTOList == null) {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                                .body("No TEMPLATE found with projectId: " + searchParam);
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
//                          "Invalid parameter type for searchType: " + searchType
//                              + "\nExpecting parameter of type: Long",
//                          nFE.getMessage()));
//        } catch (IllegalArgumentException iAE) {
//            /* Catch invalid searchType */
//            return ResponseEntity.badRequest().body(
//                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
//        } catch (Exception e) {
//            String errorMsg = "Error searching for TEMPLATE with ";
//
//            switch (searchType) {
//                case REPORT_TYPE_BY_NAME_CONTAINS:
//                    errorMsg += "name contains: " + searchParam;
//                    break;
//            }
//
//            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
//        }
//    }
//
//    /* UPDATE */
//    //@PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping(value = "/v1/updateTEMPLATE", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> updateTEMPLATE(@RequestBody TEMPLATEUpdateDTO temPlateDTO){
//        try {
//            TEMPLATEReadDTO updatedTEMPLATEDTO = temPlateService.updateTEMPLATEByDTO(temPlateDTO);
//
//            if (updatedTEMPLATEDTO == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("No TEMPLATE found with Id: " + temPlateDTO.getTEMPLATEId());
//            }
//
//            return ResponseEntity.ok().body(updatedTEMPLATEDTO);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(
//                    new ErrorResponse("Error updating TEMPLATE with Id: " + temPlateDTO.getTEMPLATEId(),
//                            e.getMessage()));
//        }
//    }
//
//    /* DELETE */
//    //@PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping(value = "/v1/deleteTEMPLATE/{temPlateId}", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> deleteTEMPLATE(@PathVariable(name = "temPlateId") long temPlateId){
//        try {
//            if (!temPlateService.deleteTEMPLATE(temPlateId)) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("No TEMPLATE found with Id: " + temPlateId);
//            }
//
//            return ResponseEntity.ok().body("Deleted TEMPLATE with Id: " + temPlateId);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(
//                    new ErrorResponse("Error deleting TEMPLATE with Id: " + temPlateId, e.getMessage()));
//        }
//    }
//    /* ================================================ Ver 1 ================================================ */

}