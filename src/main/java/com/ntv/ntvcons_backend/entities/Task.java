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
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId", nullable = false)
    private Integer taskId;

    @Column(name = "projectId", nullable = false)
    private Integer projectId;

    @Column(name = "creatorId", nullable = false)
    private Integer creatorId;

    @Column(name = "taskName", nullable = false)
    private String taskName;

    @Column(name = "taskDesc", length = 500)
    private String taskDesc;

    @Column(name = "planStartDate", nullable = false)
    private Instant planStartDate;

    @Column(name = "planEndDate")
    private Instant planEndDate;

    @Column(name = "actualStartDate")
    private Instant actualStartDate;

    @Column(name = "actualEndDate")
    private Instant actualEndDate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}