package com.ntv.ntvcons_backend.entities.RequestDetailModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequestDetailModel {
    private Long requestDetailId;
    private String itemDesc, itemUnit;
    private Double itemAmount, itemPrice;
}
