package com.ntv.ntvcons_backend.entities.ProjectModels;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateProjectModel {
    /* Tạm đối sang Long, Double đẻ có thể nhập null, bị bể do đổi blueprint */
    private Long userId;
    private String projectName;
    //private LocalDateTime planStartDate,planEndDate,actualStartDate,actualEndDate;
    private String planStartDate,planEndDate,actualStartDate,actualEndDate; // đổi qua String để FE đẩy data xuống
    private Double projectEstimateCost, projectActualCost;
    private String designerName, projectBlueprintName;
    private Double blueprintEstimateCost;
    private String addressNumber, street, area, ward, district, city, province, country, coordinate;
}
