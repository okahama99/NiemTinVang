package com.ntv.ntvcons_backend.dtos.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdateDTO implements Serializable {
    private Long roleId;
    private String roleName;
    private String roleDesc;
    /* TODO: to be replace with status */
    private final Boolean isDeleted = false;
}
