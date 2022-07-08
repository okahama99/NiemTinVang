package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentCreateDTO extends BaseCreateDTO {
    @Positive
    @NotNull(message = "taskId REQUIRED for create")
    private Long taskId;

    @Positive
    @NotNull(message = "userId (assignerId) REQUIRED for create")
    private Long assignerId;

    @Positive
    @NotNull(message = "userId (assigneeId) REQUIRED for create")
    private Long assigneeId;

    /** yyyy-MM-dd HH:mm */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "assignDate REQUIRED for create")
    private String assignDate;
}
