package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locationId", nullable = false)
    private Integer locationId;

    @Column(name = "addressNumber", length = 100)
    private String addressNumber;

    @Column(name = "street", length = 100)
    private String street;

    /** area = Khu phố, khu dân cư,... (optional) */
    @Column(name = "area", length = 100)
    private String area;

    @Column(name = "ward", length = 100)
    private String ward;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "coordinate", nullable = false, length = 500, unique = true)
    private String coordinate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}