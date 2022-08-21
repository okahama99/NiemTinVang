package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentReadDTO extends BaseReadDTO {
    private Long assignmentId;
    private Long taskId;
    private UserReadDTO assigner;
    private UserReadDTO assignee;
    private String assignDate;
    private String removeDate;
}
