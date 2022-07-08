package com.ntv.ntvcons_backend.dtos.report;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportUpdateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long reportId;

    @Positive
    @NotNull(message = "projectId REQUIRED for update")
    private Long projectId;

    @Positive
    @NotNull(message = "reportTypeId REQUIRED for update")
    private Long reportTypeId;

    @Positive
    @NotNull(message = "reporterId REQUIRED for update")
    private Long reporterId;

    @ApiModelProperty(example = "Báo cáo xxx") /* Hint for Swagger */
    @Positive
    @NotNull(message = "reportName REQUIRED for update")
    private String reportName;

    @ApiModelProperty(example = "Chuẩn bị xây tường, ...") /* Hint for Swagger */
    @Positive
    private String reportDesc;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "reportDate REQUIRED for update")
    private String reportDate;

    private List<ReportDetailUpdateDTO> reportDetailList;
    private List<TaskReportUpdateDTO> taskReportList;
}
