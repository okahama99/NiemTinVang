package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
        Page<Role> findAllByIsDeletedIsFalse(Pageable pageable);


        /* Id */
        boolean existsByRoleIdAndIsDeletedIsFalse(long roleId);
        Optional<Role> findByRoleIdAndIsDeletedIsFalse(long roleId);
        boolean existsAllByRoleIdInAndIsDeletedIsFalse(Collection<Long> roleIdCollection);
        List<Role> findAllByRoleIdInAndIsDeletedIsFalse(Collection<Long> roleIdCollection);
        /* Id & roleName */
        /** Check duplicate roleName for update */
        boolean existsByRoleNameAndRoleIdIsNotAndIsDeletedIsFalse(String roleName, long roleId);

        /* roleName */
        boolean existsByRoleNameAndIsDeletedIsFalse(String roleName);
        Optional<Role> findByRoleNameAndIsDeletedIsFalse(String roleName);
        List<Role> findAllByRoleNameContainsAndIsDeletedIsFalse(String roleName);
}
