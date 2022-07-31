package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
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
    Page<Location> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);

    Location getByAddressNumberAndStatusNotIn(
            String addressNumber, Collection<Status> statusCollection);

    Location getByCoordinateAndStatusNotIn(
            String addressNumber, Collection<Status> statusCollection);

    /* Id */
    boolean existsByLocationIdAndStatusNotIn(
            long locationId, Collection<Status> statusCollection);
    Optional<Location> findByLocationIdAndStatusNotIn(
            long locationId, Collection<Status> statusCollection);
    List<Location> findAllByLocationIdInAndStatusNotIn(
            Collection<Long> locationIdCollection, Collection<Status> statusCollection);
    /* Id & coordinate */
    /** Check duplicate for Create */
    boolean existsByCoordinateAndLocationIdIsNotAndStatusNotIn(
            String coordinate, long locationId, Collection<Status> statusCollection);
    /* Id & Top down */
    /** Check duplicate for Create  */
    boolean existsByCountryAndProvinceAndCityAndDistrictAndWardAndAreaAndStreetAndAddressNumberAndLocationIdIsNotAndStatusNotIn(
            String country, String province, String city, String district,
            String ward, String area, String Street, String addressNumber,
            long locationId, Collection<Status> statusCollection);


    /* Top down */
    List<Location> findAllByCountryAndStatusNotIn(
            String country, Collection<Status> statusCollection);
    List<Location> findAllByCountryAndProvinceAndStatusNotIn(
            String country, String province, Collection<Status> statusCollection);
    List<Location> findAllByCountryAndProvinceAndCityAndStatusNotIn(
            String country, String province, String city, Collection<Status> statusCollection);
    List<Location> findAllByCountryAndProvinceAndCityAndDistrictAndStatusNotIn(
            String country, String province, String city, String district, Collection<Status> statusCollection);
    List<Location> findAllByCountryAndProvinceAndCityAndDistrictAndWardAndStatusNotIn(
            String country, String province, String city, String district,
            String ward, Collection<Status> statusCollection);
    List<Location> findAllByCountryAndProvinceAndCityAndDistrictAndWardAndStreetAndStatusNotIn(
            String country, String province, String city, String district,
            String ward, String Street, Collection<Status> statusCollection);
    Optional<Location> findByCountryAndProvinceAndCityAndDistrictAndWardAndStreetAndAddressNumberAndStatusNotIn(
            String country, String province, String city, String district,
            String ward, String Street, String addressNumber, Collection<Status> statusCollection);
    /* Top down with area */
    List<Location> findAllByCountryAndProvinceAndCityAndDistrictAndWardAndAreaAndStatusNotIn(
            String country, String province, String city, String district,
            String ward, String area, Collection<Status> statusCollection);
    List<Location> findAllByCountryAndProvinceAndCityAndDistrictAndWardAndAreaAndStreetAndStatusNotIn(
            String country, String province, String city, String district,
            String ward, String area, String Street, Collection<Status> statusCollection);
    Optional<Location> findByCountryAndProvinceAndCityAndDistrictAndWardAndAreaAndStreetAndAddressNumberAndStatusNotIn(
            String country, String province, String city, String district,
            String ward, String area, String Street, String addressNumber, Collection<Status> statusCollection);
    boolean existsByCountryAndProvinceAndCityAndDistrictAndWardAndAreaAndStreetAndAddressNumberAndStatusNotIn(
            String country, String province, String city, String district,
            String ward, String area, String Street, String addressNumber, Collection<Status> statusCollection);


    /* coordinate */
    /** Check duplicate for Create */
    boolean existsByCoordinateAndStatusNotIn(
            String coordinate, Collection<Status> statusCollection);
    Optional<Location> findByCoordinateAndStatusNotIn(
            String coordinate, Collection<Status> statusCollection);
    List<Location> findAllByCoordinateContainsAndStatusNotIn(
            String coordinate, Collection<Status> statusCollection);
    List<Location> findAllByCoordinateInAndStatusNotIn(
            Collection<String> coordinateCollection, Collection<Status> statusCollection);
}
