package com.ntv.ntvcons_backend.dtos.report;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportUpdateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportUpdateDTO extends BaseUpdateDTO {
    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long reportId;

    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "projectId REQUIRED for update")
    private Long projectId;

    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "reportTypeId REQUIRED for update")
    private Long reportTypeId;

    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "reporterId REQUIRED for update")
    private Long reporterId;

    @ApiModelProperty(example = "Báo cáo xxx",
            notes = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "reportName max length: 100 characters")
    @NotNull(message = "reportName REQUIRED for update")
    private String reportName;

    @ApiModelProperty(example = "Chuẩn bị xây tường, ...",
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "reportDesc max length: 100 characters")
    private String reportDesc;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm",
            notes = "NOT NULL; date <= now") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "reportDate REQUIRED for update")
    private String reportDate;

    @ApiModelProperty(notes = "Nullable; size >= 1 (if not null)") /* Hint for Swagger */
    @Size(min = 1, message = "Needed at least 1 for update")
    private List<ReportDetailUpdateDTO> reportDetailList;

    @ApiModelProperty(notes = "Nullable; size >= 1 (if not null)") /* Hint for Swagger */
    @Size(min = 1, message = "Needed at least 1 for update")
    private List<TaskReportUpdateDTO> taskReportList;
}
