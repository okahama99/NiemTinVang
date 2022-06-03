package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Task;
import com.ntv.ntvcons_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
        List<User> findAllByIsDeletedFalse();


        /* Id */
        Optional<User> findByUserIdAndIsDeletedIsFalse(int userId);
        List<User> findAllByUserIdInAndIsDeletedIsFalse(Collection<Integer> userIdCollection);


        /* username */
        Optional<User> findByUsernameAndIsDeletedIsFalse(String username);
        List<User> findByUsernameLikeAndIsDeletedIsFalse(String username);


        /* username */
        Optional<User> findByPhoneAndIsDeletedIsFalse(String phone);
        List<User> findByPhoneLikeAndIsDeletedIsFalse(String phone);


        /* username */
        Optional<User> findByEmailAndIsDeletedIsFalse(String email);
        List<User> findByEmailLikeAndIsDeletedIsFalse(String email);
}
