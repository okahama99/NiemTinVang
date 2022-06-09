package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.dtos.role.RoleDTO;
import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.entities.RoleModels.ShowRoleModel;

import java.util.Collection;
import java.util.List;

public interface RoleService {
    /* CREATE */
    Role createRole(Role newRole);
    RoleDTO createRoleByDTO(RoleDTO newRoleDTO);

    /* READ */
    List<Role> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);
    List<RoleDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Role> getAllByRoleNameLike(String roleName);
    List<RoleDTO> getAllDTOByRoleNameLike(String roleName);

    Role getById(long roleId);
    RoleDTO getDTOById(long roleId);

    Role getAllByIdIn(Collection<Long> roleIdCollection);
    RoleDTO getAllDTOByIdIn(Collection<Long> roleIdCollection);

    /* UPDATE */
    Role updateRole(Role updatedRole);
    RoleDTO updateRoleByDTO(RoleDTO updatedRoleDTO);

    /* DELETE */
    boolean deleteRole(long roleID);
}
