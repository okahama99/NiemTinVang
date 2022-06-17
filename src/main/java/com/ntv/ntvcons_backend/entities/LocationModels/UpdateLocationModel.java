package com.ntv.ntvcons_backend.entities.LocationModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLocationModel {
    private long locationId, userId;
    private String addressNumber, street, area, ward, district, city, province, country, coordinate;
}
