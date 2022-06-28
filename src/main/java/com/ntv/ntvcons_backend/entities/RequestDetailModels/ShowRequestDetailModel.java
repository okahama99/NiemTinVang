package com.ntv.ntvcons_backend.entities.RequestDetailModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ShowRequestDetailModel {
    private Long requestDetailId, requestId;
    private String itemDesc, itemUnit;
    private Double itemAmount, itemPrice;

    private Long createdBy, updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private Double totalPage;
}
