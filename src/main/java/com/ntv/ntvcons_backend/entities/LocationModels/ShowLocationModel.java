package com.ntv.ntvcons_backend.entities.LocationModels;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ShowLocationModel {
    private long locationId;
    private String addressNumber, street, ward, district, city, province, coordinate;
    private Long createdBy, updatedBy;
    private Date createdAt, updatedAt;
    private double totalPage;
}
