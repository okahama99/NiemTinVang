package com.example.ntv.Database.Entities;

import com.example.ntv.Database.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "entitywrapper")
public class EntityWrapper extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EntityWrapperId", nullable = false)
    private int entityWrapperId;
    @Basic
    @Column(name = "ProjectId", unique = true)
    private Integer projectId;
    @Basic
    @Column(name = "ReportId", unique = true)
    private Integer reportId;
    @Basic
    @Column(name = "RequestId", unique = true)
    private Integer requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityWrapper that = (EntityWrapper) o;
        return entityWrapperId == that.entityWrapperId && Objects.equals(projectId, that.projectId) && Objects.equals(reportId, that.reportId) && Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityWrapperId, projectId, reportId, requestId);
    }
}
