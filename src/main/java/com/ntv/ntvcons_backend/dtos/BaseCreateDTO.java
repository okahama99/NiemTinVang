package com.ntv.ntvcons_backend.dtos;

import com.ntv.ntvcons_backend.constants.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseCreateDTO implements Serializable {
    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private Long createdBy = 4L;

    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private final LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private final Status status = Status.ACTIVE;

    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private final Boolean isDeleted = false;
}
