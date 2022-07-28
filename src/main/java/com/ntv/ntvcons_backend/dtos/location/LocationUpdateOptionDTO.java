package com.ntv.ntvcons_backend.dtos.location;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateOptionDTO implements Serializable {
    @ApiModelProperty(example = "UPDATE_EXISTING_LOCATION_USED") /* Hint for Swagger */
    @NotNull(message = "updateOption REQUIRED for Update")
    private UpdateOption updateOption;

    private LocationCreateDTO newLocation;

    @Positive
    private Long existingLocationId;

    private LocationUpdateDTO updatedLocation;

    public enum UpdateOption {
        CREATE_NEW_LOCATION,
        SELECT_NEW_EXISTING_LOCATION,
        UPDATE_EXISTING_LOCATION_USED
    }
}
