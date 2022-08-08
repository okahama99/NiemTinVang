package com.ntv.ntvcons_backend.dtos.location;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreateOptionDTO implements Serializable {
    @Schema(example = "CREATE_NEW_LOCATION") /* Hint for Swagger */
    @NotNull(message = "createOption REQUIRED for Create Location or not")
    private CreateOption createOption;

    private LocationCreateDTO newLocation;

    @Positive
    private Long existingLocationId;

    @Hidden
    public enum CreateOption {
        CREATE_NEW_LOCATION,
        SELECT_EXISTING_LOCATION
    }
}
