package com.ntv.ntvcons_backend.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ntv.ntvcons_backend.constants.Gender;
import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.constants.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreateDTO implements Serializable { /* No extends BaseCreateDTO */
    @Positive
    @NotNull(message = "roleId REQUIRED for Create")
    private Long roleId;

    @Size(max = 50, message = "username max length: 50 characters")
    @NotNull(message = "username REQUIRED for Create")
    private String username;

    @Size(max = 128, message = "password max length: 128 characters")
//    @NotNull(message = "password REQUIRED for Create")
    private String password;

    @Size(max = 100, message = "fullName max length: 100 characters")
    @NotNull(message = "fullName REQUIRED for Create")
    private String fullName;

    @Pattern(regexp = Regex.PHONE_REGEX_1, message = "Need to match pattern '0 + (9-14) digits'")
    @NotNull(message = "phone REQUIRED for Create")
    private String phone;

    @Email
    @Size(max = 320, message = "email max length: 320 characters")
    private String email;

    @Schema(example = "UNKNOWN") /* Hint for Swagger */
    private Gender gender = Gender.UNKNOWN; /* Default */

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATE_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd'")
    private String birthdate;

    /* Copy from BaseCreateDTO due to FK conflict */
    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
//    @Positive
//    @NotNull(message = "userId (createdBy) REQUIRED for Create")
//    For self create user
    private final Long createdBy = null;

    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private final LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private final Status status = Status.ACTIVE;
}
