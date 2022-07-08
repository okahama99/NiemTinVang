package com.ntv.ntvcons_backend.dtos.role;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleReadDTO extends BaseReadDTO {
    private Long roleId;
    private String roleName;
    private String roleDesc;
}
