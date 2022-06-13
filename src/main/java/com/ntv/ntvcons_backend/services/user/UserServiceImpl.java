package com.ntv.ntvcons_backend.services.user;

import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.entities.UserModels.UserFCMToken;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;
    /* CREATE */

    /* READ */

    /* UPDATE */

    /* DELETE */


    @Override
    public void updateUserFcmToken(UserFCMToken userFCMToken) {
        User user = userRepository.getById(userFCMToken.getUserid());
        user.setFcmToken(userFCMToken.getFcmToken());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUserFcmToken(Long userid) {
        User user = userRepository.getById(userid);
        user.setFcmToken(null);
        userRepository.saveAndFlush(user);
    }
}
