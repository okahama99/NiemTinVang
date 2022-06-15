package com.ntv.ntvcons_backend.entities.ProjectModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ProjectModel {
    private long projectId, locationId, blueprintId, userId;
    private String projectName, addressNumber, street, area, ward, district, city, province, country, coordinate, designerName, blueprintName;
    private LocalDateTime planStartDate, planEndDate, actualStartDate, actualEndDate;
    private Double projectEstimateCost, actualCost, blueprintEstimateCost;
    private boolean isDelete;
    private Long createdBy, updatedBy;
    private Date createdAt, updatedAt;
    private double totalPage;
}
