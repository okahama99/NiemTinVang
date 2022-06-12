package com.ntv.ntvcons_backend.services.location;

import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;

import java.util.List;

public interface LocationService {
    /* CREATE */
    Location createLocation(String addressNumber, String street, String ward, String district,
                            String city, String province, String coordinate);

    /* READ */
    List<ShowLocationModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Location> getAllByWardContains(String ward);

    List<Location> getAllByDistrictContains(String district);

    List<Location> getAllByCityContains(String city);

    List<Location> getAllByProvinceContains(String province);

    Location getByCoordinate(String coordinate);

    Location getById(int locationId);

    /* UPDATE */
    boolean updateLocation(ShowLocationModel showLocationModel);

    /* DELETE */
    boolean deleteLocation(int locationId);
}