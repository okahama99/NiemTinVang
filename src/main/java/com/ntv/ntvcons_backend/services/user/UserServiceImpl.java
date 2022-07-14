package com.ntv.ntvcons_backend.services.user;

import com.ntv.ntvcons_backend.dtos.role.RoleReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserCreateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserUpdateDTO;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
import com.ntv.ntvcons_backend.services.role.RoleService;
import com.ntv.ntvcons_backend.services.taskAssignment.TaskAssignmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TaskAssignmentService taskAssignmentService;
    @Autowired
    private ProjectManagerService projectManagerService;

    /* CREATE */
    @Override
    public User createUser(User newUser) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!roleService.existsById(newUser.getRoleId())) {
            errorMsg += "No Role found with Id: '" + newUser.getRoleId()
                    + "'. Which violate constraint: FK_User_Role. ";
        }
        if (newUser.getCreatedBy() != null) {
            if (!userRepository.existsByUserIdAndIsDeletedIsFalse(newUser.getCreatedBy())) {
                errorMsg += "No User (CreatedBy) found with Id: '" + newUser.getCreatedBy()
                        + "'. Which violate constraint: FK_User_User_CreatedBy. ";
            }
        }

        /* Check duplicate */
        if (userRepository
                .existsByUsernameOrPhoneOrEmailAndIsDeletedIsFalse(
                        newUser.getUsername(),
                        newUser.getPhone(),
                        newUser.getEmail())) {
            errorMsg += "Already exists another User with username: '" + newUser.getUsername()
                    + "', phone: '" + newUser.getPhone()
                    + "', email: '" + newUser.getEmail() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return userRepository.saveAndFlush(newUser);
    }
    @Override
    public UserReadDTO createUserByDTO(UserCreateDTO newUserDTO) throws Exception {
        User newUser = modelMapper.map(newUserDTO, User.class);

        newUser = createUser(newUser);

        UserReadDTO userDTO = modelMapper.map(newUser, UserReadDTO.class);

        /* Get associated Role */
        userDTO.setRole(roleService.getDTOById(newUser.getRoleId()));

        return userDTO;
    }

    /* READ */
    @Override
    public Page<User> getPageAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<User> userPage = userRepository.findAllByIsDeletedIsFalse(paging);

        if (userPage.isEmpty()) 
            return null;

        return userPage;
    }
    @Override
    public List<UserReadDTO> getAllDTOInPaging(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Page<User> userPage = getPageAll(pageNo, pageSize, sortBy, sortType);

        if (userPage == null) {
             return null;
        }

        List<User> userList = userPage.getContent();

        if (userList.isEmpty()) 
            return null;

        int totalPage = userPage.getTotalPages();

        /* Get associated Role */
        Map<Long, RoleReadDTO> roleIdRoleDTOMap =
                roleService.mapRoleIdRoleDTOByIdIn(
                        userList.stream()
                                .map(User::getRoleId)
                                .collect(Collectors.toSet()));

        return userList.stream()
                .map(user -> {
                    UserReadDTO userReadDTO =
                            modelMapper.map(user, UserReadDTO.class);

                    userReadDTO.setRole(roleIdRoleDTOMap.get(user.getRoleId()));
                    userReadDTO.setTotalPage(totalPage);

                    return userReadDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(long userId) throws Exception {
        return userRepository.existsByUserIdAndIsDeletedIsFalse(userId);
    }
    @Override
    public User getById(long userId) throws Exception {
        return userRepository
                .findByUserIdAndIsDeletedIsFalse(userId)
                .orElse(null);
    }
    @Override
    public UserReadDTO getDTOById(long userId) throws Exception {
        User user = getById(userId);

        if (user == null) {
            return null;
            /* Not found with Id */
        }

        UserReadDTO userDTO = modelMapper.map(user, UserReadDTO.class);

        /* Get associated Role */
        userDTO.setRole(roleService.getDTOById(user.getRoleId()));

        return userDTO;
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> userIdCollection) throws Exception {
        return userRepository.existsAllByUserIdInAndIsDeletedIsFalse(userIdCollection);
    }
    @Override
    public List<User> getAllByIdIn(Collection<Long> userIdCollection) throws Exception {
        List<User> userList = userRepository.findAllByUserIdInAndIsDeletedIsFalse(userIdCollection);

        if (!userList.isEmpty()) 
            return null;

        return userList;
    }
    @Override
    public List<UserReadDTO> getAllDTOByIdIn(Collection<Long> userIdCollection) throws Exception {
        List<User> userList = getAllByIdIn(userIdCollection);

        if (userList == null) 
            return null;

        /* Get associated Role */
        Map<Long, RoleReadDTO> roleIdRoleDTOMap =
                roleService.mapRoleIdRoleDTOByIdIn(
                        userList.stream()
                                .map(User::getRoleId)
                                .collect(Collectors.toSet()));

        return userList.stream()
                .map(user -> {
                    UserReadDTO userDTO = modelMapper.map(user, UserReadDTO.class);

                    userDTO.setRole(roleIdRoleDTOMap.get(user.getRoleId()));

                    return userDTO;})
                .collect(Collectors.toList());
    }
    @Override
    public Map<Long, UserReadDTO> mapUserIdUserDTOByIdIn(Collection<Long> userIdCollection) throws Exception {
        List<UserReadDTO> userDTOList = getAllDTOByIdIn(userIdCollection);

        if (userDTOList == null) 
            return new HashMap<>();

        return userDTOList.stream()
                .collect(Collectors.toMap(UserReadDTO::getUserId, Function.identity()));
    }

    @Override
    public List<User> getAllByRoleId(long roleId) throws Exception {
        List<User> userList = userRepository.findAllByRoleIdAndIsDeletedIsFalse(roleId);

        if (userList.isEmpty()) 
            return null;

        return userList;
    }
    @Override
    public List<UserReadDTO> getAllDTOByRoleId(long roleId) throws Exception {
        List<User> userList = getAllByRoleId(roleId);

        if (userList == null) 
            return null;

        /* Get associated Role */
        RoleReadDTO roleDTO = roleService.getDTOById(roleId);

        return userList.stream()
                .map(user -> {
                    UserReadDTO userDTO = modelMapper.map(user, UserReadDTO.class);

                    userDTO.setRole(roleDTO);

                    return userDTO;})
                .collect(Collectors.toList());
    }

    /* UPDATE */
    @Override
    public User updateUser(User updatedUser) throws Exception {
        User oldUser = getById(updatedUser.getUserId());

        if (oldUser == null) 
            return null;

        String errorMsg = "";

        /* Check FK (if changed) */
        if (!oldUser.getRoleId().equals(updatedUser.getRoleId())) {
            if (!roleService.existsById(updatedUser.getRoleId())) {
                errorMsg += "No Role found with Id: '" + updatedUser.getRoleId()
                        + "'. Which violate constraint: FK_User_Role. ";
            }
        }
        if (oldUser.getUpdatedBy() != null) {
            if (!oldUser.getUpdatedBy().equals(updatedUser.getUpdatedBy())) {
                if (!userRepository.existsByUserIdAndIsDeletedIsFalse(updatedUser.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedUser.getUpdatedBy()
                            + "'. Which violate constraint: FK_User_User_UpdatedBy. ";
                }
            }
        }

        /* Check duplicate */
        if (userRepository
                .existsByUsernameOrPhoneOrEmailAndUserIdIsNotAndIsDeletedIsFalse(
                        updatedUser.getUsername(),
                        updatedUser.getPhone(),
                        updatedUser.getEmail(),
                        updatedUser.getUserId())) {
            errorMsg += "Already exists another User with username: '" + updatedUser.getUsername()
                    + "', phone: '" + updatedUser.getPhone()
                    + "', email: '" + updatedUser.getEmail() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return userRepository.saveAndFlush(updatedUser);
    }
    @Override
    public UserReadDTO updateUserByDTO(UserUpdateDTO updatedUserDTO) throws Exception {
        User updatedUser = modelMapper.map(updatedUserDTO, User.class);

        updatedUser = updateUser(updatedUser);

        UserReadDTO userDTO = modelMapper.map(updatedUser, UserReadDTO.class);

        /* Get associated Role */
        userDTO.setRole(roleService.getDTOById(updatedUser.getRoleId()));

        return userDTO;
    }

    /* DELETE */
    @Override
    public boolean deleteUser(long userId) throws Exception {
        User user = getById(userId);

        if (user == null) {
            return false;
            /* Not found by Id */
        }

        /* Delete all associated taskAssignment */
        taskAssignmentService.deleteAllByUserId(userId);

        /* Delete all associated projectManager */
        projectManagerService.deleteAllByUserId(userId);

        user.setIsDeleted(true);
        userRepository.saveAndFlush(user);

        return true;
    }
}
