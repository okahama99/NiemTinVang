package com.ntv.ntvcons_backend.services.location;

import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.locationModels.LocationModel;
import java.util.List;

public interface LocationService {
    /* CREATE */
    Location createLocation(String addressNumber, String street, String ward, String district,
                            String city, String province, String coordinate);

    /* READ */
    List<Location> getAll();

    List<Location> getAllByWardLike(String ward);

    List<Location> getAllByDistrictLike(String district);

    List<Location> getAllByCityLike(String city);

    List<Location> getAllByProvinceLike(String province);

    Location getByCoordinate(String coordinate);

    Location getById(int locationId);

    /* UPDATE */
    boolean updateLocation(LocationModel locationModel);


    /* DELETE */
    boolean deleteLocation(int locationId);
}