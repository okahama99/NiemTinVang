package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ReportDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReportDetailId", nullable = false)
    private int reportDetailId;
    @Basic
    @Column(name = "ReportId", nullable = false)
    private int reportId;
    @Basic
    @Column(name = "ItemDesc", nullable = false, length = 500)
    private String itemDesc;
    @Basic
    @Column(name = "ItemAmount", nullable = false, precision = 0)
    private double itemAmount;
    @Basic
    @Column(name = "ItemUnit", nullable = false, length = 50)
    private String itemUnit;
    @Basic
    @Column(name = "ItemPrice", nullable = false, precision = 0)
    private double itemPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportDetail that = (ReportDetail) o;
        return reportDetailId == that.reportDetailId && reportId == that.reportId && Double.compare(that.itemAmount, itemAmount) == 0 && Double.compare(that.itemPrice, itemPrice) == 0 && Objects.equals(itemDesc, that.itemDesc) && Objects.equals(itemUnit, that.itemUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportDetailId, reportId, itemDesc, itemAmount, itemUnit, itemPrice);
    }
}
