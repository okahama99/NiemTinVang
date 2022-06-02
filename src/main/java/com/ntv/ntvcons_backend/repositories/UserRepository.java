package com.ntv.ntvcons_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
        User findByUsername (String name);

        User findAllByUsernameLike (String name);

        User findByPhone (String phone);

        User findByEmail (String email);
}
