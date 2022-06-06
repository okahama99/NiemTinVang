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
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportId", nullable = false)
    private Long reportId;

    @Column(name = "projectId", nullable = false)
    private Long projectId;

    @Column(name = "reportTypeId", nullable = false)
    private Long reportTypeId;

    @Column(name = "reporterId", nullable = false)
    private Long reporterId;

    @Column(name = "reportDate", nullable = false)
    private Instant reportDate;

    @Column(name = "reportDesc", nullable = false, length = 500)
    private String reportDesc;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}