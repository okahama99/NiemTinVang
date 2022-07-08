package com.ntv.ntvcons_backend.dtos.reportDetail;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailCreateDTO extends BaseCreateDTO {
    @Positive
    @NotNull(message = "reportId REQUIRED for create")
    private Long reportId;

    @ApiModelProperty(example = "00.00") /* Hint for Swagger */
    @Positive
    @NotNull(message = "itemAmount REQUIRED for create")
    private Double itemAmount;

    @ApiModelProperty(example = "Viên") /* Hint for Swagger */
    @Size(max = 50, message = "itemUnit max length: 50 characters")
    @NotNull(message = "itemUnit REQUIRED for create")
    private String itemUnit;

    @ApiModelProperty(example = "Gạch") /* Hint for Swagger */
    @Size(max = 100, message = "itemUnit max length: 100 characters")
    @NotNull(message = "itemDesc REQUIRED for create")
    private String itemDesc;

    @ApiModelProperty(example = "00.00") /* Hint for Swagger */
    @Positive
    @NotNull(message = "itemPrice REQUIRED for create")
    private Double itemPrice;
}
