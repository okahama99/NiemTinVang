package com.ntv.ntvcons_backend.services.user;

import com.ntv.ntvcons_backend.dtos.user.UserCreateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserUpdateDTO;
import com.ntv.ntvcons_backend.entities.User;

public interface UserService {
    /* CREATE */
    User createUser(User newUser) throws Exception;
    UserReadDTO createUserByDTO(UserCreateDTO newUserDTO) throws Exception;

    /* READ */
    boolean existsById(long userId) throws Exception;
    User getById(long userId) throws Exception;
    UserReadDTO getDTOById(long userId) throws Exception;

    /* UPDATE */
    User updateUser(User updatedUser) throws Exception;
    UserReadDTO updateUserByDTO(UserUpdateDTO updatedUserDTO) throws Exception;

    /* DELETE */
    boolean deletedUser(long userId) throws Exception;
}
