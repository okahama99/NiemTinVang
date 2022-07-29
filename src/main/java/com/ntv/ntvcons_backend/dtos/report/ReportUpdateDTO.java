package com.ntv.ntvcons_backend.dtos.report;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "Id REQUIRED for Update")
    private Long reportId;

    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "projectId REQUIRED for Update")
    private Long projectId;

    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "reportTypeId REQUIRED for Update")
    private Long reportTypeId;

    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "reporterId REQUIRED for Update")
    private Long reporterId;

    @Schema(example = "Báo cáo xxx",
            description = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "reportName max length: 100 characters")
    @NotNull(message = "reportName REQUIRED for Update")
    private String reportName;

    @Schema(example = "Chuẩn bị xây tường, ...",
            description = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "reportDesc max length: 100 characters")
    private String reportDesc;

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd HH:mm",
            description = "NOT NULL; date <= now") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "reportDate REQUIRED for Update")
    private String reportDate;

    @Schema(description = "Nullable; size >= 1 (if not null)") /* Hint for Swagger */
    @Size(min = 1, message = "Needed at least 1 for Update")
    private List<ReportDetailUpdateDTO> reportDetailList;

    @Schema(description = "Nullable; size >= 1 (if not null)") /* Hint for Swagger */
    @Size(min = 1, message = "Needed at least 1 for Update")
    private List<TaskReportUpdateDTO> taskReportList;
}
