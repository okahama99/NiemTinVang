package com.ntv.ntvcons_backend.dtos.requestType;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeCreateDTO extends BaseCreateDTO {
    @ApiModelProperty(example = "Yêu cầu chi tiêu") /* Hint for Swagger */
    @Size(max = 100, message = "requestTypeName max length: 100 characters")
    @NotNull(message = "requestTypeName REQUIRED for create")
    private String requestTypeName;

    @ApiModelProperty(example = "Yêu cầu chi tiêu") /* Hint for Swagger */
    @Size(max = 100, message = "requestTypeDesc max length: 100 characters")
    private String requestTypeDesc;
}
