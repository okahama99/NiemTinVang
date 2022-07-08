package com.ntv.ntvcons_backend.dtos.projectManager;

import com.ntv.ntvcons_backend.constants.Regex;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectManagerUpdateDTO implements Serializable {
    @Positive
    @NotNull(message = "projectId REQUIRED for update")
    private Long projectManagerId;

    @Positive
    @NotNull(message = "projectId REQUIRED for update")
    private Long projectId;

    @Positive
    @NotNull(message = "userId (managerId) REQUIRED for update")
    private Long managerId;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "assignDate REQUIRED for update")
    private String assignDate;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String removeDate;
}
