package com.ntv.ntvcons_backend.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportReadDTO implements Serializable {
    private Long reportId;
    /* TODO: to be replace with DTO */
    private Long projectId;
    private ReportTypeReadDTO reportType;
    private Long reporterId;
    private LocalDateTime reportDate;
    private String reportDesc;

    private List<ReportDetailReadDTO> reportDetailList;
    private List<TaskReportReadDTO> taskReportList;

    /* TODO: to be replace with status */
//    private Boolean isDeleted = false;

    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;
}
