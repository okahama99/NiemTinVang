package com.ntv.ntvcons_backend.services.user;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.role.RoleReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserCreateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserUpdateDTO;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import com.ntv.ntvcons_backend.services.entityWrapper.EntityWrapperService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
import com.ntv.ntvcons_backend.services.role.RoleService;
import com.ntv.ntvcons_backend.services.taskAssignment.TaskAssignmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityWrapperService entityWrapperService;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;

    private final EntityType ENTITY_TYPE = EntityType.USER_ENTITY;

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
            if (!existsById(newUser.getCreatedBy())) {
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
                    + "', or with phone: '" + newUser.getPhone()
                    + "', or with email: '" + newUser.getEmail() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        if (newUser.getPassword() != null) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        if (newUser.getCreatedBy() == null) { /* Self created */
            newUser = userRepository.saveAndFlush(newUser);

            newUser.setCreatedBy(newUser.getUserId());
        }

        return userRepository.saveAndFlush(newUser);
    }
    @Override
    public UserReadDTO createUserByDTO(UserCreateDTO newUserDTO) throws Exception {
        User newUser = modelMapper.map(newUserDTO, User.class);

        newUser = createUser(newUser);

        /* Create associated EntityWrapper */
        entityWrapperService
                .createEntityWrapper(newUser.getUserId(), ENTITY_TYPE, newUser.getCreatedBy());

        return fillDTO(newUser);
    }

    /* READ */
    @Override
    public Page<User> getPageAll(Pageable paging) throws Exception {
        Page<User> userPage = userRepository.findAllByIsDeletedIsFalse(paging);

        if (userPage.isEmpty())
            return null;

        return userPage;
    }
    @Override
    public List<UserReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<User> userPage = getPageAll(paging);

        if (userPage == null)
            return null;

        List<User> userList = userPage.getContent();

        if (userList.isEmpty())
            return null;

        return fillAllDTO(userList, userPage.getTotalPages());
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

        if (user == null)
            return null; /* Not found with Id */

        return fillDTO(user);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> userIdCollection) throws Exception {
        return userRepository.existsAllByUserIdInAndIsDeletedIsFalse(userIdCollection);
    }
    @Override
    public List<User> getAllByIdIn(Collection<Long> userIdCollection) throws Exception {
        List<User> userList =
                userRepository.findAllByUserIdInAndIsDeletedIsFalse(userIdCollection);

        if (userList.isEmpty())
            return null;

        return userList;
    }
    @Override
    public List<UserReadDTO> getAllDTOByIdIn(Collection<Long> userIdCollection) throws Exception {
        List<User> userList = getAllByIdIn(userIdCollection);

        if (userList == null) 
            return null;

        return fillAllDTO(userList, null);
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

        return fillAllDTO(userList, null);
    }
    @Override
    public Page<User> getPageAllByRoleId(Pageable paging, long roleId) throws Exception {
        Page<User> userPage =
                userRepository.findAllByRoleIdAndIsDeletedIsFalse(roleId, paging);

        if (userPage.isEmpty())
            return null;

        return userPage;
    }
    @Override
    public List<UserReadDTO> getAllDTOInPagingByRoleId(Pageable paging, long roleId) throws Exception {
        Page<User> userPage = getPageAllByRoleId(paging, roleId);

        if (userPage == null)
            return null;

        List<User> userList = userPage.getContent();

        if (userList.isEmpty())
            return null;

        return fillAllDTO(userList, userPage.getTotalPages());
    }

    @Override
    public User getByUsername(String username) throws Exception {
        return userRepository
                .findByUsernameAndIsDeletedIsFalse(username)
                .orElse(null);
    }
    @Override
    public UserReadDTO getDTOByUsername(String username) throws Exception {
        User user = getByUsername(username);

        if (user == null)
            return null;

        return fillDTO(user);
    }

    @Override
    public List<User> getAllByUsernameContains(String username) throws Exception {
        List<User> userList = userRepository.findAllByUsernameContainsAndIsDeletedIsFalse(username);

        if (userList.isEmpty())
            return null;

        return userList;
    }
    @Override
    public List<UserReadDTO> getAllDTOByUsernameContains(String username) throws Exception {
        List<User> userList = getAllByUsernameContains(username);

        if (userList == null)
            return null;

        return fillAllDTO(userList, null);
    }
    @Override
    public Page<User> getPageAllByUsernameContains(Pageable paging, String username) throws Exception {
        Page<User> userPage =
                userRepository.findAllByUsernameContainsAndIsDeletedIsFalse(username, paging);

        if (userPage.isEmpty())
            return null;

        return userPage;
    }
    @Override
    public List<UserReadDTO> getAllDTOInPagingByUsernameContains(Pageable paging, String username) throws Exception {
        Page<User> userPage = getPageAllByUsernameContains(paging, username);

        if (userPage == null)
            return null;

        List<User> userList = userPage.getContent();

        if (userList.isEmpty())
            return null;

        return fillAllDTO(userList, userPage.getTotalPages());
    }

    @Override
    public User getByPhone(String phone) throws Exception {
        return userRepository
                .findByPhoneAndIsDeletedIsFalse(phone)
                .orElse(null);
    }
    @Override
    public UserReadDTO getDTOByPhone(String phone) throws Exception {
        User user = getByPhone(phone);

        if (user == null)
            return null;

        return fillDTO(user);
    }

    @Override
    public List<User> getAllByPhoneContains(String phone) throws Exception {
        List<User> userList = userRepository.findAllByPhoneContainsAndIsDeletedIsFalse(phone);

        if (userList.isEmpty())
            return null;

        return userList;
    }
    @Override
    public List<UserReadDTO> getAllDTOByPhoneContains(String phone) throws Exception {
        List<User> userList = getAllByPhoneContains(phone);

        if (userList == null)
            return null;

        return fillAllDTO(userList, null);
    }
    @Override
    public Page<User> getPageAllByPhoneContains(Pageable paging, String phone) throws Exception {
        Page<User> userPage =
                userRepository.findAllByPhoneContainsAndIsDeletedIsFalse(phone, paging);

        if (userPage.isEmpty())
            return null;

        return userPage;
    }
    @Override
    public List<UserReadDTO> getAllDTOInPagingByPhoneContains(Pageable paging, String phone) throws Exception {
        Page<User> userPage = getPageAllByPhoneContains(paging, phone);

        if (userPage == null)
            return null;

        List<User> userList = userPage.getContent();

        if (userList.isEmpty())
            return null;

        return fillAllDTO(userList, userPage.getTotalPages());
    }

    @Override
    public User getByEmail(String email) throws Exception {
        return userRepository
                .findByEmailAndIsDeletedIsFalse(email)
                .orElse(null);
    }
    @Override
    public UserReadDTO getDTOByEmail(String email) throws Exception {
        User user = getByEmail(email);

        if (user == null)
            return null;

        return fillDTO(user);
    }

    @Override
    public List<User> getAllByEmailContains(String email) throws Exception {
        List<User> userList = userRepository.findAllByEmailContainsAndIsDeletedIsFalse(email);

        if (userList.isEmpty())
            return null;

        return userList;
    }
    @Override
    public List<UserReadDTO> getAllDTOByEmailContains(String email) throws Exception {
        List<User> userList = getAllByEmailContains(email);

        if (userList == null)
            return null;

        return fillAllDTO(userList, null);
    }
    @Override
    public Page<User> getPageAllByEmailContains(Pageable paging, String email) throws Exception {
        Page<User> userPage =
                userRepository.findAllByEmailContainsAndIsDeletedIsFalse(email, paging);

        if (userPage.isEmpty())
            return null;

        return userPage;
    }
    @Override
    public List<UserReadDTO> getAllDTOInPagingByEmailContains(Pageable paging, String email) throws Exception {
        Page<User> userPage = getPageAllByEmailContains(paging, email);

        if (userPage == null)
            return null;

        List<User> userList = userPage.getContent();

        if (userList.isEmpty())
            return null;

        return fillAllDTO(userList, userPage.getTotalPages());
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
                if (!existsById(updatedUser.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedUser.getUpdatedBy()
                            + "'. Which violate constraint: FK_User_User_UpdatedBy. ";
                }
            }
        } else {
            if (!existsById(updatedUser.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedUser.getUpdatedBy()
                        + "'. Which violate constraint: FK_User_User_UpdatedBy. ";
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
                    + "', or with phone: '" + updatedUser.getPhone()
                    + "', or with email: '" + updatedUser.getEmail() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedUser.setCreatedAt(oldUser.getCreatedAt());
        updatedUser.setCreatedBy(oldUser.getCreatedBy());

        return userRepository.saveAndFlush(updatedUser);
    }
    @Override
    public UserReadDTO updateUserByDTO(UserUpdateDTO updatedUserDTO) throws Exception {
        User updatedUser = modelMapper.map(updatedUserDTO, User.class);

        updatedUser = updateUser(updatedUser);

        return fillDTO(updatedUser);
    }

    /* DELETE */
    @Override
    public boolean deleteUser(long userId) throws Exception {
        User user = getById(userId);

        if (user == null) {
            return false;
            /* Not found by Id */
        }

        /* Delete all associated TaskAssignment */
        taskAssignmentService.deleteAllByUserId(userId);

        /* Delete all associated ProjectManager */
        projectManagerService.deleteAllByUserId(userId);

        /* Delete associated EntityWrapper => All EFEWPairing */
        entityWrapperService.deleteByEntityIdAndEntityType(userId, ENTITY_TYPE);

        user.setIsDeleted(true);
        userRepository.saveAndFlush(user);

        return true;
    }

    /* Utils */
    private UserReadDTO fillDTO(User user) throws Exception {
        UserReadDTO userDTO = modelMapper.map(user, UserReadDTO.class);

        /* Get associated Role */
        userDTO.setRole(
                roleService.getDTOById(user.getRoleId()));
        /* Get associated ExternalFile */
        userDTO.setFileList(
                eFEWPairingService
                        .getAllExternalFileDTOByEntityIdAndEntityType(user.getUserId(), ENTITY_TYPE));

        return userDTO;
    }

    private List<UserReadDTO> fillAllDTO(Collection<User> userCollection, Integer totalPage) throws Exception {
        Set<Long> roleIdSet = new HashSet<>();
        Set<Long> userIdSet = new HashSet<>();

        for (User user : userCollection) {
            roleIdSet.add(user.getRoleId());
            userIdSet.add(user.getUserId());
        }

        /* Get associated Role */
        Map<Long, RoleReadDTO> roleIdRoleDTOMap =
                roleService.mapRoleIdRoleDTOByIdIn(roleIdSet);
        /* Get associated ExternalFile */
        Map<Long, List<ExternalFileReadDTO>> userIdExternalFileDTOListMap =
                eFEWPairingService
                        .mapEntityIdExternalFileDTOListByEntityIdInAndEntityType(userIdSet, ENTITY_TYPE);

        return userCollection.stream()
                .map(user -> {
                    UserReadDTO userReadDTO =
                            modelMapper.map(user, UserReadDTO.class);

                    userReadDTO.setRole(roleIdRoleDTOMap.get(user.getRoleId()));

                    userReadDTO.setFileList(
                            userIdExternalFileDTOListMap.get(user.getUserId()));

                    userReadDTO.setTotalPage(totalPage);

                    return userReadDTO;})
                .collect(Collectors.toList());
    }
}
