package com.ntv.ntvcons_backend.entities.ProjectModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProjectModel {
    private Long projectId;
    private String projectName;

    //private LocalDateTime planStartDate,planEndDate,actualStartDate,actualEndDate;
    // đổi qua String để FE đẩy data xuống
    /** yyyy-MM-dd HH:mm */
    private String planStartDate;
    /** yyyy-MM-dd HH:mm */
    private String planEndDate;
    /** yyyy-MM-dd HH:mm */
    private String actualStartDate;
    /** yyyy-MM-dd HH:mm */
    private String actualEndDate;

    private Double estimateCost;
    private Double actualCost;

    /** UpdateBy */
    private Long userId;

    private Long locationId;
    private String addressNumber, street, area, ward, district, city, province, country, coordinate;
}
