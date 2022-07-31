package com.ntv.ntvcons_backend.dtos.blueprint;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
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
public class BlueprintCreateDTO extends BaseCreateDTO {
    @Schema(description = "NOT NULL; Id >= 0") /* Hint for Swagger */
    @PositiveOrZero
    @NotNull(message = "projectId REQUIRED for Create")
    private Long projectId;

    @Schema(example = "Nguyen Van A",
            description = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "designerName max length: 100 characters")
    @NotNull(message = "designerName REQUIRED for Create")
    private String designerName;

    @Schema(example = "Bản vẽ xxx",
            description = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "blueprintName max length: 100 characters")
    @NotNull(message = "blueprintName REQUIRED for Create")
    private String blueprintName;

    @Schema(example = "00.00",
            description = "NOT NULL; cost > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "estimatedCost REQUIRED for Create")
    private Double estimatedCost;
}
