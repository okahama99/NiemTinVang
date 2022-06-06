package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_worker")
public class ProjectWorker extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectWorkerId", nullable = false)
    private Long projectWorkerId;

    @Column(name = "projectId", nullable = false)
    private Long projectId;

    @Column(name = "workerId", nullable = false)
    private Long workerId;

    @Column(name = "assignDate", nullable = false)
    private Instant assignDate;

    @Column(name = "removeDate")
    private Instant removeDate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}