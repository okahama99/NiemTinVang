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
@Table(name = "worker")
public class Worker extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workerId", nullable = false)
    private Long workerId;

    @Column(name = "fullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "citizenId", nullable = false, length = 20)
    private String citizenId;

    @Column(name = "socialSecurityCode", nullable = false, length = 100)
    private String socialSecurityCode;

    @Column(name = "addressId", nullable = false)
    private Long addressId;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}