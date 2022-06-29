package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long assignmentId;

    @Positive
    @NotNull(message = "taskId REQUIRED for update")
    private Long taskId;

    @Positive
    @NotNull(message = "userId (assignerId) REQUIRED for update")
    private Long assignerId;

    @Positive
    @NotNull(message = "userId (assigneeId)  REQUIRED for update")
    private Long assigneeId;

    /** yyyy-MM-dd HH:mm */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "assignDate REQUIRED for update")
    private String assignDate;

    /** yyyy-MM-dd HH:mm */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String removeDate;

}
