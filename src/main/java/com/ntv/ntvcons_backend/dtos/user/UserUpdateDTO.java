package com.ntv.ntvcons_backend.dtos.user;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "userId REQUIRED for update")
    private Long userId;

    @Positive
    @NotNull(message = "roleId REQUIRED for update")
    private Long roleId;

    @NotNull(message = "username REQUIRED for update")
    @Size(max = 50, message = "username max length: 50 characters")
    private String username;

    @Size(max = 128, message = "password max length: 128 characters")
//    @NotNull(message = "password REQUIRED for update")
    private String password;

    @NotNull(message = "fullName REQUIRED for update")
    @Size(max = 100, message = "fullName max length: 100 characters")
    private String fullName;

    @NotNull(message = "phone REQUIRED for update")
    @Pattern(regexp = Regex.PHONE_REGEX_1, message = "Need to match pattern '0 + (9-14) digits'")
    private String phone;

    @Email
    @Size(max = 320, message = "email max length: 320 characters")
    private String email;
}
