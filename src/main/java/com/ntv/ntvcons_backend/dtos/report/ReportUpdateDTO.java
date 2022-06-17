package com.ntv.ntvcons_backend.dtos.report;

import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportUpdateDTO implements Serializable {
    private Long reportId;
    private Long projectId;
    private Long reportTypeId;
    private Long reporterId;
    private Instant reportDate;
    private String reportDesc;

    private List<ReportDetailCreateDTO> newReportDetailList;
    private List<TaskReportCreateDTO> newTaskReportList;

    private List<ReportDetailUpdateDTO> updatedReportDetailList;
    private List<TaskReportUpdateDTO> updatedTaskReportList;

    private final Boolean isDeleted = false;
}