package com.ntv.ntvcons_backend.dtos.role;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long roleId;

    @ApiModelProperty(example = "Admin") /* Hint for Swagger */
    @Size(max = 100, message = "roleName max length: 100 characters")
    @NotNull(message = "roleName REQUIRED for update")
    private String roleName;

    @ApiModelProperty(example = "Quản trị") /* Hint for Swagger */
    @Size(max = 100, message = "roleDesc max length: 100 characters")
    private String roleDesc;
}
