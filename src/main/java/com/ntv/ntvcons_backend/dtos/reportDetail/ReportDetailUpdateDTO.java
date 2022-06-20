package com.ntv.ntvcons_backend.dtos.reportDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailUpdateDTO implements Serializable {
    private Long reportDetailId;
    private Long reportId;
    private String itemDesc;
    private Double itemAmount;
    private String itemUnit;
    private Double itemPrice;

    /* TODO: to be replace with status */
    @JsonIgnore /* No serialize/deserialize => no accept input */
    @ApiModelProperty(hidden = true) /* No show on Swagger */
    private final Boolean isDeleted = false;
}
