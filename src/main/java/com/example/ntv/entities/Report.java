package com.example.ntv.entities;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

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
    @Column(name = "ReportId", nullable = false)
    private int reportId;
    @Basic
    @Column(name = "ProjectId", nullable = false)
    private int projectId;
    @Basic
    @Column(name = "ReporterId", nullable = false)
    private int reporterId;
    @Basic
    @Column(name = "ReportDate", nullable = false)
    private Timestamp reportDate;
    @Basic
    @Column(name = "ReportDesc", nullable = false)
    private String reportDesc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return reportId == report.reportId && projectId == report.projectId && reporterId == report.reporterId && Objects.equals(reportDate, report.reportDate) && Objects.equals(reportDesc, report.reportDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId, projectId, reporterId, reportDate, reportDesc);
    }
}