package com.ntv.ntvcons_backend.services.OTPEmail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResetPasswordModel {
    private String email;
    private String password;
}
