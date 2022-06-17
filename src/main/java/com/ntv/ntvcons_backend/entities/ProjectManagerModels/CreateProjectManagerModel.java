package com.ntv.ntvcons_backend.entities.ProjectManagerModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateProjectManagerModel {
    private long projectId, managerId;
    private LocalDateTime assignDate;
}
