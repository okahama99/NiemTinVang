package com.ntv.ntvcons_backend.dtos.user;

import com.ntv.ntvcons_backend.dtos.role.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToMany;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private Integer userId;
    private RoleDTO role;
    private String username;
    private String phone;
    private String email;
    private Boolean isDeleted = false;
}
