package com.ntv.ntvcons_backend.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
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
public class ReportReadDTO extends BaseReadDTO {
    private Long reportId;
    private Long projectId;
    private ReportTypeReadDTO reportType;
    private Long reporterId;
    private String reportName;
    private String reportDesc;
    private LocalDateTime reportDate;

    private List<ReportDetailReadDTO> reportDetailList;
    private List<TaskReportReadDTO> taskReportList;
}
