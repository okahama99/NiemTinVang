package com.ntv.ntvcons_backend.dtos.taskReport;

import com.ntv.ntvcons_backend.dtos.report.ReportDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReportDTO implements Serializable {
    private Integer taskReportId;
    private TaskReadDTO task;
    private ReportDTO report;
    private String taskProgress;
    private String taskNote;
    private Boolean isDeleted = false;
}
