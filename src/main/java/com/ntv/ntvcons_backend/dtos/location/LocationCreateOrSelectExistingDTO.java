package com.ntv.ntvcons_backend.dtos.location;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreateOrSelectExistingDTO implements Serializable {
    @ApiModelProperty(example = "true") /* Hint for Swagger */
    @NotNull(message = "isNewLocation REQUIRED for create Location or not")
    private Boolean isNewLocation;

    @ApiModelProperty(example = "null") /* Hint for Swagger */
    @Positive
    private Long existingLocationId;

    private LocationCreateDTO newLocation;
}
