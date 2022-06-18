package com.ntv.ntvcons_backend.entities.ProjectModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class CreateProjectModel {
    /* Tạm đối sang Long, Double đẻ có thể nhập null, bị bể do đổi blueprint */
    private Long userId;
    private String projectName;
    private LocalDate planStartDate,planEndDate,actualStartDate,actualEndDate; // đổi qua LocalDate để FE dùng library đẩy data xuống
    private Double projectEstimateCost, projectActualCost;
    private String designerName, projectBlueprintName;
    private Double blueprintEstimateCost;
    private String addressNumber, street, area, ward, district, city, province, country, coordinate;
}
