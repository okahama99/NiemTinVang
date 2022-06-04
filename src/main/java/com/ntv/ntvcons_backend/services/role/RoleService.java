package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.entities.RoleModels.ShowRoleModel;

import java.util.List;

public interface RoleService {
    Role insertRole (int roleID, String roleName);

    boolean updateRole(int roleID, String roleName);

    boolean deleteRole(int roleID);

    List<ShowRoleModel> getAllRole(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Role> getRoleByRoleName(String roleName);
}
