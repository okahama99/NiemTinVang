package com.ntv.ntvcons_backend.entities;

import com.ntv.ntvcons_backend.constants.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;


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

    @Column(name = "gender", length = 20)
    private Gender gender;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "birthPlace", length = 100)
    private String birthPlace;

    @Column(name = "socialSecurityCode", nullable = false, length = 100)
    private String socialSecurityCode;

    @Column(name = "addressId", nullable = false)
    private Long addressId;
}