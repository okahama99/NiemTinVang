package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentReadDTO implements Serializable {
    private Long assignmentId;
    private Long taskId;
    private UserReadDTO assigner;
    private UserReadDTO assignee;
    private LocalDateTime assignDate;
    private LocalDateTime removeDate;

    /* TODO: to be replace with status */
//    private Boolean isDeleted = false;

    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;

}
