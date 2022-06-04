package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.entities.Role;

import java.util.List;

public interface RoleService {
    /* CREATE */
    Role createRole(int roleID, String roleName);

    /* READ */
    List<Role> getRoleByRoleName(String roleName);

    List<Role> getAllRole();

    /* UPDATE */
    boolean updateRole(int roleID, String roleName);

    /* DELETE */
    boolean deleteRole(int roleID);
}
