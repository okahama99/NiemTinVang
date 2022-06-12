package com.ntv.ntvcons_backend.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO implements Serializable {
    private Long taskId;
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
