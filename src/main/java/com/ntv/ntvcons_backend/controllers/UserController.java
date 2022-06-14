package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.repositories.UserRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
}
