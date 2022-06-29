package com.ntv.ntvcons_backend.dtos.blueprint;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlueprintReadDTO extends BaseReadDTO {
    private Long blueprintId;
    private Long projectId;
    private String designerName;
    private String blueprintName;
    private Double estimatedCost;
}
