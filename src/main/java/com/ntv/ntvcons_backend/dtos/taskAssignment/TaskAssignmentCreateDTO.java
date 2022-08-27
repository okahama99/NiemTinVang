package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentCreateDTO extends BaseCreateDTO {
    @Positive
    @NotNull(message = "taskId REQUIRED for Create")
    private Long taskId;

    @Positive
    @NotNull(message = "userId (assignerId) REQUIRED for Create")
    private Long assignerId;

    @Positive
    @NotNull(message = "userId (assigneeId) REQUIRED for Create")
    private Long assigneeId;

    /** yyyy-MM-dd HH:mm */
    /* TODO: use later or skip forever */
//    /@Schema(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
//    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
//    @NotNull(message = "assignDate REQUIRED for Create")
//    private String assignDate;

    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private final LocalDateTime assignDate = LocalDateTime.now();
}
