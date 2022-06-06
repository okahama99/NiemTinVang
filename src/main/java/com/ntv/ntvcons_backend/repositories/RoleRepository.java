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
        Optional<Role> findByRoleIdAndIsDeletedIsFalse(long roleId);
        List<Role> findAllByRoleIdInAndIsDeletedIsFalse(Collection<Long> roleIdCollection);


        /* roleName */
        Optional<Role> findByRoleNameAndIsDeletedIsFalse(String roleName);
        List<Role> findAllByRoleNameLikeAndIsDeletedIsFalse(String roleName);
}
