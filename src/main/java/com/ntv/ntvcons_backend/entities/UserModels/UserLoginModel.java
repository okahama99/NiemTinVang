package com.ntv.ntvcons_backend.entities.UserModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
public class UserLoginModel {
    @Size(max = 50)
    @NotNull
    private String username;

    @Size(max = 128)
    @NotNull
    private String password;
}
