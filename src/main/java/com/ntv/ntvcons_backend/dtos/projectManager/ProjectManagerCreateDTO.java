package com.ntv.ntvcons_backend.dtos.projectManager;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectManagerCreateDTO extends BaseCreateDTO {
    @PositiveOrZero
    @NotNull(message = "projectId REQUIRED for create")
    private Long projectId;

    @Positive
    @NotNull(message = "userId (managerId) REQUIRED for create")
    private Long managerId;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "assignDate REQUIRED for create")
    private String assignDate;
}
