package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private LocalDateTime assignDate;

    @Column(name = "removeDate")
    private LocalDateTime removeDate;

}