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
public class ProjectWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectWorkerId", nullable = false)
    private Integer projectWorkerId;

    @Column(name = "workerId", nullable = false)
    private Integer workerId;

    @Column(name = "projectId", nullable = false)
    private Integer projectId;

    @Column(name = "assignDate", nullable = false)
    private Instant assignDate;

    @Column(name = "removeDate")
    private Instant removeDate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}