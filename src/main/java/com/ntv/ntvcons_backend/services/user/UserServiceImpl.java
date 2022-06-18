package com.ntv.ntvcons_backend.services.user;

import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;



    /* CREATE */

    /* READ */
    @Override
    public boolean existsById(long userId) throws Exception {
        return userRepository.existsByUserIdAndIsDeletedIsFalse(userId);
    }

    /* UPDATE */

    /* DELETE */
    // gọi service delete của projectmanager
}
