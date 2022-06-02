package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "task_report")
public class TaskReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskReportId", nullable = false)
    private Integer taskReportId;

    @Column(name = "taskId", nullable = false)
    private Integer taskId;

    @Column(name = "reportId", nullable = false)
    private Integer reportId;

    @Column(name = "taskProgress", nullable = false, length = 500)
    private String taskProgress;

    @Column(name = "taskNote", length = 500)
    private String taskNote;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}