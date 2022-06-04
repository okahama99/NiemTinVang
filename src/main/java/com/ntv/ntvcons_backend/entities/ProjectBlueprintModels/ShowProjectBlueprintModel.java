package com.ntv.ntvcons_backend.entities.ProjectBlueprintModels;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ShowProjectBlueprintModel {
    private int projectBlueprintId, designerId;
    private String projectBlueprintName;
    private double projectBlueprintCost;
    private Long createdBy, updatedBy;
    private Date createdAt, updatedAt;
    private double totalPage;
}
