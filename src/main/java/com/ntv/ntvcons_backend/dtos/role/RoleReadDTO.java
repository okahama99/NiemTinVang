package com.ntv.ntvcons_backend.dtos.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleReadDTO implements Serializable {
    private Long roleId;
    private String roleName;
    private String roleDesc;

    /* TODO: to be replace with status */
//    private Boolean isDelete = false;

    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;
}
