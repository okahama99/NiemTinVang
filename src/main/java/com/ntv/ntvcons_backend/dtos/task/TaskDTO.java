package com.ntv.ntvcons_backend.dtos.task;

import com.ntv.ntvcons_backend.dtos.project.ProjectDTO;
import com.ntv.ntvcons_backend.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO implements Serializable {
    private Integer taskId;
    private ProjectDTO project;
    private UserDTO creator;
    private String taskName;
    private String taskDesc;
    private Instant planStartDate;
    private Instant planEndDate;
    private Instant actualStartDate;
    private Instant actualEndDate;
    private Boolean isDeleted = false;
}
