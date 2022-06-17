package com.ntv.ntvcons_backend.entities.BlueprintModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBlueprintModel {
    /* Tạm đối sang Long, Double đẻ có thể nhập null, bị bể do đổi blueprint */
    private Long blueprintId,userId;
    private String designerName, blueprintName;
    private Double estimateCost;
}
