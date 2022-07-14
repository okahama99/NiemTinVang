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
    @Positive
    @NotNull(message = "projectId REQUIRED for create")
    private Long projectId;

    @Positive
    @NotNull(message = "reportTypeId REQUIRED for create")
    private Long reportTypeId;

    @Positive
    @NotNull(message = "reporterId REQUIRED for create")
    private Long reporterId;

    @ApiModelProperty(example = "Báo cáo xxx") /* Hint for Swagger */
    @Positive
    @NotNull(message = "reportName REQUIRED for create")
    private String reportName;

    @ApiModelProperty(example = "Chuẩn bị xây tường, ...", notes = "Nullable") /* Hint for Swagger */
    private String reportDesc;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "reportDate REQUIRED for create")
    private String reportDate;

    @Size(min = 1, message = "Needed at least 1 for create")
    private List<ReportDetailCreateDTO> reportDetailList;

    @Size(min = 1, message = "Needed at least 1 for create")
    private List<TaskReportCreateDTO> taskReportList;
}
