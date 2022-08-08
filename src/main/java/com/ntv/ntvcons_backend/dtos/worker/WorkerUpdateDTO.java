package com.ntv.ntvcons_backend.dtos.worker;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for Update")
    private Long workerId;

    @Schema(example = "Nguyen Van A") /* Hint for Swagger */
    @Size(max = 100, message = "fullName max length: 100 characters")
    @NotNull(message = "fullName REQUIRED for Update")
    private String fullName;

    @Schema(example = "xxxxxxxxx") /* Hint for Swagger */
    @Size(max = 20, message = "citizenId max length: 20 characters")
    @NotNull(message = "citizenId REQUIRED for Update")
    private String citizenId;

    @Schema(example = "xxxxxxxxx") /* Hint for Swagger */
    @Size(max = 100, message = "socialSecurityCode max length: 100 characters")
    @NotNull(message = "socialSecurityCode REQUIRED for Update")
    private String socialSecurityCode;

    /* TODO: to reuse later
    private LocationUpdateOptionDTO address;*/

    private LocationUpdateDTO address;
}