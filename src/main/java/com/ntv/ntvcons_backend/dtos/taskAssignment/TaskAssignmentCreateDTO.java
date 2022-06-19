package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class TaskAssignmentCreateDTO implements Serializable {
    private Long taskId;
    private UserReadDTO assigner;
    private UserReadDTO assignee;
    private LocalDateTime assignDate;

    @JsonIgnore
    private final Boolean isDeleted = false;
}
