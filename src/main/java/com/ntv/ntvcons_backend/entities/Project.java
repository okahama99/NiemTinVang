package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProjectId", nullable = false)
    private int projectId;
    @Basic
    @Column(name = "ProjectName", nullable = false, length = 255)
    private String projectName;
    @Basic
    @Column(name = "LocationId", nullable = false)
    private int locationId;
    @Basic
    @Column(name = "StartDate", nullable = false)
    private Timestamp startDate;
    @Basic
    @Column(name = "EndDate", nullable = true)
    private Timestamp endDate;
    @Basic
    @Column(name = "BlueprintId", nullable = false)
    private int blueprintId;
    @Basic
    @Column(name = "EstimatedCost", nullable = true, precision = 0)
    private Double estimatedCost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return projectId == project.projectId && locationId == project.locationId && blueprintId == project.blueprintId && Objects.equals(projectName, project.projectName) && Objects.equals(startDate, project.startDate) && Objects.equals(endDate, project.endDate) && Objects.equals(estimatedCost, project.estimatedCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, locationId, startDate, endDate, blueprintId, estimatedCost);
    }
}
