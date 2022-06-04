package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
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
    private Integer requestDetailId;

    @Column(name = "requestId", nullable = false)
    private Integer requestId;

    @Column(name = "itemDesc", nullable = false, length = 500)
    private String itemDesc;

    @Column(name = "itemAmount", nullable = false)
    private Double itemAmount;

    @Column(name = "itemUnit", nullable = false, length = 50)
    private String itemUnit;

    @Column(name = "itemPrice", nullable = false)
    private Double itemPrice;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}