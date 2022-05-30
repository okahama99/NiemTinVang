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

    @Override
    public boolean insertRole(int roleID, String roleName) {
        return false;
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
    public List<Role> getAllRole() {
        return null;
    }

    @Override
    public List<Role> getRoleByRoleName(String roleName) {
        return null;
    }
}
