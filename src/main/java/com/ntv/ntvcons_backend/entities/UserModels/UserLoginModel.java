package com.ntv.ntvcons_backend.entities.UserModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserLoginModel {
    private String username;
    private String password;
}
