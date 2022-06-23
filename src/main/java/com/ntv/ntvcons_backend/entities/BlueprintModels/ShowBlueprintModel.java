package com.ntv.ntvcons_backend.entities.BlueprintModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ShowBlueprintModel {
    /* Tạm đối sang Long, Double đẻ có thể nhập null, bị bể do đổi blueprint */
    private Long projectBlueprintId;
    private String projectBlueprintName, designerName;
    private Double projectBlueprintCost;
    private Long createdBy, updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private Double totalPage;
}
