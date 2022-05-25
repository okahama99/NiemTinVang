package com.example.ntv.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProjectId", nullable = false)
    private int projectId;
    @Basic
    @Column(name = "ProjectName", nullable = false)
    private String projectName;
    @Basic
    @Column(name = "LocationId", nullable = false)
    private int locationId;
    @Basic
    @Column(name = "StartDate", nullable = false)
    private Timestamp startDate;
    @Basic
    @Column(name = "EndDate")
    private Timestamp endDate;
    @Basic
    @Column(name = "BlueprintId", nullable = false)
    private int blueprintId;
    @Basic
    @Column(name = "EstimatedCost")
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