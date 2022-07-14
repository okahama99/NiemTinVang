package com.ntv.ntvcons_backend.dtos.task;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReadDTO extends BaseReadDTO {
    private Long taskId;
    private Long projectId;

    private String taskName;
    private String taskDesc;

    private LocalDateTime planStartDate;
    private LocalDateTime planEndDate;

    private LocalDateTime actualStartDate;
    private LocalDateTime actualEndDate;

    private TaskAssignmentReadDTO taskAssignment;
    private List<TaskReportReadDTO> taskReportList;

}
