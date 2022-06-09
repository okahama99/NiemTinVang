package com.ntv.ntvcons_backend.services.role;

import com.google.common.base.Converter;
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
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleRepository roleRepository;

    /* CREATE */
    @Override
    public Role createRole(long roleID, String roleName) {
        Role role = new Role();
//        role.setName(inputtedRole);
//        role.setStatus(Status.ACTIVE);
//        roleRepository.saveAndFlush(role);
        return role;
    }

    /* READ */
    @Override
    public List<ShowRoleModel> getAllRole(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
        Page<Role> pagingResult = roleRepository.findAllByIsDeletedIsFalse(paging);
        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);
            Page<ShowRoleModel> modelResult = pagingResult.map(new Converter<Role, ShowRoleModel>() {
                @Override
                protected ShowRoleModel doForward(Role role) {
                    ShowRoleModel model = new ShowRoleModel();
                    //TODO
                    return model;
                }

                @Override
                protected Role doBackward(ShowRoleModel showRoleModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ShowRoleModel>();
        }
    }

    @Override
    public List<Role> getRoleByRoleName(String roleName) {
        return null;
    }

    @Override
    public List<Role> findAllByStatus() {
        List<Role> tempForComment = new ArrayList<>();
//        List<Role> role = roleRepository. findByStatus(Status.ACTIVE);
//        List<RoleModel> roleModels = new ArrayList<>();
//        for (Role roles : role) {
//            RoleModel model = new RoleModel();
//            model.setId(roles.getId());
//            model.setName(roles.getName());
//            model.setStatus(roles.getStatus());
//            roleModels.add(model);
//        }
        return tempForComment;
    }

    /* UPDATE */
    public boolean updateRole(long roleID, String roleName) {
        Role result = roleRepository.findById(roleID).get();
//        result.setName(role.getName());
//        result.setStatus(role.getStatus());
//        roleRepository.saveAndFlush(result);
//        if(result != null)
//        {
//            return true;
//        }
        return false;
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
