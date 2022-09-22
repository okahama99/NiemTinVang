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
@Table(name = "project_manager")
public class ProjectManager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectManagerId", nullable = false)
    private Long projectManagerId;

    @Column(name = "projectId", nullable = false)
    private Long projectId;

    @Column(name = "managerId", nullable = false)
    private Long managerId;

    @Column(name = "assignDate", nullable = false)
    private LocalDateTime assignDate;

    @Column(name = "removeDate")
    private LocalDateTime removeDate;

}