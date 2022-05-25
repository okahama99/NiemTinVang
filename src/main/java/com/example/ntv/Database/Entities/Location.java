package com.example.ntv.Database.Entities;

import com.example.ntv.Database.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "location")
public class Location extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LocationId", nullable = false)
    private int locationId;
    @Basic
    @Column(name = "AddressNumber")
    private String addressNumber;
    @Basic
    @Column(name = "Street")
    private String street;
    @Basic
    @Column(name = "Ward", nullable = false)
    private String ward;
    @Basic
    @Column(name = "District", nullable = false)
    private String district;
    @Basic
    @Column(name = "City", nullable = false)
    private String city;
    @Basic
    @Column(name = "Province", nullable = false)
    private String province;
    @Basic
    @Column(name = "Coordinate", nullable = false)
    private String coordinate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return locationId == location.locationId && Objects.equals(addressNumber, location.addressNumber) && Objects.equals(street, location.street) && Objects.equals(ward, location.ward) && Objects.equals(district, location.district) && Objects.equals(city, location.city) && Objects.equals(province, location.province) && Objects.equals(coordinate, location.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, addressNumber, street, ward, district, city, province, coordinate);
    }
}