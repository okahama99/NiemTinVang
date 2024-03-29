package com.ntv.ntvcons_backend.dtos.project;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectReadDTO extends BaseReadDTO {
    private Long projectId;
    private String projectName;

    private LocationReadDTO location;
    private BlueprintReadDTO blueprint;

    private List<ProjectManagerReadDTO> ntvManagerList;
    private List<ProjectManagerReadDTO> userManagerList;
    private List<ProjectWorkerReadDTO> projectWorkerList;

    private String planStartDate;
    private String planEndDate;
    private Double estimatedCost;

    private String actualStartDate;
    private String actualEndDate;
    private Double actualCost;
}
