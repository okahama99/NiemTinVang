package com.ntv.ntvcons_backend.dtos.blueprint;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "Id REQUIRED for Update")
    private Long blueprintId;

    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "projectId REQUIRED for Update")
    private Long projectId;

    @Schema(example = "Nguyen Van A",
            description = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "designerName max length: 100 characters")
    @NotNull(message = "designerName REQUIRED for Update")
    private String designerName;

    @Schema(example = "Bản vẽ xxx",
            description = "NOT NULL; size <= 100") /* Hint for Swagger */
    @NotNull(message = "projectId REQUIRED for Update")
    private String blueprintName;

    @Schema(example = "00.00",
            description = "NOT NULL; cost > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "estimatedCost REQUIRED for Update")
    private Double estimatedCost;
}
