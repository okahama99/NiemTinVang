package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.dtos.role.RoleDTO;
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
    public RoleDTO createRoleByDTO(RoleDTO newRoleDTO) throws Exception {
        Role newRole = modelMapper.map(newRoleDTO, Role.class);

        newRole = createRole(newRole);

        return modelMapper.map(newRole, RoleDTO.class);
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

        if (!rolePage.hasContent()) {
            return null;
        }

        return rolePage.getContent();
    }
    @Override
    public List<RoleDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        List<Role> roleList = getAll(pageNo, pageSize, sortBy, sortType);

        if (roleList != null && !roleList.isEmpty()) {
//            double totalPage = Math.ceil((double) roleList.size() / pageSize);

            return roleList.stream().map(role -> modelMapper.map(role, RoleDTO.class))
                    .collect(Collectors.toList());

        } else {
            return null;
        }
    }

    @Override
    public List<Role> getAllByRoleNameLike(String roleName) throws Exception {
        List<Role> roleList = roleRepository.findAllByRoleNameLikeAndIsDeletedIsFalse(roleName);

        if (roleList.isEmpty()) {
            return null;
        }

        return roleList;
    }
    @Override
    public List<RoleDTO> getAllDTOByRoleNameLike(String roleName) throws Exception {
        List<Role> roleList = getAllByRoleNameLike(roleName);

        if (roleList == null) {
            return null;
        }

        return roleList.stream().map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Role getById(long roleId) throws Exception {
        Optional<Role> role = roleRepository.findByRoleIdAndIsDeletedIsFalse(roleId);

        return role.orElse(null);
    }
    @Override
    public RoleDTO getDTOById(long roleId) throws Exception {
        Role role = getById(roleId);

        if (role == null) {
            return null;
        }

        return modelMapper.map(role, RoleDTO.class);
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
    public List<RoleDTO> getAllDTOByIdIn(Collection<Long> roleIdCollection) throws Exception {
        List<Role> roleList = getAllByIdIn(roleIdCollection);

        if (roleList == null) {
            return null;
        }

        return roleList.stream().map(role -> modelMapper.map(role, RoleDTO.class))
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

        return roleRepository.save(updatedRole);
    }
    @Override
    public RoleDTO updateRoleByDTO(RoleDTO updatedRoleDTO) throws Exception {
        Role updatedRole = modelMapper.map(updatedRoleDTO, Role.class);

        updatedRole = updateRole(updatedRole);

        if (updatedRole == null) {
            return null;
        }

        return modelMapper.map(updatedRole, RoleDTO.class);
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
