package com.ntv.ntvcons_backend.dtos.task;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReadDTO extends BaseReadDTO {
    private Long taskId;
    private Long projectId;

    private String taskName;
    private String taskDesc;

    private String planStartDate;
    private String planEndDate;

    private String actualStartDate;
    private String actualEndDate;

    private TaskAssignmentReadDTO taskAssignment;
    private List<TaskReportReadDTO> taskReportList;

}
