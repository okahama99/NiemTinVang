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
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportId", nullable = false)
    private Integer reportId;

    @Column(name = "projectId", nullable = false)
    private Integer projectId;

    @Column(name = "reportTypeId", nullable = false)
    private Integer reportTypeId;

    @Column(name = "reporterId", nullable = false)
    private Integer reporterId;

    @Column(name = "reportDate", nullable = false)
    private Instant reportDate;

    @Column(name = "reportDesc", nullable = false, length = 500)
    private String reportDesc;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}