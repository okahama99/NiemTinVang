package com.ntv.ntvcons_backend.dtos.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {
    private Integer roleId;
    private String roleName;
    private String roleDesc;
    private Boolean isDeleted;
}
