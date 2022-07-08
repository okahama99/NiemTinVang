package com.ntv.ntvcons_backend.dtos.location;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreateOptionDTO implements Serializable {
    @ApiModelProperty(example = "CREATE_NEW_LOCATION") /* Hint for Swagger */
    @NotNull(message = "createOption REQUIRED for create Location or not")
    private CreateOption createOption;

    private LocationCreateDTO newLocation;

    @Positive
    private Long existingLocationId;

    @ApiIgnore
    public enum CreateOption {
        CREATE_NEW_LOCATION,
        SELECT_EXISTING_LOCATION
    }
}
