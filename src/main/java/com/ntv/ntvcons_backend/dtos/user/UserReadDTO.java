package com.ntv.ntvcons_backend.dtos.user;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.role.RoleReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDTO extends BaseReadDTO {
    private Long userId;
    private RoleReadDTO role;
    private String username;
    private String phone;
    private String email;
}
