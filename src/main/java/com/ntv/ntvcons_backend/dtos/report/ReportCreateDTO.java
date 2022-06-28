package com.ntv.ntvcons_backend.dtos.report;

import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportCreateDTO implements Serializable {
    private Long projectId;
    private Long reportTypeId;
    private Long reporterId;
    private String reportName;
    private String reportDesc;
    /** yyyy-MM-dd HH:mm */
    private String reportDate;

    private List<ReportDetailCreateDTO> reportDetailList;
    private List<TaskReportCreateDTO> taskReportList;

    @JsonIgnore /* No serialize/deserialize => no accept input */
    @ApiModelProperty(hidden = true) /* No show on Swagger */
    private final Boolean isDeleted = false;
}
