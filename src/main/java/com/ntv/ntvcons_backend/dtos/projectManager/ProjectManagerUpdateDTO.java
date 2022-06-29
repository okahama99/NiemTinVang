package com.ntv.ntvcons_backend.dtos.projectManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectManagerUpdateDTO implements Serializable {
    private Long projectManagerId;
    private Long projectId;
    private Long managerId;
    private String assignDate;
    private String removeDate;

    @JsonIgnore
    private final Boolean isDeleted = false;
}
