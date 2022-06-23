package com.ntv.ntvcons_backend.dtos.blueprint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
