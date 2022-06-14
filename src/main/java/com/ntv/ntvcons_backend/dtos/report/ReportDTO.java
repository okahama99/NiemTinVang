package com.ntv.ntvcons_backend.dtos.report;

import com.ntv.ntvcons_backend.dtos.project.ProjectDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO implements Serializable {
    private Integer reportId;
    private ProjectDTO project;
    private ReportTypeReadDTO reportType;
    private UserDTO reporter;
    private Instant reportDate;
    private String reportDesc;
    private Boolean isDeleted = false;
}
