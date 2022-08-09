package com.ntv.ntvcons_backend.controllers;


import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.entities.UserModels.RegisterUserModel;
import com.ntv.ntvcons_backend.entities.UserModels.UserLoginModel;
import com.ntv.ntvcons_backend.services.OTPEmail.OtpService;
import com.ntv.ntvcons_backend.services.OTPEmail.UserResetPasswordModel;
import com.ntv.ntvcons_backend.services.OTPPhone.OTPService;
import com.ntv.ntvcons_backend.services.OTPPhone.SmsRequest;
import com.ntv.ntvcons_backend.services.user.UserService;
import com.ntv.ntvcons_backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

    @Autowired
    private OTPService serviceOTP;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginModel user) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            /* TODO: Có cần kiểm tra authentication.isAuthenticated() hay ko? */

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Map<String, String> response = new HashMap<>(2);

            String token = jwtUtil.generateTokenV2(authentication);

            response.put("status", "success");
            response.put("token", token);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error Login", e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/logout", method= RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }


    @PostMapping(value = "/resetPasswordOTPEmail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> resetPasswordOTPEmail(@RequestParam("email") String email) {

        Map<String, String> response = new HashMap<>(2);

        // generate OTP.
        Boolean isGenerated = otpService.generateOTPForResetPassword(email);
        if (!isGenerated)
        {
            response.put("status", "error");
            response.put("message", "Không thể khởi tạo OTP.");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // success message
        response.put("status", "success");
        response.put("message", "Đã gửi mã OTP, xin vui lòng kiểm tra email.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/emailOTPVerification", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> emailOTPVerification(@RequestParam("email") String email) {

        Map<String, String> response = new HashMap<>(2);

        // generate OTP.
        Boolean isGenerated = otpService.generateVerificationOTP(email);
        if (!isGenerated)
        {
            response.put("status", "error");
            response.put("message", "Không thể khởi tạo OTP.");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // success message
        response.put("status", "success");
        response.put("message", "Đã gửi mã OTP, xin vui lòng kiểm tra email.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/resetPassword", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> resetPassword(@RequestBody UserResetPasswordModel userResetPasswordModel)
    {
        userService.resetPasswordUser(userResetPasswordModel.getEmail(), passwordEncoder.encode(userResetPasswordModel.getPassword()));
        return new ResponseEntity<>("Đổi mật khẩu thành công", HttpStatus.OK);
    }
    @PostMapping(value = "/validateOTP", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> validateOTP(@RequestParam String email,
                                              @RequestParam Integer otp)
    {
        // validate provided OTP.
        Boolean isValid = otpService.validateOTP(email, otp);
        if (!isValid)
        {
            return new ResponseEntity<>("OTP không chính xác", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("OTP chính xác", HttpStatus.OK);
    }

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> register(@RequestBody RegisterUserModel registerUserModel) {
        try {
            User user = userService.register(registerUserModel);

            return ResponseEntity.ok().body(user);
        }catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating User", e.getMessage()));
        }
    }

    @Autowired
    public LoginController(OTPService otpService) {
        this.serviceOTP = otpService;
    }

    @PostMapping(value = "/phoneOTPVerification", produces = "application/json;charset=UTF-8")
    public void smsSender(@RequestBody @Valid SmsRequest smsRequest) {
        serviceOTP.smsSender(smsRequest);
    }
}
