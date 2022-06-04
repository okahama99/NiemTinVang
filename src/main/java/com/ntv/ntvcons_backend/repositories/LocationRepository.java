package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {
        Location findByAddressNumberAndStreetAndWardAndDistrictAndCityAndProvince
                (String address, String street, String ward, String district, String city, String province);

        Location findByCoordinate(String coordinate);
}
