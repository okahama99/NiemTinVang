package com.ntv.ntvcons_backend.dtos.project;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateOptionDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateDTO extends BaseCreateDTO {
    @ApiModelProperty(example = "Dự án xxx") /* Hint for Swagger */
    @Size(max = 100, message = "projectName max length: 100 characters")
    @NotNull(message = "projectName REQUIRED for create")
    private String projectName;

    @NotNull(message = "location REQUIRED for create")
    private LocationCreateOptionDTO location;

    @NotNull(message = "blueprint REQUIRED for create")
    private BlueprintCreateDTO blueprint;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "planStartDate REQUIRED for create")
    private String planStartDate;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String planEndDate;

    @ApiModelProperty(example = "00.00") /* Hint for Swagger */
    @Positive
    private Double estimatedCost;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String actualStartDate;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String actualEndDate;

    @ApiModelProperty(example = "00.00") /* Hint for Swagger */
    @Positive
    private Double actualCost;
}
