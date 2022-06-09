package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.entities.RoleModels.ShowRoleModel;
import com.ntv.ntvcons_backend.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Role")
public class RoleController {
    @Autowired
    RoleService roleService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/insertRole", produces = "application/json;charset=UTF-8")
    public HttpStatus insertRole(@RequestBody int roleID,
                                 @RequestBody String roleName){

//        Role result = roleService.createRole(roleID, roleName);
//        if(result!=null){
//            return HttpStatus.OK;
//        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/updateRole", produces = "application/json;charset=UTF-8")
    public HttpStatus updateRole(@RequestBody int roleID,
                                 @RequestBody String roleName){
//        boolean result = roleService.updateRole(roleID, roleName);
//        if(result){
//            return HttpStatus.OK;
//        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowRoleModel> getAll(@RequestBody int pageNo,
                               @RequestBody int pageSize,
                               @RequestBody String sortBy,
                               @RequestBody boolean sortType) {
//        List<ShowRoleModel> roles = roleService.getAllRole(pageNo, pageSize, sortBy, sortType);
//        return roles;
        return null;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteRole/{roleId}", produces = "application/json;charset=UTF-8")
    public HttpStatus deleteRole(@PathVariable(name = "roleId") int roleId){
        if(roleService.deleteRole(roleId)) {
            return HttpStatus.OK;
        } else{
            return HttpStatus.BAD_REQUEST;
        }
    }
}
