package com.ntv.ntvcons_backend.dtos.blueprint;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
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
    @NotNull(message = "blueprintId REQUIRED for create")
    private Long blueprintId;

    @Positive
    @NotNull(message = "projectId REQUIRED for create")
    private Long projectId;

    @Size(max = 100, message = "designerName max length: 100 characters")
    private String designerName;

    @Size(max = 100, message = "blueprintName max length: 100 characters")
    private String blueprintName;

    @Positive
    private Double estimatedCost;
}
