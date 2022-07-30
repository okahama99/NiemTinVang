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
@Table(name = "project")
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectId", nullable = false)
    private Long projectId;

    @Column(name = "locationId", nullable = false)
    private Long locationId;

    @Column(name = "projectName", nullable = false, length = 100)
    private String projectName;

    @Column(name = "planStartDate", nullable = false)
    private LocalDateTime planStartDate;

    @Column(name = "planEndDate")
    private LocalDateTime planEndDate;

    @Column(name = "estimatedCost")
    private Double estimatedCost;

    @Column(name = "actualStartDate")
    private LocalDateTime actualStartDate;

    @Column(name = "actualEndDate")
    private LocalDateTime actualEndDate;

    @Column(name = "actualCost")
    private Double actualCost;


}