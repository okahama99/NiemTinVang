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
public class RequestDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestDetailId", nullable = false)
    private int requestDetailId;
    @Basic
    @Column(name = "RequestId", nullable = false)
    private int requestId;
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
        RequestDetail that = (RequestDetail) o;
        return requestDetailId == that.requestDetailId && requestId == that.requestId && Double.compare(that.itemAmount, itemAmount) == 0 && Double.compare(that.itemPrice, itemPrice) == 0 && Objects.equals(itemDesc, that.itemDesc) && Objects.equals(itemUnit, that.itemUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestDetailId, requestId, itemDesc, itemAmount, itemUnit, itemPrice);
    }
}
