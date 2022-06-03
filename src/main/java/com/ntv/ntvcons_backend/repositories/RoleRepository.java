package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Request;
import com.ntv.ntvcons_backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
        List<Role> findAllByIsDeletedFalse();


        /* Id */
        Optional<Role> findByRoleIdAndIsDeletedIsFalse(int roleId);
        List<Role> findAllByRoleIdInAndIsDeletedIsFalse(Collection<Integer> roleIdCollection);


        /* roleName */
        Optional<Role> findByRoleNameAndIsDeletedIsFalse(String roleName);
        List<Role> findAllByRoleNameLikeAndIsDeletedIsFalse(String roleName);
}
