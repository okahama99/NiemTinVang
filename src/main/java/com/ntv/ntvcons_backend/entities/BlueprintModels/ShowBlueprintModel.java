package com.ntv.ntvcons_backend.entities.BlueprintModels;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ShowBlueprintModel {
    private long projectBlueprintId;
    private String projectBlueprintName, image, designerName;
    private double projectBlueprintCost;
    private Long createdBy, updatedBy;
    private Date createdAt, updatedAt;
    private double totalPage;
}
