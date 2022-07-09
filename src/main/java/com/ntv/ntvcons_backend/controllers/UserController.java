package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.user.UserCreateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserUpdateDTO;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    //@PreAuthorize("hasAnyRole('Admin','Staff')")
    @PostMapping(value = "/v1/createUser", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserCreateDTO userDTO){
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

    /* READ */
    //@PreAuthorize("hasAnyRole('Admin','Staff')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<UserReadDTO> userDTOList =
                    userService.getAllDTOInPaging(pageNo, pageSize, sortBy, sortTypeAsc);

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


    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.USER searchType) {
        // TODO:
        return null;
    }

    //@PreAuthorize("hasAnyRole('Admin','Staff')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_USER searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            List<UserReadDTO> userDTOList;

            switch (searchType) {
                case BY_ROLE_ID:
                    userDTOList = userService.getAllDTOByRoleId(Long.parseLong(searchParam));

                    if (userDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No User found with roleId: '" + searchParam + "'. ");
                    }
                    break;

//                case BY_PHONE_CONTAINS:
//                    userDTOList = userService.getAllDTOByPhoneContains(searchParam);
//
//                    if (userDTOList == null) {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                                .body("No User found with phone contains: '" + searchParam + "'. ");
//                    }
//                    break;

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
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given" , iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for User with ";

            switch (searchType) {
                case BY_ROLE_ID:
                    errorMsg += "roleId: '" + searchParam + "'. ";
                    break;

//                case BY_USERNAME_CONTAINS:
//                    errorMsg += "username contains: '" + searchParam + "'. ";
//                    break;
//
//                case BY_PHONE_CONTAINS:
//                    errorMsg += "phone contains: '" + searchParam + "'. ";
//                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    //@PreAuthorize("hasAnyRole('Admin','Staff')")
    @PutMapping(value = "/v1/updateUser", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserUpdateDTO userDTO){
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

    /* DELETE */
    //@PreAuthorize("hasAnyRole('Admin','Staff')")
    @DeleteMapping(value = "/v1/deleteUser/{userId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "userId") long userId){
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
    /* ================================================ Ver 1 ================================================ */
}
