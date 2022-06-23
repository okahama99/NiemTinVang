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
public abstract class BaseUpdateDTO implements Serializable {
    @Positive
    @NotNull(message = "userId (createBy) REQUIRED for update")
    private Long updatedBy;

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private static final LocalDateTime updatedAt = LocalDateTime.now();

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private static final Boolean isDeleted = false;
}
