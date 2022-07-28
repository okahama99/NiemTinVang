package com.ntv.ntvcons_backend.dtos.reportType;

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
public class ReportTypeUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for Create")
    private Long reportTypeId;

    @ApiModelProperty(example = "Báo cáo sáng") /* Hint for Swagger */
    @Size(max = 100, message = "reportTypeName max length: 100 characters")
    @NotNull(message = "reportTypeName REQUIRED for Create")
    private String reportTypeName;

    @ApiModelProperty(example = "Báo cáo sáng") /* Hint for Swagger */
    @Size(max = 100, message = "reportTypeDesc max length: 100 characters")
    private String reportTypeDesc;
}
