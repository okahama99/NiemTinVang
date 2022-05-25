package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer> {
        User findByUsernameLike (String name);

        User findByPhone (String phone);

        User findByEmail (String email);
}
