package com.ntv.ntvcons_backend.services.role;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.entities.RoleModels.ShowRoleModel;
import com.ntv.ntvcons_backend.repositories.PagingRepositories.RolePagingRepository;
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

    @Autowired
    RolePagingRepository rolePagingRepository;

    @Override
    public Role insertRole(int roleID, String roleName) {
        return null;
    }

    @Override
    public boolean updateRole(int roleID, String roleName) {
        return false;
    }

    @Override
    public boolean deleteRole(int roleID) {
        return false;
    }

    @Override
    public List<ShowRoleModel> getAllRole(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType)
        {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
        Page<Role> pagingResult = rolePagingRepository.findAll(paging);
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
}
