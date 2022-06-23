package com.ntv.ntvcons_backend.entities.LocationModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ShowLocationModel {
    private long locationId;
    private String addressNumber, street, area, ward, district, city, province, country, coordinate;
    private Long createdBy, updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private double totalPage;
}
