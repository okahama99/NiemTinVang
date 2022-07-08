package com.ntv.ntvcons_backend.dtos.requestDetail;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import com.ntv.ntvcons_backend.dtos.request.RequestReadDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetailUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long reportDetailId;

    @Positive
    @NotNull(message = "reportId REQUIRED for update")
    private Long reportId;

    @ApiModelProperty(example = "00.00") /* Hint for Swagger */
    @Positive
    @NotNull(message = "itemAmount REQUIRED for update")
    private Double itemAmount;

    @ApiModelProperty(example = "Viên") /* Hint for Swagger */
    @Size(max = 50, message = "itemUnit max length: 50 characters")
    @NotNull(message = "itemUnit REQUIRED for update")
    private String itemUnit;

    @ApiModelProperty(example = "Gạch") /* Hint for Swagger */
    @Size(max = 100, message = "itemUnit max length: 100 characters")
    @NotNull(message = "itemDesc REQUIRED for update")
    private String itemDesc;

    @ApiModelProperty(example = "00.00") /* Hint for Swagger */
    @Positive
    @NotNull(message = "itemPrice REQUIRED for update")
    private Double itemPrice;
}
