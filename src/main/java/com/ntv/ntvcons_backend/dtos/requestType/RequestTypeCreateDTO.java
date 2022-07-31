package com.ntv.ntvcons_backend.dtos.requestType;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeCreateDTO extends BaseCreateDTO {
    @Schema(example = "Yêu cầu chi tiêu") /* Hint for Swagger */
    @Size(max = 100, message = "requestTypeName max length: 100 characters")
    @NotNull(message = "requestTypeName REQUIRED for Create")
    private String requestTypeName;

    @Schema(example = "Yêu cầu chi tiêu") /* Hint for Swagger */
    @Size(max = 100, message = "requestTypeDesc max length: 100 characters")
    private String requestTypeDesc;
}
