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
@Table(name = "report_type")
public class ReportType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportTypeId", nullable = false)
    private Long reportTypeId;

    @Column(name = "reportTypeName", nullable = false, length = 50)
    private String reportTypeName;

    @Column(name = "reportTypeDesc", length = 100)
    private String reportTypeDesc;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}