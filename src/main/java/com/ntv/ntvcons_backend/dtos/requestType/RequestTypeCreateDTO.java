package com.ntv.ntvcons_backend.dtos.requestType;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.lang.NonNull;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeCreateDTO implements Serializable {
    private String requestTypeName;
    private String requestTypeDesc;

    /* TODO: to be replace with status */
    @JsonIgnore /* No serialize/deserialize => no accept input */
    @ApiModelProperty(hidden = true) /* No show on Swagger */
    private final Boolean isDeleted = false;
}
