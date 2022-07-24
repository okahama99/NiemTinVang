package com.ntv.ntvcons_backend.controllers;


import com.ntv.ntvcons_backend.entities.UserModels.UserLoginModel;
import com.ntv.ntvcons_backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> login(@RequestBody UserLoginModel user){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, String> response = new HashMap<>(2);
        String token = jwtUtil.generateTokenNew(authentication);
        response.put("status", "success");
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
