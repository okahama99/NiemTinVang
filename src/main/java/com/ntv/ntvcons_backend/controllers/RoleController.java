package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.role.RoleCreateDTO;
import com.ntv.ntvcons_backend.dtos.role.RoleReadDTO;
import com.ntv.ntvcons_backend.dtos.role.RoleUpdateDTO;
import com.ntv.ntvcons_backend.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyRole('Admin')")
    @PostMapping(value = "/v1/createRole", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createRole(@Valid @RequestBody RoleCreateDTO roleDTO){
        try {
            RoleReadDTO newRoleDTO = roleService.createRoleByDTO(roleDTO);

            return ResponseEntity.ok().body(newRoleDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Role", e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyRole('Admin')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<RoleReadDTO> roleDTOList =
                    roleService.getAllDTO(pageNo, pageSize, sortBy, sortTypeAsc);

            if (roleDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Role found");
            }

            return ResponseEntity.ok().body(roleDTOList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Role", e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.ROLE searchType) {
        // TODO:
        return null;
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_ROLE searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            List<RoleReadDTO> roleDTOList;

            switch (searchType) {
                case BY_NAME_CONTAINS:
                    roleDTOList = roleService.getAllDTOByRoleNameContains(searchParam);

                    if (roleDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Role found with name contains: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Role");
            }

            return ResponseEntity.ok().body(roleDTOList);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for Role with ";

            switch (searchType) {
                case BY_NAME_CONTAINS:
                    errorMsg += "name contains: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyRole('Admin')")
    @PutMapping(value = "/v1/updateRole", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRole(@Valid @RequestBody RoleUpdateDTO roleDTO){
        try {
            RoleReadDTO updatedRoleDTO = roleService.updateRoleByDTO(roleDTO);

            if (updatedRoleDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Role found with Id: '" + roleDTO.getRoleId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedRoleDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Role with Id: '" + roleDTO.getRoleId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyRole('Admin')")
    @DeleteMapping(value = "/v1/deleteRole/{roleId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteRole(@PathVariable(name = "roleId") long roleId){
        try {
            if (!roleService.deleteRole(roleId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Role found with Id: '" + roleId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Role with Id: '" + roleId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Role with Id: '" + roleId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}