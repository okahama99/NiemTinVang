package com.ntv.ntvcons_backend.dtos;

import com.ntv.ntvcons_backend.constants.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseCreateDTO implements Serializable {
    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private final Long createdBy = 4L;
    /* TODO: to be replace when login done
    @Positive
    @NotNull(message = "userId (createdBy) REQUIRED for create")
    private Long createdBy; */

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private final LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private final Status status = Status.ACTIVE;

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private final Boolean isDeleted = false;
}
