package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserCreateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserUpdateDTO;
import com.ntv.ntvcons_backend.services.misc.FileCombineService;
import com.ntv.ntvcons_backend.services.user.UserService;
import com.ntv.ntvcons_backend.utils.MiscUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileCombineService fileCombineService;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PostMapping(value = "/v1/createUser", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDTO userDTO) {
        try {
            UserReadDTO newUserDTO = userService.createUserByDTO(userDTO);

            return ResponseEntity.ok().body(newUserDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating User", e.getMessage()));
        }
    }

    @PostMapping(value = "/v1/createUser/withFile",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createUserWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    UserCreateDTO userDTO,
            @RequestPart MultipartFile userAvatar) {
        try {
            /* Create to get Id */
            UserReadDTO newUserDTO =
                    userService.createUserByDTO(userDTO);

            long userId = newUserDTO.getUserId();

            String fileName = userAvatar.getOriginalFilename();
            if (fileName == null || fileName.isEmpty())
                throw new IllegalArgumentException(
                        "Invalid file name, could not assert file type from the file name given. ");

            String extension = miscUtil.getExtension(fileName);
            String type = new MimetypesFileTypeMap().getContentType(fileName)
                    .split("/")[0];
            if (extension.isEmpty() || !type.equals("image"))
                throw new IllegalArgumentException("Invalid file type for user avatar. ");

            fileCombineService.saveFileInDBAndFirebase(
                    userAvatar, FileType.USER_AVATAR, userId, EntityType.USER_ENTITY, userId);

            /* Get again after file created & save */
            newUserDTO = userService.getDTOById(userId);

            return ResponseEntity.ok().body(newUserDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating User", e.getMessage()));
        }
    }

    @PostMapping(value = "/v1/addFile/{userId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addFileToUserById(
            @PathVariable long userId,
            @RequestPart MultipartFile userAvatar) {
        try {
            /* Create to get Id */
            UserReadDTO userDTO =
                    userService.getDTOById(userId);

            if (userDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No User found with Id: '" + userId + "' to add file.");

            if (userDTO.getFile() != null)
                return ResponseEntity.badRequest()
                        .body("User with Id: '" + userId + "' already has file (Max 1). " +
                                "Try using 'PUT:../replaceFile/{userId}' instead");

            String fileName = userAvatar.getOriginalFilename();
            if (fileName == null || fileName.isEmpty())
                throw new IllegalArgumentException(
                        "Invalid file name, could not assert file type from the file name given. ");

            String extension = miscUtil.getExtension(fileName);
            String type = new MimetypesFileTypeMap().getContentType(fileName)
                    .split("/")[0];
            if (extension.isEmpty() || !type.equals("image"))
                throw new IllegalArgumentException("Invalid file type for user avatar. ");

            fileCombineService.saveFileInDBAndFirebase(
                    userAvatar, FileType.USER_AVATAR, userId, EntityType.USER_ENTITY, userId);

            /* Get again after file created & save */
            userDTO = userService.getDTOById(userId);

            return ResponseEntity.ok().body(userDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error adding file to User with Id: '" + userId + "'. ",
                            e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<UserReadDTO> userDTOList =
                    userService.getAllDTOInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (userDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No User found");
            }

            return ResponseEntity.ok().body(userDTOList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for User", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.USER searchType) {
        try {
            UserReadDTO userDTO;

            switch (searchType) {
                case BY_ID:
                    userDTO = userService.getDTOById(Long.parseLong(searchParam));

                    if (userDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No User found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_USERNAME:
                    userDTO = userService.getDTOByUsername(searchParam);

                    if (userDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No User found with username: '" + searchParam + "'. ");
                    }
                    break;

                case BY_PHONE:
                    userDTO = userService.getDTOByPhone(searchParam);

                    if (userDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No User found with phone: '" + searchParam + "'. ");
                    }
                    break;
                    
                case BY_EMAIL:
                    userDTO = userService.getDTOByEmail(searchParam);

                    if (userDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No User found with email: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity User");
            }

            return ResponseEntity.ok().body(userDTO);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for User with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;

                case BY_USERNAME:
                    errorMsg += "name: '" + searchParam + "'. ";
                    break;

                case BY_PHONE:
                    errorMsg += "phone: '" + searchParam + "'. ";
                    break;

                case BY_EMAIL:
                    errorMsg += "email: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_USER searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<UserReadDTO> userDTOList;

            switch (searchType) {
                case BY_ROLE_ID:
                    userDTOList =
                            userService.getAllDTOInPagingByRoleId(paging, Long.parseLong(searchParam));

                    if (userDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No User found with roleId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_USERNAME_CONTAINS:
                    userDTOList =
                            userService.getAllDTOInPagingByUsernameContains(paging, searchParam);

                    if (userDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No User found with username contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_PHONE_CONTAINS:
                    userDTOList =
                            userService.getAllDTOInPagingByPhoneContains(paging, searchParam);

                    if (userDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No User found with phone contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_EMAIL_CONTAINS:
                    userDTOList =
                            userService.getAllDTOInPagingByEmailContains(paging, searchParam);

                    if (userDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No User found with email contains: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity User");
            }

            return ResponseEntity.ok().body(userDTOList);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                  new ErrorResponse(
                          "Invalid parameter type for searchType: '" + searchType
                              + "'. Expecting parameter of type: Long",
                          nFE.getMessage()));
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy/searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for User with ";

            switch (searchType) {
                case BY_ROLE_ID:
                    errorMsg += "roleId: '" + searchParam + "'. ";
                    break;

                case BY_USERNAME_CONTAINS:
                    errorMsg += "username contains: '" + searchParam + "'. ";
                    break;

                case BY_PHONE_CONTAINS:
                    errorMsg += "phone contains: '" + searchParam + "'. ";
                    break;

                case BY_EMAIL_CONTAINS:
                    errorMsg += "email contains: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updateUser", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserUpdateDTO userDTO) {
        try {
            UserReadDTO updatedUserDTO = userService.updateUserByDTO(userDTO);

            if (updatedUserDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No User found with Id: '" + userDTO.getUserId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedUserDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating User with Id: '" + userDTO.getUserId() + "'. ",
                            e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updateUser/withFile", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateUserWithFile(
            @RequestPart @Valid /* For regular FE input */
            @Parameter(schema = @Schema(type = "string", format = "binary")) /* For Swagger input only */
                    UserUpdateDTO userDTO,
            @RequestPart MultipartFile userAvatar) {
        try {
            UserReadDTO updatedUserDTO = userService.updateUserByDTO(userDTO);

            if (updatedUserDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No User found with Id: '" + userDTO.getUserId() + "'. ");

            long userId = updatedUserDTO.getUserId();

            String fileName = userAvatar.getOriginalFilename();
            if (fileName == null || fileName.isEmpty())
                throw new IllegalArgumentException(
                        "Invalid file name, could not assert file type from the file name given. ");

            String extension = miscUtil.getExtension(fileName);
            String type = new MimetypesFileTypeMap().getContentType(fileName)
                    .split("/")[0];
            if (extension.isEmpty() || !type.equals("image"))
                throw new IllegalArgumentException("Invalid file type for user avatar. ");

            ExternalFileReadDTO fileDTO = updatedUserDTO.getFile();
            if (fileDTO != null) {
                fileCombineService.deleteFileInDBAndFirebaseByFileDTO(fileDTO);
            }

            fileCombineService.saveFileInDBAndFirebase(
                    userAvatar, FileType.USER_AVATAR, userId, EntityType.USER_ENTITY, userId);

            /* Get again after file created & save */
            updatedUserDTO = userService.getDTOById(userId);

            return ResponseEntity.ok().body(updatedUserDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating User with Id: '" + userDTO.getUserId() + "'. ",
                            e.getMessage()));
        }
    }

    @PutMapping(value = "/v1/replaceFile/{userId}",
            consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> replaceFileOfUserById(
            @PathVariable long userId,
            @RequestPart MultipartFile userAvatar) {
        try {
            UserReadDTO userDTO =
                    userService.getDTOById(userId);

            if (userDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No User found with Id: '" + userId + "' to add file.");

            ExternalFileReadDTO fileDTO = userDTO.getFile();
            if (fileDTO == null) {
                return ResponseEntity.badRequest()
                        .body("User with Id: '" + userId + "' has no file to replace. "
                                + "Try using 'POST:../addFile/{userId}' instead");
            } else {
                fileCombineService.deleteFileInDBAndFirebaseByFileDTO(fileDTO);
            }

            String fileName = userAvatar.getOriginalFilename();
            if (fileName == null || fileName.isEmpty())
                throw new IllegalArgumentException(
                        "Invalid file name, could not assert file type from the file name given. ");

            String extension = miscUtil.getExtension(fileName);
            String type = new MimetypesFileTypeMap().getContentType(fileName)
                    .split("/")[0];
            if (extension.isEmpty() || !type.equals("image"))
                throw new IllegalArgumentException("Invalid file type for user avatar. ");

            fileCombineService.saveFileInDBAndFirebase(
                    userAvatar, FileType.USER_AVATAR, userId, EntityType.USER_ENTITY, userId);

            /* Get again after file created & save */
            userDTO = userService.getDTOById(userId);

            return ResponseEntity.ok().body(userDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error replacing file of User with Id: '" + userId + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @DeleteMapping(value = "/v1/deleteUser/{userId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        try {
            if (!userService.deleteUser(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No User found with Id: '" + userId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted User with Id: '" + userId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting User with Id: '" + userId + "'. ", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @DeleteMapping(value = "/v1/deleteFile/{userId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteFileOfUserById(@PathVariable long userId) {
        try {
            /* Get by Id */
            UserReadDTO userDTO =
                    userService.getDTOById(userId);

            if (userDTO == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No User found with Id: '" + userId + "' to replace file.");

            /* Delete old file */
            ExternalFileReadDTO fileDTO = userDTO.getFile();
            if (fileDTO != null) {
                fileCombineService.deleteFileInDBAndFirebaseByFileDTO(fileDTO);
            } else {
                return ResponseEntity.badRequest()
                        .body("User with Id: '" + userId + "' has no file to delete. ");
            }

            /* Get again after file deleted */
            userDTO = userService.getDTOById(userId);

            return ResponseEntity.ok().body(userDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Project/User by respective Id, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting file of User with Id: '" + userId + "'. ",
                            e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */
}
