package com.ntv.ntvcons_backend.services.location;

import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;

import java.util.List;

public interface LocationService {
    /* CREATE */
    void createLocation(CreateLocationModel createLocationModel);

    /* READ */
    List<ShowLocationModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Location> getAllByWardLike(String ward);

    List<Location> getAllByDistrictLike(String district);

    List<Location> getAllByCityLike(String city);

    List<Location> getAllByProvinceLike(String province);

    Location getByCoordinate(String coordinate);

    Location getById(int locationId);

    /* UPDATE */
    void updateLocation(UpdateLocationModel updateLocationModel);

    /* DELETE */
    boolean deleteLocation(long locationId);
}