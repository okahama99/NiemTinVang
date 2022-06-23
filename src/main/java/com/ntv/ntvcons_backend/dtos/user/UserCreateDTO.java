package com.ntv.ntvcons_backend.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO implements Serializable {
    private Long roleId;
    private String username;
    private String phone;
    private String email;
    /* TODO: to be replace with status */
    private final Boolean isDeleted = false;
}
