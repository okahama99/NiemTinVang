package com.ntv.ntvcons_backend.dtos.taskReport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReportUpdateDTO implements Serializable {
    private Long taskReportId;
    private Long taskId;
    private Long reportId;
    private String taskProgress;
    private String taskNote;

    /* TODO: to be replace with status */
    private final Boolean isDeleted = false;
}