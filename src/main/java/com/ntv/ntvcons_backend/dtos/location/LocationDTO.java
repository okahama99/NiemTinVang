package com.ntv.ntvcons_backend.dtos.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO implements Serializable {
    private Integer locationId;
    private String addressNumber;
    private String street;
    private String area;
    private String ward;
    private String district;
    private String city;
    private String province;
    private String country;
    private String coordinate;
    private Boolean isDeleted = false;
}
