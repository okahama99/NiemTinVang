package com.ntv.ntvcons_backend.dtos.reportDetail;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
