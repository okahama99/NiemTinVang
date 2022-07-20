package com.ntv.ntvcons_backend.dtos.blueprint;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.annotations.ApiModelProperty;
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
public class BlueprintCreateDTO extends BaseCreateDTO {
    @ApiModelProperty(notes = "NOT NULL; Id >= 0") /* Hint for Swagger */
    @PositiveOrZero
    @NotNull(message = "projectId REQUIRED for create")
    private Long projectId;

    @ApiModelProperty(example = "Nguyen Van A",
            notes = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "designerName max length: 100 characters")
    @NotNull(message = "designerName REQUIRED for create")
    private String designerName;

    @ApiModelProperty(example = "Bản vẽ xxx",
            notes = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "blueprintName max length: 100 characters")
    @NotNull(message = "blueprintName REQUIRED for create")
    private String blueprintName;

    @ApiModelProperty(example = "00.00",
            notes = "NOT NULL; cost > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "estimatedCost REQUIRED for create")
    private Double estimatedCost;
}
