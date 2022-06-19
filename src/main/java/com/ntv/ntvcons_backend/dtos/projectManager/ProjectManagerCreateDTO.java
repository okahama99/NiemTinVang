package com.ntv.ntvcons_backend.dtos.projectManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectManagerCreateDTO implements Serializable {
    private Long projectId;
    private Long managerId;
    private LocalDateTime assignDate;
    private LocalDateTime removeDate;

    @JsonIgnore
    private final Boolean isDeleted = false;
}
