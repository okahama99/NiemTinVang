package com.ntv.ntvcons_backend.dtos.requestDetail;

import com.ntv.ntvcons_backend.dtos.request.RequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetailDTO implements Serializable {
    private Integer requestDetailId;
    private RequestDTO request;
    private String itemDesc;
    private Double itemAmount;
    private String itemUnit;
    private Double itemPrice;
    private Boolean isDeleted;
}
