package com.ntv.ntvcons_backend.dtos.report;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
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
public class ReportCreateDTO extends BaseCreateDTO {
    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "projectId REQUIRED for create")
    private Long projectId;

    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "reportTypeId REQUIRED for create")
    private Long reportTypeId;

    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "reporterId REQUIRED for create")
    private Long reporterId;

    @ApiModelProperty(example = "Báo cáo xxx", 
            notes = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "reportName max length: 100 characters")
    @NotNull(message = "reportName REQUIRED for create")
    private String reportName;

    @ApiModelProperty(example = "Chuẩn bị xây tường, ...", 
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "reportDesc max length: 100 characters")
    private String reportDesc;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm", 
            notes = "NOT NULL; date <= now") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "reportDate REQUIRED for create")
    private String reportDate;

    @ApiModelProperty(notes = "Nullable; size >= 1 (if not null)") /* Hint for Swagger */
    @Size(min = 1, message = "Needed at least 1 for create")
    private List<ReportDetailCreateDTO> reportDetailList;

    @ApiModelProperty(notes = "Nullable; size >= 1 (if not null)") /* Hint for Swagger */
    @Size(min = 1, message = "Needed at least 1 for create")
    private List<TaskReportCreateDTO> taskReportList;
}
