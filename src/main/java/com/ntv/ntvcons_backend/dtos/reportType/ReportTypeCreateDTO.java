package com.ntv.ntvcons_backend.dtos.reportType;

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
public class ReportTypeCreateDTO extends BaseCreateDTO {
    @Schema(example = "Báo cáo sáng") /* Hint for Swagger */
    @Size(max = 100, message = "reportTypeName max length: 100 characters")
    @NotNull(message = "reportTypeName REQUIRED for Create")
    private String reportTypeName;

    @Schema(example = "Báo cáo sáng") /* Hint for Swagger */
    @Size(max = 100, message = "reportTypeDesc max length: 100 characters")
    private String reportTypeDesc;
}
