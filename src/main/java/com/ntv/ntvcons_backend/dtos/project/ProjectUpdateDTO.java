package com.ntv.ntvcons_backend.dtos.project;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintUpdateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateDTO extends BaseUpdateDTO {
    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "Id REQUIRED for Update")
    private Long projectId;

    @Schema(example = "Dự án xxx",
            description = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "projectName max length: 100 characters")
    @NotNull(message = "projectName REQUIRED for Update")
    private String projectName;

    /* TODO: reuse later
    private LocationUpdateOptionDTO location;*/
    @Schema(description = "Nullable") /* Hint for Swagger */
    private LocationUpdateDTO location;

    @Schema(description = "Nullable") /* Hint for Swagger */
    private BlueprintUpdateDTO blueprint;

    @Schema(description = "Nullable, size >= 1 (if not null)") /* Hint for Swagger */
    @Size(min = 1, message = "Need at least 1 for Update")
    private List<Long> managerIdList;

    @Schema(description = "Nullable, size >= 1 (if not null)") /* Hint for Swagger */
    @Size(min = 1, message = "Need at least 1 for Update")
    private List<Long> workerIdList;

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd HH:mm",
            description = "NOT NULL") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    @NotNull(message = "planStartDate REQUIRED for Create")
    private String planStartDate;

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd HH:mm",
            description = "Nullable; endDate >= startDate") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String planEndDate;

    @Schema(example = "00.00",
            description = "Nullable; cost > 0 (if not null)") /* Hint for Swagger */
    @Positive
    private Double estimatedCost;

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd HH:mm",
            description = "Nullable; date <= now") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String actualStartDate;

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd HH:mm",
            description = "Nullable; endDate >= startDate; date <= now") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String actualEndDate;

    @Schema(example = "00.00",
            description = "Nullable; cost > 0 (if not null)") /* Hint for Swagger */
    @Positive
    private Double actualCost;
}
