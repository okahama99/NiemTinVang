package com.ntv.ntvcons_backend.entities.projectModels;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class ShowProjectModel {
    private int projectId, locationId, blueprintId;
    private String projectName;
    private Timestamp startDate, endDate;
    private Double estimateCost;
    private Long createdBy, updatedBy;
    private Date createdAt, updatedAt;
    private double totalPage;
}
