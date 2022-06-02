package com.ntv.ntvcons_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
        Role findByRoleNameLike (String roleName);
}
