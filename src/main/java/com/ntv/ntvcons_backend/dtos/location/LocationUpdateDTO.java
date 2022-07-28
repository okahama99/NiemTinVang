package com.ntv.ntvcons_backend.dtos.location;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateDTO extends BaseUpdateDTO {
    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "Id REQUIRED for Update")
    private Long locationId;

    @ApiModelProperty(example = "Lô E2a-7",
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "addressNumber max length: 100 characters")
    private String addressNumber;

    @ApiModelProperty(example = "D1",
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "street max length: 100 characters")
    private String street;

    /** *Optional */
    @ApiModelProperty(example = "Khu công nghệ cao Q9",
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "area max length: 100 characters")
    private String area;

    @ApiModelProperty(example = "Long Thạnh Mỹ",
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "ward max length: 100 characters")
    private String ward;

    @ApiModelProperty(example = "TP.Thủ Đức",
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "district max length: 100 characters")
    private String district;

    @ApiModelProperty(example = "TP.Thủ Đức",
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "city max length: 100 characters")
    private String city;

    @ApiModelProperty(example = "TP.HCM",
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "province max length: 100 characters")
    private String province;

    @ApiModelProperty(example = "Việt Nam",
            notes = "Nullable; size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "country max length: 100 characters")
    private String country;

    /** <b>*Required</b> */
    @ApiModelProperty(example = "10.841139984351623, 106.80988203281531",
            notes = "NOT NULL; size <= 100") /* Hint for Swagger */
    @NotNull(message = "Coordinate REQUIRED for Update")
    @Size(max = 100, message = "coordinate max length: 100 characters")
    private String coordinate;
}
