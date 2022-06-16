package com.ntv.ntvcons_backend.entities.BlueprintModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBlueprintModel {
    private String designerName, projectBlueprintName, image;
    private double estimateCost;
}
