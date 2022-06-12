package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
        Page<Location> findAllByIsDeletedIsFalse(Pageable pageable);


        /* Id */
        Optional<Location> findByLocationIdAndIsDeletedIsFalse(long locationId);
        List<Location> findAllByLocationIdInAndIsDeletedIsFalse(Collection<Long> locationIdCollection);


        /* Top down */
        List<Location> findAllByCountryAndIsDeletedIsFalse
                (String country);
        List<Location> findAllByCountryAndProvinceAndIsDeletedIsFalse
                (String country, String province);
        List<Location> findAllByCountryAndProvinceAndCityAndIsDeletedIsFalse
                (String country, String province, String city);
        List<Location> findAllByCountryAndProvinceAndCityAndDistrictAndIsDeletedIsFalse
                (String country, String province, String city, String district);
        List<Location> findAllByCountryAndProvinceAndCityAndDistrictAndWardAndIsDeletedIsFalse
                (String country, String province, String city, String district, String ward);
        List<Location> findAllByCountryAndProvinceAndCityAndDistrictAndWardAndStreetAndIsDeletedIsFalse
                (String country, String province, String city, String district, String ward, String Street);
        Optional<Location> findByCountryAndProvinceAndCityAndDistrictAndWardAndStreetAndAddressNumberAndIsDeletedIsFalse
                (String country, String province, String city, String district,
                 String ward, String Street, String addressNumber);
        /* Top down with area */
        List<Location> findAllByCountryAndProvinceAndCityAndDistrictAndWardAndAreaAndIsDeletedIsFalse
                (String country, String province, String city, String district, String ward, String area);
        List<Location> findAllByCountryAndProvinceAndCityAndDistrictAndWardAndAreaAndStreetAndIsDeletedIsFalse
                (String country, String province, String city, String district, String ward, String area, String Street);
        Optional<Location> findByCountryAndProvinceAndCityAndDistrictAndWardAndAreaAndStreetAndAddressNumberAndIsDeletedIsFalse
                (String country, String province, String city, String district,
                 String ward, String area, String Street, String addressNumber);


        /* coordinate */
        Optional<Location> findByCoordinateAndIsDeletedIsFalse(String coordinate);
        List<Location> findAllByCoordinateContainsAndIsDeletedIsFalse(String coordinate);
        List<Location> findAllByCoordinateInAndIsDeletedIsFalse(Collection<String> coordinateCollection);
}
