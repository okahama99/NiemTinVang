package com.ntv.ntvcons_backend.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestVerifyDTO extends BaseUpdateDTO {
    @Schema(description = "NOT NULL; Id > 0") /* Hint for Swagger */
    @Positive
    @NotNull(message = "Id REQUIRED for Update")
    private Long requestId;

    /* Verifying */
    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private Long verifierId = null;

    @Schema(example = "Chi tiêu hợp lý, đồng ý",
            description = "NOTNULL; size <= 100") /* Hint for Swagger */
    @NotNull(message = "verifyNote REQUIRED for Verifying")
    @Size(max = 100, message = "verifyNote max length: 100 characters")
    private String verifyNote;

    @Schema(description = "NOT NULL")
    @NotNull(message = "isApproved REQUIRED for Verifying")
    private Boolean isApproved;
}
