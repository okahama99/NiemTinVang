package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Integer> {
        Role findByRoleNameLike ( String roleName);
}
