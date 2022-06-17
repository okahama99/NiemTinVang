package com.ntv.ntvcons_backend.dtos.reportDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailUpdateDTO implements Serializable {
    private Long reportDetailId;
    private Long reportId;
    private String itemDesc;
    private Double itemAmount;
    private String itemUnit;
    private Double itemPrice;
    /* TODO: to be replace with status */
    private final Boolean isDeleted = false;
}