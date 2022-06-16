package com.ntv.ntvcons_backend.dtos.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportUpdateDTO implements Serializable {
    private Long reportId;
    private Long projectId;
    private Long reportTypeId;
    private Long reporterId;
    private Instant reportDate;
    private String reportDesc;
    private final Boolean isDeleted = false;
}
