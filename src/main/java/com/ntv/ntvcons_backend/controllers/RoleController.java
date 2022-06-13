package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.role.RoleDTO;
import com.ntv.ntvcons_backend.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/v1/create", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> insertRole(@RequestBody RoleDTO roleDTO){
        try {
            RoleDTO newRoleDTO = roleService.createRoleByDTO(roleDTO);

            return ResponseEntity.ok().body(newRoleDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Role", e.getMessage()));
        }
    }

    /* READ */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/v1/read/all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<RoleDTO> roleDTOList = roleService.getAllDTO(pageNo, pageSize, sortBy, sortType);

            if (roleDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Role found");
            }

            return ResponseEntity.ok().body(roleDTOList);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Role", e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/read", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParams(@RequestParam String roleName) {
        try {
            List<RoleDTO> roleDTOList = roleService.getAllDTOByRoleNameLike(roleName);

            if (roleDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Role found with name like: " + roleName);
            }

            return ResponseEntity.ok().body(roleDTOList);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Role with name like: " + roleName, e.getMessage()));
        }
    }

    /* UPDATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/update/{roleId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateRole(@PathVariable(name = "roleId") long roleId,
                                             @RequestBody RoleDTO roleDTO){
        if (roleId != roleDTO.getRoleId()) {
            return ResponseEntity.badRequest().body("Mismatch Id");
        }

        try {
            RoleDTO updatedRoleDTO = roleService.updateRoleByDTO(roleDTO);

            if (updatedRoleDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Role found with Id: " + roleId);
            }

            return ResponseEntity.ok().body(updatedRoleDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Role with Id: " + roleId, e.getMessage()));
        }
    }

    /* DELETE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/v1/delete/{roleId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteRole(@PathVariable(name = "roleId") long roleId){
        try {
            if (!roleService.deleteRole(roleId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Role found with Id: " + roleId);
            }

            return ResponseEntity.ok().body("Deleted Role with Id: " + roleId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Role with Id: " + roleId, e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */


}
