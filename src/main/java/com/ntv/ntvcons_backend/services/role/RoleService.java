package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.entities.RoleModels.ShowRoleModel;

import java.util.List;

public interface RoleService {
    /* CREATE */
    Role createRole(long roleID, String roleName);

    /* READ */
    List<ShowRoleModel> getAllRole(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Role> getRoleByRoleName(String roleName);

    List<Role> findAllByStatus();

    /* UPDATE */
    boolean updateRole(long roleID, String roleName);

    /* DELETE */
    boolean deleteRole(long roleID);
}
