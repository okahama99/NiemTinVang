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
        Page<User> findAllByIsDeletedIsFalse(Pageable pageable);


        /* Id */
        Optional<User> findByUserIdAndIsDeletedIsFalse(long userId);
        List<User> findAllByUserIdInAndIsDeletedIsFalse(Collection<Long> userIdCollection);


        /* username */
        Optional<User> findByUsernameAndIsDeletedIsFalse(String username);
        List<User> findByUsernameContainsAndIsDeletedIsFalse(String username);


        /* username */
        Optional<User> findByPhoneAndIsDeletedIsFalse(String phone);
        List<User> findByPhoneContainsAndIsDeletedIsFalse(String phone);


        /* username */
        Optional<User> findByEmailAndIsDeletedIsFalse(String email);
        List<User> findByEmailContainsAndIsDeletedIsFalse(String email);
}
