package com.ntv.ntvcons_backend.dtos.user;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.role.RoleReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDTO extends BaseReadDTO {
    private Long userId;
    private RoleReadDTO role;
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String gender;
    private Date birthdate;
}
