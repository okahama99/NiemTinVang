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
@Table(name = "project")
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectId", nullable = false)
    private Long projectId;

    @Column(name = "projectName", nullable = false, length = 500)
    private String projectName;

    @Column(name = "locationId", nullable = false)
    private Long locationId;

    @Column(name = "blueprintId", nullable = false)
    private Long blueprintId;

    @Column(name = "planStartDate", nullable = false)
    private Instant planStartDate;

    @Column(name = "planEndDate")
    private Instant planEndDate;

    @Column(name = "estimatedCost")
    private Double estimatedCost;

    @Column(name = "actualStartDate")
    private Instant actualStartDate;

    @Column(name = "actualEndDate")
    private Instant actualEndDate;

    @Column(name = "actualCost")
    private Double actualCost;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}