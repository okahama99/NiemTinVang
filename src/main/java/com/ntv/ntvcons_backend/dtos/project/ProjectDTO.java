package com.ntv.ntvcons_backend.dtos.project;

import com.ntv.ntvcons_backend.dtos.location.LocationDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO implements Serializable {
    private Integer projectId;
    private String projectName;
    private LocationDTO location;
    private BlueprintDTO blueprint;
    private LocalDateTime planStartDate;
    private LocalDateTime planEndDate;
    private Double estimatedCost;
    private LocalDateTime actualStartDate;
    private LocalDateTime actualEndDate;
    private Double actualCost;
    private Boolean isDeleted = false;
}
