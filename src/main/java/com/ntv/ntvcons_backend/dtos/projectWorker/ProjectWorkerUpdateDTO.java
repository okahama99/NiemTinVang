package com.ntv.ntvcons_backend.dtos.projectWorker;

import com.ntv.ntvcons_backend.constants.Regex;
import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWorkerUpdateDTO extends BaseUpdateDTO {
    @Schema(description = "NOT NULL; Id >= 0") /* Hint for Swagger */
    @PositiveOrZero
    @NotNull(message = "Id REQUIRED for Update")
    private Long projectWorkerId;

    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "projectId REQUIRED for Update")
    private Long projectId;

    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "workerId REQUIRED for Update")
    private Long workerId;

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd HH:mm", description = "Nullable") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String assignDate;

    /** yyyy-MM-dd HH:mm */
    @Schema(example = "yyyy-MM-dd HH:mm", description = "Nullable") /* Hint for Swagger */
    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
    private String removeDate;
}
