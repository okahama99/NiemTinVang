package com.ntv.ntvcons_backend.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateDTO extends BaseCreateDTO {
    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "projectId REQUIRED for Create")
    private Long projectId;

    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "requestTypeId REQUIRED for Create")
    private Long requestTypeId;

    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private Long RequesterId = null;

    @Schema(example = "Yêu cầu xxx",
            description = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "requestName max length: 100 characters")
    @NotNull(message = "requestName REQUIRED for Create")
    private String requestName;

    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private final LocalDateTime requestDate = LocalDateTime.now();

    @Schema(example = "Yêu cầu chi tiêu xxx",
            description = "NOT NULL; size <= 100") /* Hint for Swagger */
    @Size(max = 100, message = "requestDesc max length: 100 characters")
    @NotNull(message = "requestDesc REQUIRED for Create")
    private String requestDesc;

    @Schema(description = "NOT NULL; size >= 1") /* Hint for Swagger */
    @Size(min = 1, message = "Needed at least 1 detail")
    @NotNull(message = "requestDetailList REQUIRED for Create")
    private List<RequestDetailCreateDTO> requestDetailList;
}
