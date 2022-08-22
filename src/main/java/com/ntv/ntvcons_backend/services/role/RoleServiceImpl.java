package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.constants.Status;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
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
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public Role createRole(Role newRole) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (newRole.getCreatedBy() != null) {
            if (!userService.existsById(newRole.getCreatedBy())) {
                errorMsg += "No User (CreatedBy) found with Id: '" + newRole.getCreatedBy()
                        + "'. Which violate constraint: FK_Role_User_CreatedBy. ";
            }
        }
        
        /* Check duplicate */
        if (roleRepository
                .existsByRoleNameAndStatusNotIn(
                        newRole.getRoleName(),
                        N_D_S_STATUS_LIST)) {
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
       Page<Role> rolePage = roleRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

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
        return roleRepository
                .existsByRoleIdAndStatusNotIn(roleId, N_D_S_STATUS_LIST);
    }
    @Override
    public Role getById(long roleId) throws Exception {
        return roleRepository
                .findByRoleIdAndStatusNotIn(roleId, N_D_S_STATUS_LIST)
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
        return roleRepository.existsAllByRoleIdInAndStatusNotIn(roleIdCollection, N_D_S_STATUS_LIST);
    }
    @Override
    public List<Role> getAllByIdIn(Collection<Long> roleIdCollection) throws Exception {
        List<Role> roleList = 
                roleRepository.findAllByRoleIdInAndStatusNotIn(roleIdCollection, N_D_S_STATUS_LIST);

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
                .findByRoleNameAndStatusNotIn(roleName, N_D_S_STATUS_LIST)
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
                roleRepository.findAllByRoleNameContainsAndStatusNotIn(roleName, N_D_S_STATUS_LIST);

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
                roleRepository.findAllByRoleNameContainsAndStatusNotIn(roleName, N_D_S_STATUS_LIST, paging);

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
        if (updatedRole.getUpdatedBy() != null) {
            if (oldRole.getUpdatedBy() != null) {
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
        }

        /* Check duplicate */
        if (roleRepository
                .existsByRoleNameAndRoleIdIsNotAndStatusNotIn(
                        updatedRole.getRoleName(),
                        updatedRole.getRoleId(),
                        N_D_S_STATUS_LIST)) {
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

        role.setStatus(Status.DELETED);
        roleRepository.saveAndFlush(role);

        return true;
    }

    /* Utils */
    private RoleReadDTO fillDTO(Role role) throws Exception {
        modelMapper.typeMap(Role.class, RoleReadDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(RoleReadDTO::setCreatedAt);
                    mapper.skip(RoleReadDTO::setUpdatedAt);});

        RoleReadDTO roleDTO = modelMapper.map(role, RoleReadDTO.class);

        if (role.getCreatedAt() != null)
            roleDTO.setCreatedAt(role.getCreatedAt().format(dateTimeFormatter));
        if (role.getUpdatedAt() != null)
            roleDTO.setUpdatedAt(role.getUpdatedAt().format(dateTimeFormatter));

        return roleDTO;
    }

    private List<RoleReadDTO> fillAllDTO(Collection<Role> roleCollection, Integer totalPage) throws Exception {
        modelMapper.typeMap(Role.class, RoleReadDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(RoleReadDTO::setCreatedAt);
                    mapper.skip(RoleReadDTO::setUpdatedAt);});

        return roleCollection.stream()
                .map(role -> {
                    RoleReadDTO roleDTO =
                            modelMapper.map(role, RoleReadDTO.class);

                    if (role.getCreatedAt() != null)
                        roleDTO.setCreatedAt(role.getCreatedAt().format(dateTimeFormatter));
                    if (role.getUpdatedAt() != null)
                        roleDTO.setUpdatedAt(role.getUpdatedAt().format(dateTimeFormatter));

                    roleDTO.setTotalPage(totalPage);

                    return roleDTO;})
                .collect(Collectors.toList());
    }
}
