package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "task")
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId", nullable = false)
    private Long taskId;

    @Column(name = "projectId", nullable = false)
    private Long projectId;

    @Column(name = "taskName", nullable = false)
    private String taskName;

    @Column(name = "taskDesc", length = 100)
    private String taskDesc;

    @Column(name = "planStartDate", nullable = false)
    private LocalDateTime planStartDate;

    @Column(name = "planEndDate")
    private LocalDateTime planEndDate;

    @Column(name = "actualStartDate")
    private LocalDateTime actualStartDate;

    @Column(name = "actualEndDate")
    private LocalDateTime actualEndDate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}