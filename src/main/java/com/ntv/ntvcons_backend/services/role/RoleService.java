package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.entities.Role;

import java.util.List;

public interface RoleService {
    boolean insertRole (int roleID, String roleName);

    boolean updateRole(int roleID, String roleName);

    boolean deleteRole(int roleID);

    List<Role> getAllRole();

    List<Role> getRoleByRoleName(String roleName);
}
