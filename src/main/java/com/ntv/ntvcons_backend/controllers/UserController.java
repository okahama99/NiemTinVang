package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.entities.UserModels.UserFCMToken;
import com.ntv.ntvcons_backend.repositories.UserRepository;
//import com.ntv.ntvcons_backend.services.FireBase.CRUDUserFireBaseService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

//    @PostMapping("/createFcmToken")
//    public String createFcmToken(@RequestBody UserFCMToken userFCMToken ) throws InterruptedException, ExecutionException {
//        userService.updateUserFcmToken(userFCMToken);
//        return CRUDUserFireBaseService.saveUserDetails(userFCMToken);
//    }
//
//    //@PreAuthorize("hasRole('ROLE_USER')")
//    @PostMapping("/deleteFcmToken")
//    public String deleteFcmToken(@RequestParam Long userid ) throws InterruptedException, ExecutionException {
//        userService.deleteUserFcmToken(userid);
//        User user = userRepository.findById(userid).get();
////        UserProfileModel userProfile = userProfileService.findByUser(user);
//          UserFCMToken userFCMToken = new UserFCMToken();
////        userFCMToken.setFcmToken(null);
////        userFCMToken.setUserid(user.getId());
////        userFCMToken.setUserName(user.getUsername());
////        userFCMToken.setAvatar(userProfile.getAvatar());
////        userFCMToken.setEmail(user.getEmail());
////        userFCMToken.setPhone(user.getPhone());
//        return CRUDUserFireBaseService.updatePatientDetails(userFCMToken);
//    }
}
