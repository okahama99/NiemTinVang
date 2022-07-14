package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        Page<User> findAllByIsDeletedIsFalse(Pageable paging);

        List<User> findByIsDeletedFalse();

        /* Id */
        boolean existsByUserIdAndIsDeletedIsFalse(long userId);
        Optional<User> findByUserIdAndIsDeletedIsFalse(long userId);
        boolean existsAllByUserIdInAndIsDeletedIsFalse(Collection<Long> userIdCollection);
        List<User> findAllByUserIdInAndIsDeletedIsFalse(Collection<Long> userIdCollection);
        /* username & phone & email */
        boolean existsByUsernameOrPhoneOrEmailAndUserIdIsNotAndIsDeletedIsFalse
                (String username, String phone, String email, long userId);


        /* roleId */
        List<User> findAllByRoleIdAndIsDeletedIsFalse(long roleId);
        List<User> findAllByRoleIdInAndIsDeletedIsFalse(Collection<Long> roleIdCollection);


        /* username */
        Optional<User> findByUsernameAndIsDeletedIsFalse(String username);
        List<User> findByUsernameContainsAndIsDeletedIsFalse(String username);


        /* phone */
        Optional<User> findByPhoneAndIsDeletedIsFalse(String phone);
        List<User> findByPhoneContainsAndIsDeletedIsFalse(String phone);


        /* email */
        Optional<User> findByEmailAndIsDeletedIsFalse(String email);
        List<User> findByEmailContainsAndIsDeletedIsFalse(String email);
        /* username || phone || email */
        boolean existsByUsernameOrPhoneOrEmailAndIsDeletedIsFalse(String username, String phone, String email);
}
