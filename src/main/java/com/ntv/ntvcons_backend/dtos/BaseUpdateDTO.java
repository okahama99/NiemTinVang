package com.ntv.ntvcons_backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ntv.ntvcons_backend.constants.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseUpdateDTO implements Serializable {
    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private Long updatedBy = null; /* accAdmin */

    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private final LocalDateTime updatedAt = LocalDateTime.now();

    /* TODO: replace later or dynamic change */
    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private Status status = Status.ACTIVE;
}
