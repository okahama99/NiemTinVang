package com.ntv.ntvcons_backend.entities.ReportModels;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class ShowReportModel {
    private long reportId, projectId, reporterId;
    private Timestamp reportDate;
    private String reportDesc;
    private Long createdBy, updatedBy;
    private Date createdAt, updatedAt;
    private double totalPage;
}
