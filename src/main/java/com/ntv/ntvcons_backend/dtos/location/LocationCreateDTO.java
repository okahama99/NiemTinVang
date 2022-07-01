package com.ntv.ntvcons_backend.dtos.location;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreateDTO extends BaseCreateDTO {
    @ApiModelProperty(example = "Lô E2a-7") /* Hint for Swagger */
    @Size(max = 100, message = "addressNumber max length: 100 characters")
    private String addressNumber;

    @ApiModelProperty(example = "D1") /* Hint for Swagger */
    @Size(max = 100, message = "street max length: 100 characters")
    private String street;

    /** *Optional */
    @ApiModelProperty(example = "Khu công nghệ cao Q9") /* Hint for Swagger */
    @Size(max = 100, message = "area max length: 100 characters")
    private String area;

    @ApiModelProperty(example = "Long Thạnh Mỹ") /* Hint for Swagger */
    @Size(max = 100, message = "ward max length: 100 characters")
    private String ward;

    @ApiModelProperty(example = "TP.Thủ Đức") /* Hint for Swagger */
    @Size(max = 100, message = "district max length: 100 characters")
    private String district;

    @ApiModelProperty(example = "TP.Thủ Đức") /* Hint for Swagger */
    @Size(max = 100, message = "city max length: 100 characters")
    private String city;

    @ApiModelProperty(example = "TP.HCM") /* Hint for Swagger */
    @Size(max = 100, message = "province max length: 100 characters")
    private String province;

    @ApiModelProperty(example = "Việt Nam") /* Hint for Swagger */
    @Size(max = 100, message = "country max length: 100 characters")
    private String country;

    /** <b>*Required</b> */
    @ApiModelProperty(example = "10.841139984351623, 106.80988203281531") /* Hint for Swagger */
    @NotNull(message = "coordinate REQUIRED for create")
    @Size(max = 100, message = "coordinate max length: 100 characters")
    private String coordinate;
}
