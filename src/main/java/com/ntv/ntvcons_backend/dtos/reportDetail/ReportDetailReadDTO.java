package com.ntv.ntvcons_backend.dtos.reportDetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailReadDTO extends BaseReadDTO {
    private Long reportDetailId;
    private Long reportId;
    private Double itemAmount;
    private String itemUnit;
    private String itemDesc;
    private Double itemPrice;
}
