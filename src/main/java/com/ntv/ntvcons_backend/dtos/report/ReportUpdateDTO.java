package com.ntv.ntvcons_backend.dtos.report;

import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportUpdateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportUpdateDTO implements Serializable {
    private Long reportId;
    private Long projectId;
    private Long reportTypeId;
    private Long reporterId;
    /** yyyy-MM-dd HH:mm */
    private String reportDate;
    private String reportDesc;

    private List<ReportDetailCreateDTO> newReportDetailList;
    private List<TaskReportCreateDTO> newTaskReportList;

    private List<ReportDetailUpdateDTO> updatedReportDetailList;
    private List<TaskReportUpdateDTO> updatedTaskReportList;

    /* TODO: to be replace with status */
    @JsonIgnore /* No serialize/deserialize => no accept input */
    @ApiModelProperty(hidden = true) /* No show on Swagger */
    private final Boolean isDeleted = false;
}
