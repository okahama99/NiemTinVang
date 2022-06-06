package com.ntv.ntvcons_backend.dtos.project;

import com.ntv.ntvcons_backend.dtos.location.LocationDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO implements Serializable {
    private Integer projectId;
    private String projectName;
    private LocationDTO location;
    private BlueprintDTO blueprint;
    private Instant planStartDate;
    private Instant planEndDate;
    private Double estimatedCost;
    private Instant actualStartDate;
    private Instant actualEndDate;
    private Double actualCost;
    private Boolean isDeleted;
}
