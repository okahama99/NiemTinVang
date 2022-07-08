package com.ntv.ntvcons_backend.dtos.worker;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateOptionDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationUpdateOptionDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerUpdateDTO extends BaseCreateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long workerId;

    @ApiModelProperty(example = "Nguyen Van A") /* Hint for Swagger */
    @Size(max = 100, message = "taskName max length: 100 characters")
    @NotNull(message = "fullName REQUIRED for update")
    private String fullName;

    @ApiModelProperty(example = "xxxxxxxxx") /* Hint for Swagger */
    @Size(max = 20, message = "taskName max length: 20 characters")
    @NotNull(message = "citizenId REQUIRED for update")
    private String citizenId;

    @ApiModelProperty(example = "xxxxxxxxx") /* Hint for Swagger */
    @Size(max = 20, message = "taskName max length: 20 characters")
    @NotNull(message = "socialSecurityCode REQUIRED for update")
    private String socialSecurityCode;

    private LocationUpdateOptionDTO address;
}