package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentDTO implements Serializable {
    private Integer assignmentId;
    private TaskReadDTO task;
    private UserReadDTO assigner;
    private UserReadDTO assignee;
    private LocalDateTime assignDate;
    private LocalDateTime removeDate;
    private Boolean isDeleted = false;
}
