package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.ntv.ntvcons_backend.dtos.task.TaskDTO;
import com.ntv.ntvcons_backend.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentDTO implements Serializable {
    private Integer assignmentId;
    private TaskDTO task;
    private UserDTO assigner;
    private UserDTO assignee;
    private Instant assignDate;
    private Instant removeDate;
    private Boolean isDeleted = false;
}
