package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.dtos.role.*;
import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;

    /* CREATE */
    @Override
    public Role createRole(Role newRole) throws Exception {
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
    public List<Role> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Role> rolePage = roleRepository.findAllByIsDeletedIsFalse(paging);

        if (rolePage.isEmpty()) {
            return null;
        }

        return rolePage.getContent();
    }
    @Override
    public List<RoleReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        List<Role> roleList = getAll(pageNo, pageSize, sortBy, sortType);

        if (roleList != null && !roleList.isEmpty()) {
            int totalPage = (int) Math.ceil((double) roleList.size() / pageSize);

            return roleList.stream()
                    .map(role -> {
                        RoleReadDTO roleReadDTO =
                                modelMapper.map(role, RoleReadDTO.class);
                        roleReadDTO.setTotalPage(totalPage);
                        return roleReadDTO;})
                    .collect(Collectors.toList());

        } else {
            return null;
        }
    }

    @Override
    public Role getById(long roleId) throws Exception {
        Optional<Role> role = roleRepository.findByRoleIdAndIsDeletedIsFalse(roleId);

        return role.orElse(null);
    }
    @Override
    public RoleReadDTO getDTOById(long roleId) throws Exception {
        Role role = getById(roleId);

        if (role == null) {
            return null;
        }

        return modelMapper.map(role, RoleReadDTO.class);
    }

    @Override
    public List<Role> getAllByIdIn(Collection<Long> roleIdCollection) throws Exception {
        List<Role> roleList = roleRepository.findAllByRoleIdInAndIsDeletedIsFalse(roleIdCollection);

        if (roleList.isEmpty()) {
            return null;
        }

        return roleList;
    }
    @Override
    public List<RoleReadDTO> getAllDTOByIdIn(Collection<Long> roleIdCollection) throws Exception {
        List<Role> roleList = getAllByIdIn(roleIdCollection);

        if (roleList == null) {
            return null;
        }

        return roleList.stream().map(role -> modelMapper.map(role, RoleReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> getAllByRoleNameContains(String roleName) throws Exception {
        List<Role> roleList = roleRepository.findAllByRoleNameContainsAndIsDeletedIsFalse(roleName);

        if (roleList.isEmpty()) {
            return null;
        }

        return roleList;
    }
    @Override
    public List<RoleReadDTO> getAllDTOByRoleNameContains(String roleName) throws Exception {
        List<Role> roleList = getAllByRoleNameContains(roleName);

        if (roleList == null) {
            return null;
        }

        return roleList.stream()
                .map(role -> modelMapper.map(role, RoleReadDTO.class))
                .collect(Collectors.toList());
    }

    /* UPDATE */
    @Override
    public Role updateRole(Role updatedRole) throws Exception {
        Optional<Role> role = roleRepository.findByRoleIdAndIsDeletedIsFalse(updatedRole.getRoleId());

        if (!role.isPresent()) {
            return null;
            /* Not found by Id, return null */
        }

        return roleRepository.saveAndFlush(updatedRole);
    }
    @Override
    public RoleReadDTO updateRoleByDTO(RoleUpdateDTO updatedRoleDTO) throws Exception {
        Role updatedRole = modelMapper.map(updatedRoleDTO, Role.class);

        updatedRole = updateRole(updatedRole);

        if (updatedRole == null) {
            return null;
        }

        return modelMapper.map(updatedRole, RoleReadDTO.class);
    }

    /* DELETE */
    @Override
    public boolean deleteRole(long roleId) throws Exception {
        Optional<Role> role = roleRepository.findByRoleIdAndIsDeletedIsFalse(roleId);

        if (!role.isPresent()) {
            return false;
            /* Not found with Id */
        }

        role.get().setIsDeleted(true);
        roleRepository.saveAndFlush(role.get());

        return true;
    }
}
