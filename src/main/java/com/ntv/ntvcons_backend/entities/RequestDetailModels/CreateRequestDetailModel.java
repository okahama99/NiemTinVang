package com.ntv.ntvcons_backend.entities.RequestDetailModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRequestDetailModel {
    private Long requestId;
    private String itemDesc, itemUnit;
    private Double itemAmount, itemPrice;
}
