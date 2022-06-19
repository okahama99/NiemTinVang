package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentUpdateDTO implements Serializable {
    private Long assignmentId;
    private Long taskId;
    private Long assignerId;
    private Long assigneeId;
    private LocalDateTime assignDate;
    private LocalDateTime removeDate;

    @JsonIgnore
    private final Boolean isDeleted = false;
}
