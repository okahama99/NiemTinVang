package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.dtos.role.RoleCreateDTO;
import com.ntv.ntvcons_backend.dtos.role.RoleReadDTO;
import com.ntv.ntvcons_backend.dtos.role.RoleUpdateDTO;
import com.ntv.ntvcons_backend.entities.Role;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RoleService {
    /* CREATE */
    Role createRole(Role newRole) throws Exception;
    RoleReadDTO createRoleByDTO(RoleCreateDTO newRoleDTO) throws Exception;

    /* READ */
    List<Role> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<RoleReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    boolean existsById(long roleId) throws Exception;
    Role getById(long roleId) throws Exception;
    RoleReadDTO getDTOById(long roleId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> roleIdCollection) throws Exception;
    List<Role> getAllByIdIn(Collection<Long> roleIdCollection) throws Exception;
    List<RoleReadDTO> getAllDTOByIdIn(Collection<Long> roleIdCollection) throws Exception;
    Map<Long, Role> mapRoleIdRoleByIdIn(Collection<Long> roleIdCollection) throws Exception;
    Map<Long, RoleReadDTO> mapRoleIdRoleDTOByIdIn(Collection<Long> roleIdCollection) throws Exception;

    List<Role> getAllByRoleNameContains(String roleName) throws Exception;
    List<RoleReadDTO> getAllDTOByRoleNameContains(String roleName) throws Exception;

    /* UPDATE */
    Role updateRole(Role updatedRole) throws Exception;
    RoleReadDTO updateRoleByDTO(RoleUpdateDTO updatedRoleDTO) throws Exception;

    /* DELETE */
    boolean deleteRole(long roleId) throws Exception;
}