package com.ntv.ntvcons_backend.services.user;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.role.RoleReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserCreateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserUpdateDTO;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.entities.UserModels.RegisterUserModel;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import com.ntv.ntvcons_backend.services.entityWrapper.EntityWrapperService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.misc.FileCombineService;
import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
import com.ntv.ntvcons_backend.services.role.RoleService;
import com.ntv.ntvcons_backend.services.taskAssignment.TaskAssignmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
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
    private FileCombineService fileCombineService;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;

    private final EntityType ENTITY_TYPE = EntityType.USER_ENTITY;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

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
        if (newUser.getEmail() != null) {
            if (userRepository
                    .existsByUsernameOrPhoneOrEmailAndStatusNotIn(
                            newUser.getUsername(),
                            newUser.getPhone(),
                            newUser.getEmail(),
                            N_D_S_STATUS_LIST)) {
                errorMsg += "Already exists another User with username: '" + newUser.getUsername()
                        + "', or with phone: '" + newUser.getPhone()
                        + "', or with email: '" + newUser.getEmail() + "'. ";
            }
        } else {
            if (userRepository
                    .existsByUsernameOrPhoneAndStatusNotIn(
                            newUser.getUsername(),
                            newUser.getPhone(),
                            N_D_S_STATUS_LIST)) {
                errorMsg += "Already exists another User with username: '" + newUser.getUsername()
                        + "', or with phone: '" + newUser.getPhone() + "'. ";
            }
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
        modelMapper.typeMap(UserCreateDTO.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setBirthdate);});

        User newUser = modelMapper.map(newUserDTO, User.class);

        /* Check input */
        if (newUserDTO.getBirthdate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            newUser.setBirthdate(new java.sql.Date(
                    sdf.parse(newUserDTO.getBirthdate()).getTime()));

            /* Now */
            Calendar calendar = Calendar.getInstance();
            /* Tuổi tối thiểu lao động/sở hữu nhà đất TODO:(18 hay 15? hỏi lại anh sơn) */
            /* Perform addition/subtraction (số dương +, số âm -) */
            calendar.add(Calendar.YEAR, -18);

            /* TODO: hỏi về tuổi lao động, tuổi sỡ hữu nhà đất tù đó check theo role*/

            /* Convert calendar to Date */
            Date minLegalAgeBirthdate = calendar.getTime();

            if (newUser.getBirthdate().after(minLegalAgeBirthdate)) {
                throw new IllegalArgumentException(
                        "This user birthdate: '" + newUser.getBirthdate()
                                + "' mean they are younger than 18. Which violate labour/ownership law. "
                                + " Valid birthdate must be before: '" + sdf.format(minLegalAgeBirthdate) + "'. ");
            }
        }

        newUser = createUser(newUser);

        /* Create associated EntityWrapper */
        entityWrapperService
                .createEntityWrapper(newUser.getUserId(), ENTITY_TYPE, newUser.getCreatedBy());

        return fillDTO(newUser);
    }

    @Override
    public User register(RegisterUserModel registerUserModel) throws Exception {
        String errorMsg = "";
        User user = new User();
        if (registerUserModel.getEmail() != null) {
            if(!userRepository.existsByUsernameOrPhoneOrEmailAndStatusNotIn(
                    registerUserModel.getUsername(),
                    registerUserModel.getPhone(),
                    registerUserModel.getEmail(),
                    N_D_S_STATUS_LIST))
                errorMsg += "Already exists another User with username: '" + registerUserModel.getUsername()
                        + "', or with phone: '" + registerUserModel.getPhone()
                        + "', or with email: '" + registerUserModel.getEmail() + "'. ";
            } else {
            if (userRepository.existsByUsernameOrPhoneAndStatusNotIn(
                            registerUserModel.getUsername(),
                            registerUserModel.getPhone(),
                            N_D_S_STATUS_LIST)) {
                errorMsg += "Already exists another User with username: '" + registerUserModel.getUsername()
                        + "', or with phone: '" + registerUserModel.getPhone() + "'. ";
            }
        }
        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        user.setEmail(registerUserModel.getEmail());
        user.setUsername(registerUserModel.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
        user.setPhone(registerUserModel.getPhone());
        user.setFullName(registerUserModel.getFullName());
        user.setCreatedAt(LocalDateTime.now());
        user.setRoleId(Long.parseLong("4"));
        userRepository.saveAndFlush(user);

        return user;
    }

    /* READ */
    @Override
    public Page<User> getPageAll(Pageable paging) throws Exception {
        Page<User> userPage =
                userRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

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
        return userRepository
                .existsByUserIdAndStatusNotIn(userId, N_D_S_STATUS_LIST);
    }
    @Override
    public User getById(long userId) throws Exception {
        return userRepository
                .findByUserIdAndStatusNotIn(userId, N_D_S_STATUS_LIST)
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
        return userRepository
                .existsAllByUserIdInAndStatusNotIn(userIdCollection, N_D_S_STATUS_LIST);
    }
    @Override
    public List<User> getAllByIdIn(Collection<Long> userIdCollection) throws Exception {
        List<User> userList =
                userRepository.findAllByUserIdInAndStatusNotIn(userIdCollection, N_D_S_STATUS_LIST);

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
        List<User> userList =
                userRepository.findAllByRoleIdAndStatusNotIn(roleId, N_D_S_STATUS_LIST);

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
                userRepository.findAllByRoleIdAndStatusNotIn(roleId, N_D_S_STATUS_LIST, paging);

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
                .findByUsernameAndStatusNotIn(username, N_D_S_STATUS_LIST)
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
        List<User> userList = userRepository.findAllByUsernameContainsAndStatusNotIn(username, N_D_S_STATUS_LIST);

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
                userRepository.findAllByUsernameContainsAndStatusNotIn(username, N_D_S_STATUS_LIST, paging);

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
                .findByPhoneAndStatusNotIn(phone, N_D_S_STATUS_LIST)
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
        List<User> userList = userRepository.findAllByPhoneContainsAndStatusNotIn(phone, N_D_S_STATUS_LIST);

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
                userRepository.findAllByPhoneContainsAndStatusNotIn(phone, N_D_S_STATUS_LIST, paging);

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
                .findByEmailAndStatusNotIn(email, N_D_S_STATUS_LIST)
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
        List<User> userList = userRepository.findAllByEmailContainsAndStatusNotIn(email, N_D_S_STATUS_LIST);

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
                userRepository.findAllByEmailContainsAndStatusNotIn(email, N_D_S_STATUS_LIST, paging);

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
        if (updatedUser.getUpdatedBy() != null) {
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
        }

        /* Check duplicate */
        if (updatedUser.getEmail() != null) {
            if (userRepository
                    .existsByUsernameOrPhoneOrEmailAndUserIdIsNotAndStatusNotIn(
                            updatedUser.getUsername(),
                            updatedUser.getPhone(),
                            updatedUser.getEmail(),
                            updatedUser.getUserId(),
                            N_D_S_STATUS_LIST)) {
                errorMsg += "Already exists another User with username: '" + updatedUser.getUsername()
                        + "', or with phone: '" + updatedUser.getPhone()
                        + "', or with email: '" + updatedUser.getEmail() + "'. ";
            }
        } else {
            if (userRepository
                    .existsByUsernameOrPhoneAndUserIdIsNotAndStatusNotIn(
                            updatedUser.getUsername(),
                            updatedUser.getPhone(),
                            updatedUser.getUserId(),
                            N_D_S_STATUS_LIST)) {
                errorMsg += "Already exists another User with username: '" + updatedUser.getUsername()
                        + "', or with phone: '" + updatedUser.getPhone() + "'. ";
            }
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        if (updatedUser.getPassword() != null) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        } else {
            updatedUser.setPassword(oldUser.getPassword());
        }

        updatedUser.setCreatedAt(oldUser.getCreatedAt());
        updatedUser.setCreatedBy(oldUser.getCreatedBy());

        return userRepository.saveAndFlush(updatedUser);
    }
    @Override
    public UserReadDTO updateUserByDTO(UserUpdateDTO updatedUserDTO) throws Exception {
        modelMapper.typeMap(UserUpdateDTO.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setBirthdate);});

        User updatedUser = modelMapper.map(updatedUserDTO, User.class);

        if (updatedUserDTO.getBirthdate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            updatedUser.setBirthdate(new java.sql.Date(
                    sdf.parse(updatedUserDTO.getBirthdate()).getTime()));

            /* Now */
            Calendar calendar = Calendar.getInstance();
            /* Tuổi tối thiểu lao động/sở hữu nhà đất TODO:(18 hay 15? hỏi lại anh sơn) */
            /* Perform addition/subtraction (số dương +, số âm -) */
            calendar.add(Calendar.YEAR, -18);

            /* TODO: hỏi về tuổi lao động, tuổi sỡ hữu nhà đất tù đó check theo role */

            /* Convert calendar to Date */
            Date minLegalAgeBirthdate = calendar.getTime();

            if (updatedUser.getBirthdate().after(minLegalAgeBirthdate)) {
                throw new IllegalArgumentException(
                        "This user birthdate: '" + updatedUser.getBirthdate()
                                + "' mean they are younger than 18. Which violate labour/ownership law. "
                                + " Valid birthdate must be before: '" + sdf.format(minLegalAgeBirthdate) + "'. ");
            }
        }

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

        /* Delete associated File (In DB And Firebase) */
        List<ExternalFileReadDTO> fileDTOList =
                eFEWPairingService
                        .getAllExternalFileDTOByEntityIdAndEntityType(userId, ENTITY_TYPE);

        if (fileDTOList != null && !fileDTOList.isEmpty()) {
            fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
        }

        /* Delete associated EntityWrapper */
        entityWrapperService.deleteByEntityIdAndEntityType(userId, ENTITY_TYPE);

        user.setStatus(Status.DELETED);
        userRepository.saveAndFlush(user);

        return true;
    }

    /* Utils */
    private UserReadDTO fillDTO(User user) throws Exception {
        modelMapper.typeMap(User.class, UserReadDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(UserReadDTO::setGender);
                    mapper.skip(UserReadDTO::setCreatedAt);
                    mapper.skip(UserReadDTO::setUpdatedAt);});

        long userId = user.getUserId();

        UserReadDTO userDTO = modelMapper.map(user, UserReadDTO.class);

        userDTO.setGender(user.getGender().getStringValueVie());

        if (user.getCreatedAt() != null)
            userDTO.setCreatedAt(user.getCreatedAt().format(dateTimeFormatter));
        if (user.getUpdatedAt() != null)
            userDTO.setUpdatedAt(user.getUpdatedAt().format(dateTimeFormatter));

        /* Get associated Role */
        userDTO.setRole(
                roleService.getDTOById(user.getRoleId()));
        /* Get associated ExternalFile */
        List<ExternalFileReadDTO> fileReadDTOList =
                eFEWPairingService
                        .getAllExternalFileDTOByEntityIdAndEntityType(userId, ENTITY_TYPE);

        if (fileReadDTOList != null && !fileReadDTOList.isEmpty()) {
            /* if (fileReadDTOList.size() > 1)
                Log Error, user only have 1 avatar file at a time */
            userDTO.setFile(fileReadDTOList.get(0));
        }

        return userDTO;
    }

    private List<UserReadDTO> fillAllDTO(Collection<User> userCollection, Integer totalPage) throws Exception {
        modelMapper.typeMap(User.class, UserReadDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(UserReadDTO::setGender);
                    mapper.skip(UserReadDTO::setCreatedAt);
                    mapper.skip(UserReadDTO::setUpdatedAt);});

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
                    UserReadDTO userDTO =
                            modelMapper.map(user, UserReadDTO.class);

                    userDTO.setGender(user.getGender().getStringValueVie());

                    if (user.getCreatedAt() != null)
                        userDTO.setCreatedAt(user.getCreatedAt().format(dateTimeFormatter));
                    if (user.getUpdatedAt() != null)
                        userDTO.setUpdatedAt(user.getUpdatedAt().format(dateTimeFormatter));

                    userDTO.setRole(roleIdRoleDTOMap.get(user.getRoleId()));

                    List<ExternalFileReadDTO> fileReadDTOList =
                            userIdExternalFileDTOListMap.get(user.getUserId());

                    if (fileReadDTOList != null && !fileReadDTOList.isEmpty()) {
                        /* if (fileReadDTOList.size() > 1)
                            Log Error, user only have 1 avatar file at a time */
                        userDTO.setFile(fileReadDTOList.get(0));
                    }

                    userDTO.setTotalPage(totalPage);

                    return userDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public void resetPasswordUser(String email, String password) {
        User user = userRepository.findByEmailAndStatusNotIn(email, N_D_S_STATUS_LIST).orElse(null);
        user.setPassword(password);
        userRepository.save(user);
    }
}
