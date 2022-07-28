package com.ntv.ntvcons_backend.dtos.projectWorker;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWorkerCreateDTO extends BaseCreateDTO {
    @ApiModelProperty(notes = "NOT NULL; Id >= 0") /* Hint for Swagger */
    @PositiveOrZero
    @NotNull(message = "projectId REQUIRED for Create")
    private Long projectId;

    @ApiModelProperty(notes = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "workerId REQUIRED for Create")
    private Long workerId;

    /** yyyy-MM-dd HH:mm */
    /* TODO: use later or skip forever */
//    /@ApiModelProperty(example = "yyyy-MM-dd HH:mm") /* Hint for Swagger */
//    @Pattern(regexp = Regex.DATETIME_REGEX_1, message = "Need to match pattern 'yyyy-MM-dd HH:mm'")
//    @NotNull(message = "assignDate REQUIRED for Create")
//    private String assignDate;

    @JsonIgnore /* No serialize/deserialize */
    @ApiModelProperty(hidden = true) /* No show on swagger */
    private final LocalDateTime assignDate = LocalDateTime.now();
}
