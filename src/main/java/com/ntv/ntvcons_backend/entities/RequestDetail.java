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
@Table(name = "request_detail")
public class RequestDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requestDetailId", nullable = false)
    private Long requestDetailId;

    @Column(name = "requestId", nullable = false)
    private Long requestId;

    @Column(name = "itemAmount", nullable = false)
    private Double itemAmount;

    @Column(name = "itemUnit", nullable = false, length = 50)
    private String itemUnit;

    @Column(name = "itemDesc", nullable = false, length = 100)
    private String itemDesc;

    @Column(name = "itemPrice", nullable = false)
    private Double itemPrice;

}