package com.ntv.ntvcons_backend.services.user;

import com.ntv.ntvcons_backend.dtos.user.UserCreateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserUpdateDTO;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserService extends BaseService {
    /* CREATE */
    User createUser(User newUser) throws Exception;
    UserReadDTO createUserByDTO(UserCreateDTO newUserDTO) throws Exception;

    /* READ */
    Page<User> getPageAll(Pageable paging) throws Exception;
    List<UserReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    boolean existsById(long userId) throws Exception;
    User getById(long userId) throws Exception;
    UserReadDTO getDTOById(long userId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> userIdCollection) throws Exception;
    List<User> getAllByIdIn(Collection<Long> userIdCollection) throws Exception;
    List<UserReadDTO> getAllDTOByIdIn(Collection<Long> userIdCollection) throws Exception;
    Map<Long, UserReadDTO> mapUserIdUserDTOByIdIn(Collection<Long> userIdCollection) throws Exception;

    List<User> getAllByRoleId(long roleId) throws Exception;
    List<UserReadDTO> getAllDTOByRoleId(long roleId) throws Exception;
    Page<User> getPageAllByRoleId(Pageable paging, long roleId) throws Exception;
    List<UserReadDTO> getAllDTOInPagingByRoleId(Pageable paging, long roleId) throws Exception;

    User getByUsername(String username) throws Exception;
    UserReadDTO getDTOByUsername(String username) throws Exception;

    List<User> getAllByUsernameContains(String username) throws Exception;
    List<UserReadDTO> getAllDTOByUsernameContains(String username) throws Exception;
    Page<User> getPageAllByUsernameContains(Pageable paging, String username) throws Exception;
    List<UserReadDTO> getAllDTOInPagingByUsernameContains(Pageable paging, String username) throws Exception;

    User getByPhone(String phone) throws Exception;
    UserReadDTO getDTOByPhone(String phone) throws Exception;

    List<User> getAllByPhoneContains(String phone) throws Exception;
    List<UserReadDTO> getAllDTOByPhoneContains(String phone) throws Exception;
    Page<User> getPageAllByPhoneContains(Pageable paging, String phone) throws Exception;
    List<UserReadDTO> getAllDTOInPagingByPhoneContains(Pageable paging, String phone) throws Exception;

    User getByEmail(String email) throws Exception;
    UserReadDTO getDTOByEmail(String email) throws Exception;

    List<User> getAllByEmailContains(String email) throws Exception;
    List<UserReadDTO> getAllDTOByEmailContains(String email) throws Exception;
    Page<User> getPageAllByEmailContains(Pageable paging, String email) throws Exception;
    List<UserReadDTO> getAllDTOInPagingByEmailContains(Pageable paging, String email) throws Exception;

    /* UPDATE */
    User updateUser(User updatedUser) throws Exception;
    UserReadDTO updateUserByDTO(UserUpdateDTO updatedUserDTO) throws Exception;

    /* DELETE */
    boolean deleteUser(long userId) throws Exception;
}
