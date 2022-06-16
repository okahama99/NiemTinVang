package com.ntv.ntvcons_backend.dtos.report;

import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportCreateDTO implements Serializable {
    private Long projectId;
    private Long reportTypeId;
    private Long reporterId;
    private Instant reportDate;
    private String reportDesc;

    private List<ReportDetailCreateDTO> reportDetailList;
    private List<TaskReportCreateDTO> taskReportList;

    private final Boolean isDeleted = false;
}
