package com.ntv.ntvcons_backend.services.location;

import com.ntv.ntvcons_backend.dtos.location.LocationCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationUpdateDTO;
import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;

import java.util.List;

public interface LocationService { /* TODO: throws Exception for controller to handle */
    /* CREATE */
    void createLocation(CreateLocationModel createLocationModel);

    Location createLocation(Location newLocation);
    LocationReadDTO createLocationByDTO(LocationCreateDTO newLocationDTO);

    /* READ */
    List<ShowLocationModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    boolean existsById(long locationId);
    Location getById(long locationId);
    LocationReadDTO getDTOById(long locationId);

    List<Location> getAllByWardContains(String ward);

    List<Location> getAllByDistrictContains(String district);

    List<Location> getAllByCityContains(String city);

    List<Location> getAllByProvinceContains(String province);

    Location getByCoordinate(String coordinate);
    boolean existsByCoordinate(String coordinate);

    /* UPDATE */
    void updateLocation(UpdateLocationModel updateLocationModel);

    Location updateLocation(Location updatedLocation);
    LocationReadDTO updateLocationByDTO(LocationUpdateDTO updatedLocationDTO);

    /* DELETE */
    boolean deleteLocation(long locationId);

    String checkDuplicate(String addressNumber);

    boolean checkCoordinate(String coordinate);
}