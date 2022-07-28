package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
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
        Page<Role> findAllByStatusNotIn
                (Collection<Status> statusCollection, Pageable paging);


        /* Id */
        boolean existsByRoleIdAndStatusNotIn
                (long roleId, Collection<Status> statusCollection);
        Optional<Role> findByRoleIdAndStatusNotIn
                (long roleId, Collection<Status> statusCollection);
        boolean existsAllByRoleIdInAndStatusNotIn
                (Collection<Long> roleIdCollection, Collection<Status> statusCollection);
        List<Role> findAllByRoleIdInAndStatusNotIn
                (Collection<Long> roleIdCollection, Collection<Status> statusCollection);
        /* Id & roleName */
        /** Check duplicate roleName for update */
        boolean existsByRoleNameAndRoleIdIsNotAndStatusNotIn
                (String roleName, long roleId, Collection<Status> statusCollection);

        /* roleName */
        boolean existsByRoleNameAndStatusNotIn
                (String roleName, Collection<Status> statusCollection);
        Optional<Role> findByRoleNameAndStatusNotIn
                (String roleName, Collection<Status> statusCollection);
        List<Role> findAllByRoleNameContainsAndStatusNotIn
                (String roleName, Collection<Status> statusCollection);
        Page<Role> findAllByRoleNameContainsAndStatusNotIn
                (String roleName, Collection<Status> statusCollection, Pageable paging);
}
