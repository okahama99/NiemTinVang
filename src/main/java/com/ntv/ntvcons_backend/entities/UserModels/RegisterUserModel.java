package com.ntv.ntvcons_backend.entities.UserModels;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterUserModel {

    private String email;
    @NotBlank
    @NotNull
    private String username, password, fullName, phone;
}
