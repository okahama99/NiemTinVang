package com.ntv.ntvcons_backend.dtos.worker;

import com.ntv.ntvcons_backend.constants.Gender;
import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerCreateDTO extends BaseCreateDTO {
    @Schema(example = "Nguyen Van A") /* Hint for Swagger */
    @Size(max = 100, message = "fullName max length: 100 characters")
    @NotNull(message = "fullName REQUIRED for Create")
    private String fullName;

    @Schema(example = "xxxxxxxxx") /* Hint for Swagger */
    @Size(max = 20, message = "citizenId max length: 20 characters")
    @NotNull(message = "citizenId REQUIRED for Create")
    private String citizenId;

    @Schema(example = "UNKNOWN") /* Hint for Swagger */
    private Gender gender = Gender.UNKNOWN; /* Default */

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATE_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd'")
    private String birthday;

    @Schema(example = "Saigon") /* Hint for Swagger */
    @Size(max = 100, message = "birthPlace max length: 100 characters")
    private String birthPlace;

    @Schema(example = "xxxxxxxxx") /* Hint for Swagger */
    @Size(max = 100, message = "socialSecurityCode max length: 100 characters")
//    @NotNull(message = "socialSecurityCode REQUIRED for Create")
    private String socialSecurityCode;

    /* TODO: to reuse later
    @NotNull(message = "address REQUIRED for Create")
    private LocationCreateOptionDTO address;*/

    @NotNull(message = "address REQUIRED for Create")
    private LocationCreateDTO address;
}