package com.ntv.ntvcons_backend.dtos.location;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreateDTO extends BaseCreateDTO {
    @Schema(example = "Lô E2a-7",
            description = "Nullable, size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "addressNumber max length: 100 characters")
    private String addressNumber;

    @Schema(example = "D1",
            description = "Nullable, size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "street max length: 100 characters")
    private String street;

    /** *Optional */
    @Schema(example = "Khu công nghệ cao Q9",
            description = "Nullable, size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "area max length: 100 characters")
    private String area;

    @Schema(example = "Long Thạnh Mỹ",
            description = "Nullable, size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "ward max length: 100 characters")
    private String ward;

    @Schema(example = "TP.Thủ Đức",
            description = "Nullable, size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "district max length: 100 characters")
    private String district;

    @Schema(example = "TP.Thủ Đức",
            description = "Nullable, size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "city max length: 100 characters")
    private String city;

    @Schema(example = "TP.HCM",
            description = "Nullable, size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "province max length: 100 characters")
    private String province;

    @Schema(example = "Việt Nam",
            description = "Nullable, size <= 100 (if not null)") /* Hint for Swagger */
    @Size(max = 100, message = "country max length: 100 characters")
    private String country;

    /** <b>*Required</b> */
    @Schema(example = "10.841139984351623, 106.80988203281531",
            description = "NOT NULL, size <= 100") /* Hint for Swagger */
    @NotNull(message = "coordinate REQUIRED for Create")
    @Size(max = 100, message = "coordinate max length: 100 characters")
    private String coordinate;
}
