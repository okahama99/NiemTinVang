package com.ntv.ntvcons_backend.dtos.taskReport;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReportUpdateDTO extends BaseUpdateDTO {
    @PositiveOrZero
    @NotNull(message = "Id REQUIRED for Update")
    private Long taskReportId;

    @Positive
    @NotNull(message = "taskId REQUIRED for Update")
    private Long taskId;

    @Positive
    @NotNull(message = "reportId REQUIRED for Update")
    private Long reportId;

    @Schema(example = "50%") /* Hint for Swagger */
    @Size(max = 100, message = "taskName max length: 100 characters")
    @NotNull(message = "taskProgress REQUIRED for Update")
    private String taskProgress;

    @Schema(example = "Xây xxx") /* Hint for Swagger */
    @Size(max = 100, message = "taskNote max length: 100 characters")
    private String taskNote;
}