package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        List<User> findAllByStatusNotIn(
                Collection<Status> statusCollection);
        Page<User> findAllByStatusNotIn(
                Collection<Status> statusCollection, Pageable paging);

        /* Id */
        boolean existsByUserIdAndStatusNotIn(
                long userId, Collection<Status> statusCollection);
        Optional<User> findByUserIdAndStatusNotIn(
                long userId, Collection<Status> statusCollection);
        boolean existsAllByUserIdInAndStatusNotIn(
                Collection<Long> userIdCollection, Collection<Status> statusCollection);
        List<User> findAllByUserIdInAndStatusNotIn(
                Collection<Long> userIdCollection, Collection<Status> statusCollection);
        /* !Id & (username || phone || email) */
        /** Check duplicate for Update */
        @Query("SELECT case when count(u) > 0 then true else false end " +
                "FROM User u " +
                "WHERE (u.username = ?1 or u.phone = ?2 or u.email = ?3) and u.userId <> ?4 and u.status not in ?5 ")
        boolean existsByUsernameOrPhoneOrEmailAndUserIdIsNotAndStatusNotIn
        (String username, String phone, String email, long userId, Collection<Status> statusCollection);
        /* !Id & (username || phone) */
        /** Check duplicate for Update */
        @Query("SELECT case when count(u) > 0 then true else false end " +
                "FROM User u " +
                "WHERE (u.username = ?1 or u.phone = ?2) and u.userId <> ?3 and u.status not in ?4 ")
        boolean existsByUsernameOrPhoneAndUserIdIsNotAndStatusNotIn(
                String username, String phone, long userId, Collection<Status> statusCollection);


        /* roleId */
        List<User> findAllByRoleIdAndStatusNotIn(
                long roleId, Collection<Status> statusCollection);
        Page<User> findAllByRoleIdAndStatusNotIn(
                long roleId, Collection<Status> statusCollection, Pageable paging);
        List<User> findAllByRoleIdInAndStatusNotIn(
                Collection<Long> roleIdCollection, Collection<Status> statusCollection);
        Page<User> findAllByRoleIdInAndStatusNotIn(
                Collection<Long> roleIdCollection, Collection<Status> statusCollection, Pageable paging);


        /* username */
        Optional<User> findByUsernameAndStatusNotIn(
                String username, Collection<Status> statusCollection);
        List<User> findAllByUsernameContainsAndStatusNotIn(
                String username, Collection<Status> statusCollection);
        Page<User> findAllByUsernameContainsAndStatusNotIn(
                String username, Collection<Status> statusCollection, Pageable paging);


        /* phone */
        Optional<User> findByPhoneAndStatusNotIn(
                String phone, Collection<Status> statusCollection);
        List<User> findAllByPhoneContainsAndStatusNotIn(
                String phone, Collection<Status> statusCollection);
        Page<User> findAllByPhoneContainsAndStatusNotIn(
                String phone, Collection<Status> statusCollection, Pageable paging);


        /* email */
        Optional<User> findByEmailAndStatusNotIn(
                String email, Collection<Status> statusCollection);
        List<User> findAllByEmailContainsAndStatusNotIn(
                String email, Collection<Status> statusCollection);
        Page<User> findAllByEmailContainsAndStatusNotIn(
                String email, Collection<Status> statusCollection, Pageable paging);
        /* username || phone || email */
        /** Check duplicate for Create */
        @Query("SELECT case when count(u) > 0 then true else false end " +
                "FROM User u " +
                "WHERE (u.username = ?1 or u.phone = ?2 or u.email = ?3) and u.status not in ?4 ")
        boolean existsByUsernameOrPhoneOrEmailAndStatusNotIn(
                String username, String phone, String email, Collection<Status> statusCollection);
        /* username || phone */
        /** Check duplicate for Create */
        @Query("SELECT case when count(u) > 0 then true else false end " +
                "FROM User u " +
                "WHERE (u.username = ?1 or u.phone = ?2) and u.status not in ?3 ")
        boolean existsByUsernameOrPhoneAndStatusNotIn(
                String username, String phone, Collection<Status> statusCollection);
}
