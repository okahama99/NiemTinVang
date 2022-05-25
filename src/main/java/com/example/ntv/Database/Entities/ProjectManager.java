package com.example.ntv.Database.Entities;
import com.example.ntv.Database.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "projectmanager")
public class ProjectManager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProjectManagerId", nullable = false)
    private int projectManagerId;
    @Basic
    @Column(name = "ManagerId", nullable = false)
    private int managerId;
    @Basic
    @Column(name = "ProjectId", nullable = false)
    private int projectId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectManager that = (ProjectManager) o;
        return projectManagerId == that.projectManagerId && managerId == that.managerId && projectId == that.projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectManagerId, managerId, projectId);
    }
}