package com.ntv.ntvcons_backend.dtos.location;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationReadDTO extends BaseReadDTO {
    private Long locationId;
    private String addressNumber;
    private String street;
    private String area;
    private String ward;
    private String district;
    private String city;
    private String province;
    private String country;

    private String coordinate;
}
