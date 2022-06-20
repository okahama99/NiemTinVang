package com.ntv.ntvcons_backend.dtos.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateDTO implements Serializable {
    private Long projectId;
    private String taskName;
    private String taskDesc;
    /** yyyy-MM-dd HH:mm */
    private String planStartDate;
    /** yyyy-MM-dd HH:mm */
    private String planEndDate;
    /** yyyy-MM-dd HH:mm */
    private String actualStartDate;
    /** yyyy-MM-dd HH:mm */
    private String actualEndDate;

    /* TODO: to be replace with status */
    @JsonIgnore /* No serialize/deserialize => no accept input */
    @ApiModelProperty(hidden = true) /* No show on Swagger */
    private final Boolean isDeleted = false;
}
