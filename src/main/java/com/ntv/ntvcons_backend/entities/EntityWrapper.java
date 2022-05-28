package com.ntv.ntvcons_backend.entities;

import com.ntv.ntvcons_backend.constants.EntityWrapperType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class EntityWrapper extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EntityWrapperId", nullable = false)
    private int entityWrapperId;
    @Basic
    @Column(name = "ProjectId", nullable = true, unique = true)
    private Integer projectId;
    @Basic
    @Column(name = "ReportId", nullable = true, unique = true)
    private Integer reportId;
    @Basic
    @Column(name = "RequestId", nullable = true, unique = true)
    private Integer requestId;

    public EntityWrapper(Integer entityId, EntityWrapperType type) {
        switch (type) {
            case PROJECT_ENTITY:
                this.projectId = entityId;
                this.reportId = null;
                this.requestId = null;
                break;
            case REPORT_ENTITY:
                this.projectId = null;
                this.reportId = entityId;
                this.requestId = null;
                break;
            case REQUEST_ENTITY:
                this.projectId = null;
                this.reportId = null;
                this.requestId = entityId;
                break;
        }
    }

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
