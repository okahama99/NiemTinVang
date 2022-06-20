package com.ntv.ntvcons_backend.dtos.taskAssignment;

import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentCreateDTO implements Serializable {
    private Long taskId;
    private UserReadDTO assigner;
    private UserReadDTO assignee;
    private String assignDate;

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private final Boolean isDeleted = false;
}
