package com.ntv.ntvcons_backend.entities.BlueprintModels;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateBlueprintModel {
    private long blueprintId,userId;
    private String designerName, blueprintName;
    private double estimateCost;
    private Date updatedAt;
}
