package com.ntv.ntvcons_backend.dtos.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class TaskCreateDTO implements Serializable {
    private Long projectId;
    private String taskName;
    private String taskDesc;
    private Instant planStartDate;
    private Instant planEndDate;
    private Instant actualStartDate;
    private Instant actualEndDate;

    /* TODO: to be replace with status */
    private final Boolean isDeleted = false;
}
