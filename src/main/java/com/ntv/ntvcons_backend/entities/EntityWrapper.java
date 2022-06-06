package com.ntv.ntvcons_backend.entities;

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
    private Long entityWrapperId;

    @Column(name = "projectId", unique = true)
    private Long projectId;

    @Column(name = "reportId", unique = true)
    private Long reportId;

    @Column(name = "requestId", unique = true)
    private Long requestId;

    @Column(name = "postId", unique = true)
    private Long postId;

    @Column(name = "userId", unique = true)
    private Long userId;

    @Column(name = "workerId", unique = true)
    private Long workerId;

    @Column(name = "taskId", unique = true)
    private Long taskId;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}