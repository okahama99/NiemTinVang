package com.ntv.ntvcons_backend.entities;

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
@Table(name = "task_report")
public class TaskReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskReportId", nullable = false)
    private Long taskReportId;

    @Column(name = "taskId", nullable = false)
    private Long taskId;

    @Column(name = "reportId", nullable = false)
    private Long reportId;

    @Column(name = "taskProgress", nullable = false, length = 100)
    private String taskProgress;

    @Column(name = "taskNote", length = 100)
    private String taskNote;


}