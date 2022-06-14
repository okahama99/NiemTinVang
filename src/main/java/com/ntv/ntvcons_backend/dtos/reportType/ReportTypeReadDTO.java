package com.ntv.ntvcons_backend.dtos.reportType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTypeReadDTO implements Serializable {
    private Long reportTypeId;
    private String reportTypeName;
    private String reportTypeDesc;

    /* TODO: to be replace with status */
//    private Boolean isDeleted = false;

    /* If null, then no show in json */
    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;

}
