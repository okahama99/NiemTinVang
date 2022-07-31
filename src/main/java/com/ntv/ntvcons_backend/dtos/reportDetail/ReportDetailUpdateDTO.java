package com.ntv.ntvcons_backend.dtos.reportDetail;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailUpdateDTO extends BaseUpdateDTO {
    @PositiveOrZero
    @NotNull(message = "Id REQUIRED for Update")
    private Long reportDetailId;

    @Positive
    @NotNull(message = "reportId REQUIRED for Update")
    private Long reportId;

    @Schema(example = "00.00") /* Hint for Swagger */
    @Positive
    @NotNull(message = "itemAmount REQUIRED for Update")
    private Double itemAmount;

    @Schema(example = "Viên") /* Hint for Swagger */
    @Size(max = 50, message = "itemUnit max length: 50 characters")
    @NotNull(message = "itemUnit REQUIRED for Update")
    private String itemUnit;

    @Schema(example = "Gạch") /* Hint for Swagger */
    @Size(max = 100, message = "itemUnit max length: 100 characters")
    @NotNull(message = "itemDesc REQUIRED for Update")
    private String itemDesc;

    @Schema(example = "00.00") /* Hint for Swagger */
    @Positive
    @NotNull(message = "itemPrice REQUIRED for Update")
    private Double itemPrice;
}
