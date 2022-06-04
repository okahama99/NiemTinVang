package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
        Role findByRoleNameLike (String roleName);
}
