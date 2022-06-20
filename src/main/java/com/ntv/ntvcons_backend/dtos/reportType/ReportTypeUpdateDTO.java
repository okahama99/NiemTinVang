package com.ntv.ntvcons_backend.dtos.reportType;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTypeUpdateDTO implements Serializable {
    private Long reportTypeId;
    private String reportTypeName;
    private String reportTypeDesc;

    /* TODO: to be replace with status */
    @JsonIgnore /* No serialize/deserialize => no accept input */
    @ApiModelProperty(hidden = true) /* No show on Swagger */
    private final Boolean isDeleted = false;
}
