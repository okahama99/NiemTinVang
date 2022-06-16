package com.ntv.ntvcons_backend.entities.BlueprintModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBluePrintModel {
    private String designerName, projectBlueprintName;
    private double estimateCost;
}
