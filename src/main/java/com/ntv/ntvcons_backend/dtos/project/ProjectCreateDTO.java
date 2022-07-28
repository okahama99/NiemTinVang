package com.ntv.ntvcons_backend.dtos.project;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateOptionDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerCreateDTO;
import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerCreateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateDTO extends BaseCreateDTO {
    @ApiModelProperty(example = "Dự án xxx",
            notes = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "projectName max length: 100 characters")
    @NotNull(message = "projectName REQUIRED for Create")
    private String projectName;

    /* TODO: reuse later
    @NotNull(message = "location REQUIRED for Create")
    private LocationCreateOptionDTO location;*/

    @ApiModelProperty(notes = "NOT NULL") /* Hint for Swagger */
    @NotNull(message = "blueprint REQUIRED for Create")
    private LocationCreateDTO location;

    @ApiModelProperty(notes = "NOT NULL") /* Hint for Swagger */
    @NotNull(message = "blueprint REQUIRED for Create")
    private BlueprintCreateDTO blueprint;

    @ApiModelProperty(notes = "Nullable, size >= 1 (if not null)") /* Hint for Swagger */
    @Size(min = 1, message = "Need at least 1 for Create")
    private List<Long> managerIdList;

    @ApiModelProperty(notes = "Nullable, size >= 1 (if not null)") /* Hint for Swagger */
    @Size(min = 1, message = "Need at least 1 for Create")
    private List<Long> workerIdList;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "planStartDate REQUIRED for Create")
    private String planStartDate;

    /** yyyy-MM-dd HH:mm */
    @ApiModelProperty(example = "yyyy-MM-dd HH:mm",
            notes = "endDate >= startDate") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String planEndDate;

    @ApiModelProperty(example = "00.00",
            notes = "Nullable; cost > 0 (if not null)") /* Hint for Swagger */
    @Positive
    private Double estimatedCost;
}
