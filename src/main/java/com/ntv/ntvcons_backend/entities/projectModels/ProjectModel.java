package com.ntv.ntvcons_backend.entities.projectModels;

import java.sql.Timestamp;

public class ProjectModel {
    private int projectId, locationId, blueprintId;
    private String projectName;
    private Timestamp startDate, endDate;
    private Double estimateCost;
}
