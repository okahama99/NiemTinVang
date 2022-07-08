package com.ntv.ntvcons_backend.dtos.taskReport;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReportUpdateDTO extends BaseUpdateDTO {
    @PositiveOrZero
    @NotNull(message = "Id REQUIRED for update")
    private Long taskReportId;

    @Positive
    @NotNull(message = "taskId REQUIRED for update")
    private Long taskId;

    @Positive
    @NotNull(message = "reportId REQUIRED for update")
    private Long reportId;

    @ApiModelProperty(example = "50%") /* Hint for Swagger */
    @Size(max = 100, message = "taskName max length: 100 characters")
    @NotNull(message = "taskProgress REQUIRED for update")
    private String taskProgress;

    @ApiModelProperty(example = "Xây xxx") /* Hint for Swagger */
    @Size(max = 100, message = "taskNote max length: 100 characters")
    private String taskNote;
}