package com.ntv.ntvcons_backend.dtos.task;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateDTO extends BaseCreateDTO {
    @Positive
    @NotNull(message = "projectId REQUIRED for Create")
    private Long projectId;

    @Schema(example = "Xây xxx") /* Hint for Swagger */
    @Size(max = 100, message = "taskName max length: 100 characters")
    @NotNull(message = "taskName REQUIRED for Create")
    private String taskName;

    @Schema(example = "Xây xxx tầng 1") /* Hint for Swagger */
    @Size(max = 100, message = "taskDesc max length: 100 characters")
    private String taskDesc;

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "planStartDate REQUIRED for Create")
    private String planStartDate;

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String planEndDate;

    @Positive
    private Long assigneeId;
}
