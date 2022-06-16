package com.ntv.ntvcons_backend.entities.projectModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class CreateProjectModel {
    private String projectName;
    private LocalDateTime planStartDate,planEndDate,actualStartDate,actualEndDate;
    private double projectEstimateCost, projectActualCost;
    private String designerName, projectBlueprintName;
    private double blueprintEstimateCost;
    private String addressNumber, street, area, ward, district, city, province, country, coordinate;
}
