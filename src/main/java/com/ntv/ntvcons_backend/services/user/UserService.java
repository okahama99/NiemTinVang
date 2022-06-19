package com.ntv.ntvcons_backend.services.user;

import com.ntv.ntvcons_backend.dtos.user.UserCreateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserUpdateDTO;
import com.ntv.ntvcons_backend.entities.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    /* CREATE */
    User createUser(User newUser) throws Exception;
    UserReadDTO createUserByDTO(UserCreateDTO newUserDTO) throws Exception;

    /* READ */
    List<User> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<UserReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    boolean existsById(long userId) throws Exception;
    User getById(long userId) throws Exception;
    UserReadDTO getDTOById(long userId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> userIdCollection) throws Exception;
    List<User> getAllByIdIn(Collection<Long> userIdCollection) throws Exception;
    List<UserReadDTO> getAllDTOByIdIn(Collection<Long> userIdCollection) throws Exception;

    /* UPDATE */
    User updateUser(User updatedUser) throws Exception;
    UserReadDTO updateUserByDTO(UserUpdateDTO updatedUserDTO) throws Exception;

    /* DELETE */
    boolean deletedUser(long userId) throws Exception;
}
