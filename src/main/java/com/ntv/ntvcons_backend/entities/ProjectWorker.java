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
    private LocalDateTime assignDate;

    @Column(name = "removeDate")
    private LocalDateTime removeDate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}