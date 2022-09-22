package com.ntv.ntvcons_backend.entities;

import com.ntv.ntvcons_backend.constants.EntityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    private Long entityWrapperId;

    @Column(name = "blueprintId")
    private Long blueprintId;

    @Column(name = "postId")
    private Long postId;

    @Column(name = "projectId")
    private Long projectId;

    @Column(name = "reportId")
    private Long reportId;

    @Column(name = "requestId")
    private Long requestId;

    @Column(name = "taskId")
    private Long taskId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "workerId")
    private Long workerId;

    @Column(name = "messageId")
    private Long messageId;

    /** For CREATE only */
    public EntityWrapper(long entityId, EntityType type) {
        this.entityWrapperId = null;
        this.blueprintId = null;
        this.projectId = null;
        this.reportId = null;
        this.requestId = null;
        this.postId = null;
        this.userId = null;
        this.workerId = null;
        this.taskId = null;
        this.messageId = null;

        switch (type) {
            case BLUEPRINT_ENTITY:
                this.blueprintId = entityId;
                break;
            case POST_ENTITY:
                this.postId = entityId;
                break;
            case PROJECT_ENTITY:
                this.projectId = entityId;
                break;
            case REPORT_ENTITY:
                this.reportId = entityId;
                break;
            case REQUEST_ENTITY:
                this.requestId = entityId;
                break;
            case TASK_ENTITY:
                this.taskId = entityId;
                break;
            case USER_ENTITY:
                this.userId = entityId;
                break;
            case WORKER_ENTITY:
                this.workerId = entityId;
                break;
            case MESSAGE_ENTITY:
                this.messageId = entityId;
                break;
        }
    }
}