package com.ntv.ntvcons_backend.dtos.taskAssignment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentUpdateDTO implements Serializable {
    private Long assignmentId;
    private Long taskId;
    private Long assignerId;
    private Long assigneeId;
    /** yyyy-MM-dd HH:mm */
    private String assignDate;
    private String removeDate;

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private final Boolean isDeleted = false;
}
