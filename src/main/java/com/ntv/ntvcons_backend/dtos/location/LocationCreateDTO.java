package com.ntv.ntvcons_backend.dtos.location;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreateDTO extends BaseCreateDTO {
    @Size(max = 100, message = "addressNumber max length: 100 characters")
    private String addressNumber;

    @Size(max = 100, message = "street max length: 100 characters")
    private String street;

    /** *Optional */
    @Size(max = 100, message = "area max length: 100 characters")
    private String area;

    @Size(max = 100, message = "ward max length: 100 characters")
    private String ward;

    @Size(max = 100, message = "district max length: 100 characters")
    private String district;

    @Size(max = 100, message = "city max length: 100 characters")
    private String city;

    @Size(max = 100, message = "province max length: 100 characters")
    private String province;

    @Size(max = 100, message = "country max length: 100 characters")
    private String country;

    /** <b>*Required</b> */
    @NotNull(message = "coordinate REQUIRED for create")
    @Size(max = 100, message = "coordinate max length: 100 characters")
    private String coordinate;
}
