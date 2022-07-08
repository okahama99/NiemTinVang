package com.ntv.ntvcons_backend.dtos.requestDetail;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetailReadDTO extends BaseReadDTO {
    private Long requestDetailId;
    private Long requestId;
    private Double itemAmount;
    private String itemUnit;
    private String itemDesc;
    private Double itemPrice;
}
