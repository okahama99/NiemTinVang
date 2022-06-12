package com.ntv.ntvcons_backend.dtos.reportType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTypeUpdateDTO implements Serializable {
    private Long reportTypeId;
    private String reportTypeName;
    private String reportTypeDesc;
    /* TODO: to be replace with status */
    private final Boolean isDeleted = false;
}
