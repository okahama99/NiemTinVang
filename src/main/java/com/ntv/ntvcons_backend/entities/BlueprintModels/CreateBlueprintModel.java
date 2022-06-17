package com.ntv.ntvcons_backend.entities.BlueprintModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBlueprintModel {
    /* Tạm đối sang Long, Double đẻ có thể nhập null, bị bể do đổi blueprint */
    private String designerName, projectBlueprintName;
    private Double estimateCost;
}
