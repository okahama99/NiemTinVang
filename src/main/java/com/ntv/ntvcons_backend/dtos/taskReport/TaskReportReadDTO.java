package com.ntv.ntvcons_backend.dtos.taskReport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReportReadDTO extends BaseReadDTO {
    private Long taskReportId;
    private Long taskId;
    private Long reportId;
    private String taskProgress;
    private String taskNote;
}
