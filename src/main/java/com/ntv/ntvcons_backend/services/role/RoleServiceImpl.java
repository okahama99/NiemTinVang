package com.ntv.ntvcons_backend.services.role;

import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleRepository roleRepository;

    /* CREATE */
    @Override
    public Role createRole(int roleID, String roleName) {
        return null;
    }

    /* READ */
    @Override
    public List<Role> getAllRole() {
        return null;
    }

    @Override
    public List<Role> getRoleByRoleName(String roleName) {
        return null;
    }

    /* UPDATE */
    @Override
    public boolean updateRole(int roleID, String roleName) {
        return false;
    }

    /* DELETE */
    @Override
    public boolean deleteRole(int roleID) {
        return false;
    }
}
