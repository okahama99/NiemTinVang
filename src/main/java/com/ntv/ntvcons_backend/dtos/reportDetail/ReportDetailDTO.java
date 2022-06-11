package com.ntv.ntvcons_backend.dtos.reportDetail;

import com.ntv.ntvcons_backend.dtos.report.ReportDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailDTO implements Serializable {
    private Integer reportDetailId;
    private ReportDTO report;
    private String itemDesc;
    private Double itemAmount;
    private String itemUnit;
    private Double itemPrice;
    private Boolean isDeleted = false;
}
