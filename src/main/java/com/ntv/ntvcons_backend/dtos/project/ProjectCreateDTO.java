package com.ntv.ntvcons_backend.dtos.project;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateDTO extends BaseCreateDTO {
    @Size(max = 100, message = "projectName max length: 100 characters")
    private String projectName;

    @NotNull(message = "location REQUIRED for create")
    private LocationCreateDTO location;

//    private BlueprintCreateDTO blueprint;

    /** yyyy-MM-dd HH:mm */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "planStartDate REQUIRED for create")
    private String planStartDate;

    /** yyyy-MM-dd HH:mm */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String planEndDate;

    @Positive
    private Double estimatedCost;

    /** yyyy-MM-dd HH:mm */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String actualStartDate;

    /** yyyy-MM-dd HH:mm */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String actualEndDate;

    @Positive
    private Double actualCost;
}
