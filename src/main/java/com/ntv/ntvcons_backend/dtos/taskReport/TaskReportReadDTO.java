package com.ntv.ntvcons_backend.dtos.taskReport;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
