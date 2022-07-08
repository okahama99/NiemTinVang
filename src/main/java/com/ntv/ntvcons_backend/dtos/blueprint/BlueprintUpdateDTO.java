package com.ntv.ntvcons_backend.dtos.blueprint;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlueprintUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long blueprintId;

    @Positive
    @NotNull(message = "projectId REQUIRED for update")
    private Long projectId;

    @ApiModelProperty(example = "Nguyen Van A") /* Hint for Swagger */
    @Size(max = 100, message = "designerName max length: 100 characters")
    @NotNull(message = "designerName REQUIRED for update")
    private String designerName;

    @ApiModelProperty(example = "Bản vẽ xxx") /* Hint for Swagger */
    @Size(max = 100, message = "blueprintName max length: 100 characters")
    @NotNull(message = "projectId REQUIRED for update")
    private String blueprintName;

    @ApiModelProperty(example = "00.00") /* Hint for Swagger */
    @Positive
    @NotNull(message = "estimatedCost REQUIRED for update")
    private Double estimatedCost;
}
