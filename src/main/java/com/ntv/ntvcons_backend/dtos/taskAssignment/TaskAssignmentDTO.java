package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserDTO;
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
    private UserDTO assigner;
    private UserDTO assignee;
    private LocalDateTime assignDate;
    private LocalDateTime removeDate;
    private Boolean isDeleted = false;
}
