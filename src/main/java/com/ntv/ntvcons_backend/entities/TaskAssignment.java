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
@Table(name = "task_assignment")
public class TaskAssignment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignmentId", nullable = false)
    private Long assignmentId;

    @Column(name = "taskId", nullable = false)
    private Long taskId;

    @Column(name = "assignerId", nullable = false)
    private Long assignerId;

    @Column(name = "assigneeId", nullable = false)
    private Long assigneeId;

    @Column(name = "assignDate", nullable = false)
    private Instant assignDate;

    @Column(name = "removeDate")
    private Instant removeDate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}