package com.ntv.ntvcons_backend.dtos.requestType;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long requestTypeId;

    @ApiModelProperty(example = "Yêu cầu chi tiêu") /* Hint for Swagger */
    @Size(max = 100, message = "requestTypeName max length: 100 characters")
    @NotNull(message = "requestTypeName REQUIRED for update")
    private String requestTypeName;

    @ApiModelProperty(example = "Yêu cầu chi tiêu") /* Hint for Swagger */
    @Size(max = 100, message = "requestTypeDesc max length: 100 characters")
    private String requestTypeDesc;
}
