package com.ntv.ntvcons_backend.dtos.reportType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTypeDTO implements Serializable {
    private Integer reportTypeId;
    private String reportTypeName;
    private String reportTypeDesc;
    private Boolean isDeleted;
}
