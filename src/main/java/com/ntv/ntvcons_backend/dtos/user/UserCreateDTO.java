package com.ntv.ntvcons_backend.dtos.user;

import com.ntv.ntvcons_backend.constants.Regex;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO implements Serializable { /* No extends BaseCreateDTO */
    @Positive
    @NotNull(message = "roleId REQUIRED for create")
    private Long roleId;

    @Size(max = 50, message = "username max length: 100 characters")
    @NotNull(message = "username REQUIRED for create")
    private String username;

    @Pattern(regexp = Regex.PHONE_REGEX_1, message = "Need to match pattern '0 + (9-14) digits'")
    @NotNull(message = "phone REQUIRED for create")
    private String phone;

    @Email
    @Size(max = 320, message = "email max length: 320 characters")
    private String email;

    /* Copy from BaseCreateDTO due to FK conflict */
    @Positive
//    @NotNull(message = "userId (createdBy) REQUIRED for create")
//    For self create user
    private Long createdBy;

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private final LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private final Boolean isDeleted = false;
}
