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

    @Column(name = "reportName", nullable = false, length = 100)
    private String reportName;

    @Column(name = "reportDesc", nullable = false, length = 100)
    private String reportDesc;

    @Column(name = "reportDate", nullable = false)
    private LocalDateTime reportDate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}