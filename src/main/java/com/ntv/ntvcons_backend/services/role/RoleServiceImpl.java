package com.ntv.ntvcons_backend.services.role;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.dtos.role.RoleDTO;
import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.entities.RoleModels.ShowRoleModel;
import com.ntv.ntvcons_backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleRepository roleRepository;

    /* CREATE */
    @Override
    public Role createRole(Role newRole) {

        return roleRepository.saveAndFlush(newRole);
    }
    @Override
    public RoleDTO createRoleByDTO(RoleDTO newRoleDTO) {
//        Role role = new Role();
////        role.setName(inputtedRole);
////        role.setStatus(Status.ACTIVE);
////        roleRepository.saveAndFlush(role);
//        return role;
        return null;
    }

    /* READ */
    @Override
    public List<Role> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) {
//        Pageable paging;
//        if(sortType) {
//            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
//        }else{
//            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
//        }
//        Page<Role> pagingResult = roleRepository.findAllByIsDeletedIsFalse(paging);
//        if(pagingResult.hasContent()){
//            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);
//            Page<ShowRoleModel> modelResult = pagingResult.map(new Converter<Role, ShowRoleModel>() {
//                @Override
//                protected ShowRoleModel doForward(Role role) {
//                    ShowRoleModel model = new ShowRoleModel();
//                    //TODO
//                    return model;
//                }
//
//                @Override
//                protected Role doBackward(ShowRoleModel showRoleModel) {
//                    return null;
//                }
//            });
//            return modelResult.getContent();
//        }else{
//            return new ArrayList<ShowRoleModel>();
//        }
        return null;
    }
    @Override
    public List<RoleDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) {
        return null;
    }

    @Override
    public List<Role> getAllByRoleNameLike(String roleName) {
        return null;
    }
    @Override
    public List<RoleDTO> getAllDTOByRoleNameLike(String roleName) {
        return null;
    }

    @Override
    public Role getById(long roleId) {
        return null;
    }
    @Override
    public RoleDTO getDTOById(long roleId) {
        return null;
    }

    @Override
    public Role getAllByIdIn(Collection<Long> roleIdCollection) {
        return null;
    }
    @Override
    public RoleDTO getAllDTOByIdIn(Collection<Long> roleIdCollection) {
        return null;
    }

    /* UPDATE */
    @Override
    public Role updateRole(Role updatedRole) {
//        Role result = roleRepository.findById(roleID).get();
//        result.setName(role.getName());
//        result.setStatus(role.getStatus());
//        roleRepository.saveAndFlush(result);
//        if(result != null)
//        {
//            return true;
//        }
        return null;
    }
    @Override
    public RoleDTO updateRoleByDTO(RoleDTO updatedRoleDTO) {
        return null;
    }

    /* DELETE */
    @Override
    public boolean deleteRole(long roleID) {
        Role role = roleRepository.findById(roleID).get();
        if(role != null){
//            role.setStatus(Status.INACTIVE);
//            roleRepository.save(role);
            return true;
        }else{
            return false;
        }
    }
}
