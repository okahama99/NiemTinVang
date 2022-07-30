package com.ntv.ntvcons_backend.entities.ProjectModels;

import com.ntv.ntvcons_backend.constants.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectModel {
    /* Tạm đối sang Long, Double đẻ có thể nhập null, bị bể do đổi blueprint */
    private Long projectId, locationId, blueprintId, userId;
    private String projectName, addressNumber, street, area, ward, district, city, province, country, coordinate, designerName, blueprintName;
    private LocalDateTime planStartDate, planEndDate, actualStartDate, actualEndDate;
    private Double projectEstimateCost, actualCost, blueprintEstimateCost;
    private Status status;
    private Long createdBy, updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private Double totalPage;
}
