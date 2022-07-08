package com.ntv.ntvcons_backend.dtos.reportType;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTypeReadDTO extends BaseReadDTO {
    private Long reportTypeId;
    private String reportTypeName;
    private String reportTypeDesc;
}
