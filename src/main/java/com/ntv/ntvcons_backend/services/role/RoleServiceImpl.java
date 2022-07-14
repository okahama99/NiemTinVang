package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.dtos.role.RoleCreateDTO;
import com.ntv.ntvcons_backend.dtos.role.RoleReadDTO;
import com.ntv.ntvcons_backend.dtos.role.RoleUpdateDTO;
import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.repositories.RoleRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    /* CREATE */
    @Override
    public Role createRole(Role newRole) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!userService.existsById(newRole.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newRole.getCreatedBy()
                    + "'. Which violate constraint: FK_Role_User_CreatedBy. ";
        }
        
        /* Check duplicate */
        if (roleRepository.existsByRoleNameAndIsDeletedIsFalse(newRole.getRoleName())) {
            errorMsg += "Already exists another Role with name: '" + newRole.getRoleName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return roleRepository.saveAndFlush(newRole);
    }
    @Override
    public RoleReadDTO createRoleByDTO(RoleCreateDTO newRoleDTO) throws Exception {
        Role newRole = modelMapper.map(newRoleDTO, Role.class);

        newRole = createRole(newRole);

        return modelMapper.map(newRole, RoleReadDTO.class);
    }

    /* READ */
    @Override
    public Page<Role> getPageAll(Pageable paging) throws Exception {
       Page<Role> rolePage = roleRepository.findAllByIsDeletedIsFalse(paging);

        if (rolePage.isEmpty())
            return null;

        return rolePage;
    }
    @Override
    public List<RoleReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<Role> rolePage = getPageAll(paging);

        if (rolePage == null)
            return null;

        List<Role> roleList = rolePage.getContent();

        if (roleList.isEmpty())
            return null;

        return fillAllDTO(roleList, rolePage.getTotalPages());
    }

    @Override
    public boolean existsById(long roleId) throws Exception {
        return roleRepository.existsByRoleIdAndIsDeletedIsFalse(roleId);
    }
    @Override
    public Role getById(long roleId) throws Exception {
        return roleRepository
                .findByRoleIdAndIsDeletedIsFalse(roleId)
                .orElse(null);
    }
    @Override
    public RoleReadDTO getDTOById(long roleId) throws Exception {
        Role role = getById(roleId);

        if (role == null) 
            return null;

        return fillDTO(role);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> roleIdCollection) throws Exception {
        return roleRepository.existsAllByRoleIdInAndIsDeletedIsFalse(roleIdCollection);
    }
    @Override
    public List<Role> getAllByIdIn(Collection<Long> roleIdCollection) throws Exception {
        List<Role> roleList = 
                roleRepository.findAllByRoleIdInAndIsDeletedIsFalse(roleIdCollection);

        if (roleList.isEmpty()) 
            return null;

        return roleList;
    }
    @Override
    public List<RoleReadDTO> getAllDTOByIdIn(Collection<Long> roleIdCollection) throws Exception {
        List<Role> roleList = getAllByIdIn(roleIdCollection);

        if (roleList == null) 
            return null;

        return fillAllDTO(roleList, null);
    }
    @Override
    public Map<Long, RoleReadDTO> mapRoleIdRoleDTOByIdIn(Collection<Long> roleIdCollection) throws Exception {
        List<RoleReadDTO> roleDTOList = getAllDTOByIdIn(roleIdCollection);
        if (roleDTOList == null) 
            return new HashMap<>();

        return roleDTOList.stream()
                .collect(Collectors.toMap(RoleReadDTO::getRoleId, Function.identity()));
    }

    @Override
    public Role getByRoleName(String roleName) throws Exception {
        return roleRepository
                .findByRoleNameAndIsDeletedIsFalse(roleName)
                .orElse(null);
    }
    @Override
    public RoleReadDTO getDTOByRoleName(String roleName) throws Exception {
        Role role = getByRoleName(roleName);

        if (role == null)
            return null;

        return fillDTO(role);
    }
    
    @Override
    public List<Role> getAllByRoleNameContains(String roleName) throws Exception {
        List<Role> roleList =
                roleRepository.findAllByRoleNameContainsAndIsDeletedIsFalse(roleName);

        if (roleList.isEmpty()) 
            return null;

        return roleList;
    }
    @Override
    public List<RoleReadDTO> getAllDTOByRoleNameContains(String roleName) throws Exception {
        List<Role> roleList = getAllByRoleNameContains(roleName);

        if (roleList == null) 
            return null;

        return fillAllDTO(roleList, null);
    }
    @Override
    public Page<Role> getPageAllByRoleNameContains(Pageable paging, String roleName) throws Exception {
        Page<Role> rolePage =
                roleRepository.findAllByRoleNameContainsAndIsDeletedIsFalse(roleName, paging);

        if (rolePage.isEmpty())
            return null;

        return rolePage;
    }
    @Override
    public List<RoleReadDTO> getAllDTOInPagingByRoleNameContains(Pageable paging, String roleName) throws Exception {
        Page<Role> rolePage = getPageAllByRoleNameContains(paging, roleName);

        if (rolePage == null)
            return null;

        List<Role> roleList = rolePage.getContent();

        if (roleList.isEmpty())
            return null;

        return fillAllDTO(roleList, rolePage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public Role updateRole(Role updatedRole) throws Exception {
        Role oldRole = getById(updatedRole.getRoleId());

        if (oldRole == null)
            return null; /* Not found by Id, return null */

        String errorMsg = "";

        /* Check FK */
        if (oldRole.getUpdatedBy() != null)  {
            if (!oldRole.getUpdatedBy().equals(updatedRole.getUpdatedBy())) {
                if (!userService.existsById(updatedRole.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedRole.getUpdatedBy()
                            + "'. Which violate constraint: FK_Role_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedRole.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedRole.getUpdatedBy()
                        + "'. Which violate constraint: FK_Role_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (roleRepository
                .existsByRoleNameAndRoleIdIsNotAndIsDeletedIsFalse(
                        updatedRole.getRoleName(),
                        updatedRole.getRoleId())) {
            errorMsg += "Already exists another Role with name: '" + updatedRole.getRoleName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedRole.setCreatedAt(oldRole.getCreatedAt());
        updatedRole.setCreatedBy(oldRole.getCreatedBy());

        return roleRepository.saveAndFlush(updatedRole);
    }
    @Override
    public RoleReadDTO updateRoleByDTO(RoleUpdateDTO updatedRoleDTO) throws Exception {
        Role updatedRole = modelMapper.map(updatedRoleDTO, Role.class);

        updatedRole = updateRole(updatedRole);

        if (updatedRole == null) 
            return null;

        return fillDTO(updatedRole);
    }

    /* DELETE */
    @Override
    public boolean deleteRole(long roleId) throws Exception {
        Role role = getById(roleId);

        if (role == null) {
            return false;
            /* Not found with Id */
        }

        role.setIsDeleted(true);
        roleRepository.saveAndFlush(role);

        return true;
    }

    /* Utils */
    private RoleReadDTO fillDTO(Role role) throws Exception {
        return modelMapper.map(role, RoleReadDTO.class);
    }

    private List<RoleReadDTO> fillAllDTO(List<Role> roleList, Integer totalPage) throws Exception {
        return roleList.stream()
                .map(role -> {
                    RoleReadDTO roleDTO =
                            modelMapper.map(role, RoleReadDTO.class);

                    roleDTO.setTotalPage(totalPage);

                    return roleDTO;})
                .collect(Collectors.toList());
    }
}
