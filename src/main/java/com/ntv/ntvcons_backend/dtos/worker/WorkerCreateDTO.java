package com.ntv.ntvcons_backend.dtos.worker;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateOptionDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerCreateDTO extends BaseCreateDTO {
    @ApiModelProperty(example = "Nguyen Van A") /* Hint for Swagger */
    @Size(max = 100, message = "fullName max length: 100 characters")
    @NotNull(message = "fullName REQUIRED for create")
    private String fullName;

    @ApiModelProperty(example = "xxxxxxxxx") /* Hint for Swagger */
    @Size(max = 20, message = "citizenId max length: 20 characters")
    @NotNull(message = "citizenId REQUIRED for create")
    private String citizenId;

    @ApiModelProperty(example = "xxxxxxxxx") /* Hint for Swagger */
    @Size(max = 100, message = "socialSecurityCode max length: 100 characters")
    @NotNull(message = "socialSecurityCode REQUIRED for create")
    private String socialSecurityCode;

    /* TODO: to reuse later
    @NotNull(message = "address REQUIRED for create")
    private LocationCreateOptionDTO address;*/

    @NotNull(message = "address REQUIRED for create")
    private LocationCreateDTO address;
}