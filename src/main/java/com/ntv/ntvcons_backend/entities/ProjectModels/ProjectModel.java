package com.ntv.ntvcons_backend.entities.ProjectModels;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class ProjectModel {
    private long projectId, locationId, userId;
    private String projectName, addressNumber, street, area, ward, district, city, province, country, coordinate;
    private Instant planStartDate, planEndDate, actualStartDate, actualEndDate;
    private Double projectEstimateCost, actualCost;
    private boolean isDelete;
    private Long createdBy, updatedBy;
    private Date createdAt, updatedAt;
    private double totalPage;
}
