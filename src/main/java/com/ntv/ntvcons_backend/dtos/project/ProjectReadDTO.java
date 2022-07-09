package com.ntv.ntvcons_backend.dtos.project;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerCreateDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerUpdateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.request.RequestReadDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectReadDTO extends BaseReadDTO {
    private Long projectId;
    private String projectName;

    private LocationReadDTO location;
    private BlueprintReadDTO blueprint;

    private List<ProjectManagerReadDTO> projectManagerList;
//  TODO:  private List<ProjectWorkerReadDTO> projectWorkerList;

    private LocalDateTime planStartDate;
    private LocalDateTime planEndDate;
    private Double estimatedCost;

    private LocalDateTime actualStartDate;
    private LocalDateTime actualEndDate;
    private Double actualCost;

    private List<TaskReadDTO> taskList;
    private List<ReportReadDTO> reportList;
    private List<RequestReadDTO> requestList;
}
