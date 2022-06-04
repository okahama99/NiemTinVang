package com.ntv.ntvcons_backend.entities;

import com.ntv.ntvcons_backend.constants.EntityType;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "entity_wrapper")
public class EntityWrapper extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entityWrapperId", nullable = false)
    private Integer entityWrapperId;

    @Column(name = "projectId", unique = true)
    private Integer projectId;

    @Column(name = "reportId", unique = true)
    private Integer reportId;

    @Column(name = "requestId", unique = true)
    private Integer requestId;

    @Column(name = "postId", unique = true)
    private Integer postId;

    @Column(name = "userId", unique = true)
    private Integer userId;

    @Column(name = "workerId", unique = true)
    private Integer workerId;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

    /** For CREATE only */
    public EntityWrapper(int entityId, EntityType type) {
        this.entityWrapperId = null;
        this.projectId = null;
        this.reportId = null;
        this.requestId = null;
        this.postId = null;
        this.userId = null;
        this.workerId = null;
        this.isDeleted = false;

        switch (type) {
            case PROJECT_ENTITY:
                this.projectId = entityId;
                break;
            case REPORT_ENTITY:
                this.reportId = entityId;
                break;
            case REQUEST_ENTITY:
                this.requestId = entityId;
                break;
            case POST_ENTITY:
                this.postId = entityId;
                break;
            case USER_ENTITY:
                this.userId = entityId;
                break;
            case WORKER_ENTITY:
                this.workerId = entityId;
                break;
        }
    }
}