package com.ntv.ntvcons_backend.services.location;

import com.ntv.ntvcons_backend.dtos.location.LocationCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationUpdateDTO;
import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface LocationService { /* TODO: throws Exception for controller to handle */
    /* CREATE */
    void createLocation(CreateLocationModel createLocationModel);

    Location createLocation(Location newLocation)  throws Exception;
    LocationReadDTO createLocationByDTO(LocationCreateDTO newLocationDTO) throws Exception;

    /* READ */
    List<ShowLocationModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    Page<Location> getPageAll(Pageable paging);
    List<LocationReadDTO> getAllInPaging(Pageable paging);

    boolean existsById(long locationId) throws Exception;
    Location getById(long locationId) throws Exception;
    LocationReadDTO getDTOById(long locationId) throws Exception;

    List<Location> getAllByIdIn(Collection<Long> locationIdCollection) throws Exception;
    List<LocationReadDTO> getAllDTOByIdIn(Collection<Long> locationIdCollection) throws Exception;
    Map<Long, LocationReadDTO> mapLocationIdLocationDTOByIdIn(Collection<Long> locationIdCollection) throws Exception;

    boolean existsByCoordinate(String coordinate) throws Exception;
    Location getByCoordinate(String coordinate) throws Exception;
    LocationReadDTO getDTOByCoordinate(String coordinate) throws Exception;

    String checkDuplicate(String addressNumber);

    boolean checkCoordinate(String coordinate);

    /* UPDATE */
    void updateLocation(UpdateLocationModel updateLocationModel);

    Location updateLocation(Location updatedLocation) throws Exception;
    LocationReadDTO updateLocationByDTO(LocationUpdateDTO updatedLocationDTO) throws Exception;

    /* DELETE */
    boolean deleteLocation(long locationId) throws Exception;
}