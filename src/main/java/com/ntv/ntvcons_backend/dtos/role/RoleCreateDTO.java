package com.ntv.ntvcons_backend.dtos.role;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreateDTO  extends BaseCreateDTO {
    @Schema(example = "Admin") /* Hint for Swagger */
    @Size(max = 100, message = "roleName max length: 100 characters")
    @NotNull(message = "roleName REQUIRED for Create")
    private String roleName;

    @Schema(example = "Quản trị") /* Hint for Swagger */
    @Size(max = 100, message = "roleDesc max length: 100 characters")
    private String roleDesc;
}
