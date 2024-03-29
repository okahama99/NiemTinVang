package com.ntv.ntvcons_backend.dtos.report;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportReadDTO extends BaseReadDTO {
    private Long reportId;
    private Long projectId;
    private ReportTypeReadDTO reportType;
    private UserReadDTO reporter;
    private String reportName;
    private String reportDesc;
    private String reportDate;

    private List<ReportDetailReadDTO> reportDetailList;
    private List<TaskReportReadDTO> taskReportList;
}
