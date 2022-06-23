package com.ntv.ntvcons_backend.dtos;

import com.ntv.ntvcons_backend.constants.Regex;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseCreateDTO implements Serializable {
    @Positive
    @NotNull(message = "userId (createBy) REQUIRED for create")
    private Long createdBy;

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private static final LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private static final Boolean isDeleted = false;
}
