package com.example.ntv.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projectblueprint")
class ProjectBlueprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProjectBlueprintId", nullable = false)
    private int projectBlueprintId;
    @Basic
    @Column(name = "ProjectBlueprintName", nullable = false)
    private String projectBlueprintName;
    @Basic
    @Column(name = "DesignerId", nullable = false)
    private int designerId;
    @Basic
    @Column(name = "ProjectBlueprintCost", nullable = false)
    private double projectBlueprintCost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectBlueprint that = (ProjectBlueprint) o;
        return projectBlueprintId == that.projectBlueprintId && designerId == that.designerId && Double.compare(that.projectBlueprintCost, projectBlueprintCost) == 0 && Objects.equals(projectBlueprintName, that.projectBlueprintName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectBlueprintId, projectBlueprintName, designerId, projectBlueprintCost);
    }
}