package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.entities.RoleModels.ShowRoleModel;

import java.util.List;

public interface RoleService {
    /* CREATE */
    Role createRole(int roleID, String roleName);

    /* READ */
    List<Role> getRoleByRoleName(String roleName);

    List<ShowRoleModel> getAllRole(int pageNo, int pageSize, String sortBy, boolean sortType);

    /* UPDATE */
    boolean updateRole(int roleID, String roleName);

    /* DELETE */
    boolean deleteRole(int roleID);
}
