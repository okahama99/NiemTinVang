package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository  extends JpaRepository<Location, Integer> {
        Location findByStreetAndAddressNumberAndWardAndCityAndCoordinateAndProvinceAndDistrict(String street,String address, String ward, String city, String coordinate, String province, String district);
}
