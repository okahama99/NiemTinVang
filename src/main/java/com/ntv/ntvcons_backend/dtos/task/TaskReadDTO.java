package com.ntv.ntvcons_backend.dtos.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReadDTO implements Serializable {
    private Long taskId;
    private Long projectId;
    private String taskName;
    private String taskDesc;
    private LocalDateTime planStartDate;
    private LocalDateTime planEndDate;
    private LocalDateTime actualStartDate;
    private LocalDateTime actualEndDate;

    private TaskAssignmentReadDTO taskAssignment;

    /* TODO: to be replace with status */
//    private Boolean isDeleted = false;

    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;

}
