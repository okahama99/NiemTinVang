package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.dtos.role.RoleDTO;
import com.ntv.ntvcons_backend.entities.Role;

import java.util.Collection;
import java.util.List;

public interface RoleService {
    /* CREATE */
    Role createRole(Role newRole) throws Exception;
    RoleDTO createRoleByDTO(RoleDTO newRoleDTO) throws Exception;

    /* READ */
    List<Role> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<RoleDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    List<Role> getAllByRoleNameLike(String roleName) throws Exception;
    List<RoleDTO> getAllDTOByRoleNameLike(String roleName) throws Exception;

    Role getById(long roleId) throws Exception;
    RoleDTO getDTOById(long roleId) throws Exception;

    List<Role> getAllByIdIn(Collection<Long> roleIdCollection) throws Exception;
    List<RoleDTO> getAllDTOByIdIn(Collection<Long> roleIdCollection) throws Exception;

    /* UPDATE */
    Role updateRole(Role updatedRole) throws Exception;
    RoleDTO updateRoleByDTO(RoleDTO updatedRoleDTO) throws Exception;

    /* DELETE */
    boolean deleteRole(long roleId) throws Exception;
}
