package com.ntv.ntvcons_backend.dtos.reportDetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailReadDTO implements Serializable {
    private Long reportDetailId;
    private Long reportId;
    private String itemDesc;
    private Double itemAmount;
    private String itemUnit;
    private Double itemPrice;

    /* TODO: to be replace with status */
//    private Boolean isDeleted = false;

    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;
}
