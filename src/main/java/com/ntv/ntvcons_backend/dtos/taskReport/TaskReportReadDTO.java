package com.ntv.ntvcons_backend.dtos.taskReport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReportReadDTO implements Serializable {
    private Long taskReportId;
    private TaskReadDTO task;
    private Long reportId;
    private String taskProgress;
    private String taskNote;

    /* TODO: to be replace with status */
//    private Boolean isDeleted = false;

    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;

}
