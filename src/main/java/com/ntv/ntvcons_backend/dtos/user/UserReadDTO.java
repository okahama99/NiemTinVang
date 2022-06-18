package com.ntv.ntvcons_backend.dtos.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.role.RoleReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDTO implements Serializable {
    private Long userId;
    private RoleReadDTO role;
    private String username;
    private String phone;
    private String email;

    /* TODO: to be replace with status */
//    private Boolean isDeleted = false;

    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;
}
