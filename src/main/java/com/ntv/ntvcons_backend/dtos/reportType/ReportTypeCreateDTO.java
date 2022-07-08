package com.ntv.ntvcons_backend.dtos.reportType;

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
public class ReportTypeCreateDTO extends BaseCreateDTO {
    @ApiModelProperty(example = "Báo cáo sáng") /* Hint for Swagger */
    @Size(max = 100, message = "reportTypeName max length: 100 characters")
    @NotNull(message = "reportTypeName REQUIRED for create")
    private String reportTypeName;

    @ApiModelProperty(example = "Báo cáo sáng") /* Hint for Swagger */
    @Size(max = 100, message = "reportTypeDesc max length: 100 characters")
    private String reportTypeDesc;
}
