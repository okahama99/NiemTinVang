package com.ntv.ntvcons_backend.services.location;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationUpdateDTO;
import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;
import com.ntv.ntvcons_backend.repositories.LocationRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public void createLocation(CreateLocationModel createLocationModel) {
       Location location = new Location();
       location.setAddressNumber(createLocationModel.getAddressNumber());
       location.setStreet(createLocationModel.getStreet());
       location.setArea(createLocationModel.getArea());
       location.setWard(createLocationModel.getWard());
       location.setDistrict(createLocationModel.getDistrict());
       location.setCity(createLocationModel.getCity());
       location.setProvince(createLocationModel.getProvince());
       location.setCountry(createLocationModel.getCountry());
       location.setCoordinate(createLocationModel.getCoordinate());
       locationRepository.saveAndFlush(location);
    }

    @Override
    public Location createLocation(Location newLocation) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!userService.existsById(newLocation.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newLocation.getCreatedBy()
                    + "'. Which violate constraint: FK_Location_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (locationRepository.existsByCoordinateAndStatusNotIn(newLocation.getCoordinate(), N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another Location with coordinate: '" + newLocation.getCoordinate() + "'. ";
        }
        if (!(newLocation.getCountry() == null && newLocation.getProvince() == null && newLocation.getCity() == null
                && newLocation.getDistrict() == null && newLocation.getWard() == null && newLocation.getArea() == null
                && newLocation.getStreet() == null && newLocation.getAddressNumber() == null)) {
            if (locationRepository
                    .existsByCountryAndProvinceAndCityAndDistrictAndWardAndAreaAndStreetAndAddressNumberAndStatusNotIn(
                            newLocation.getCountry(), newLocation.getProvince(), newLocation.getCity(),
                            newLocation.getDistrict(), newLocation.getWard(), newLocation.getArea(),
                            newLocation.getStreet(), newLocation.getAddressNumber(),
                            N_D_S_STATUS_LIST)) {
                errorMsg += "Already exists another Location with exact address: '"
                        + newLocation.getAddressNumber() + ", " + newLocation.getStreet() + ", " + newLocation.getArea() + ", "
                        + newLocation.getWard() + ", " + newLocation.getDistrict() + ", " + newLocation.getCity() + ", "
                        + newLocation.getProvince() + ", " + newLocation.getCountry() + "'. ";
            }
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return locationRepository.saveAndFlush(newLocation);
    }
    @Override
    public LocationReadDTO createLocationByDTO(LocationCreateDTO newLocationDTO) throws Exception{
        Location newLocation = modelMapper.map(newLocationDTO, Location.class);

        newLocation = createLocation(newLocation);

        return fillDTO(newLocation);
    }

    /* READ */
    @Override
    public List<ShowLocationModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Location> pagingResult = locationRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / pageSize);

            Page<ShowLocationModel> modelResult =
                    pagingResult.map(new Converter<Location, ShowLocationModel>() {

                @Override
                protected ShowLocationModel doForward(Location location) {
                    ShowLocationModel model = new ShowLocationModel();

                    model.setLocationId(location.getLocationId());
                    model.setAddressNumber(location.getAddressNumber());
                    model.setStreet(location.getStreet());
                    model.setWard(location.getWard());
                    model.setDistrict(location.getDistrict());
                    model.setCity(location.getCity());
                    model.setProvince(location.getProvince());
                    model.setCoordinate(location.getCoordinate());
                    model.setCreatedAt(location.getCreatedAt());
                    model.setCreatedBy(location.getCreatedBy());
                    model.setUpdatedAt(location.getCreatedAt());
                    model.setUpdatedBy(location.getUpdatedBy());
                    model.setTotalPage(totalPage);

                    return model;
                }

                @Override
                protected Location doBackward(ShowLocationModel showLocationModel) {
                    return null;
                }

            });

            return modelResult.getContent();

        } else {
            return new ArrayList<ShowLocationModel>();
        }
    }

    @Override
    public Page<Location> getPageAll(Pageable paging) throws Exception {
        Page<Location> locationPage = locationRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (locationPage.isEmpty()) 
            return null;

        return locationPage;
    }
    @Override
    public List<LocationReadDTO> getAllInPaging(Pageable paging) throws Exception {
        Page<Location> locationPage = getPageAll(paging);

        if (locationPage == null) 
            return null;

        List<Location> locationList = locationPage.getContent();

        if (locationList.isEmpty()) 
            return null;

        return fillAllDTO(locationList, locationPage.getTotalPages());
    }

    @Override
    public boolean existsById(long locationId) throws Exception {
        return locationRepository
                .existsByLocationIdAndStatusNotIn(locationId, N_D_S_STATUS_LIST);
    }
    @Override
    public Location getById(long locationId) throws Exception {
        return locationRepository
                .findByLocationIdAndStatusNotIn(locationId, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public LocationReadDTO getDTOById(long locationId) throws Exception {
        Location location = getById(locationId);

        if (location == null) 
            return null;

        return fillDTO(location);
    }

    @Override
    public List<Location> getAllByIdIn(Collection<Long> locationIdCollection) throws Exception {
        List<Location> locationList =
                locationRepository.findAllByLocationIdInAndStatusNotIn(locationIdCollection, N_D_S_STATUS_LIST);

        if (locationList.isEmpty()) 
            return null;

        return locationList;
    }
    @Override
    public List<LocationReadDTO> getAllDTOByIdIn(Collection<Long> locationIdCollection) throws Exception {
        List<Location> locationList = getAllByIdIn(locationIdCollection);

        if (locationList == null) 
            return null;

        return fillAllDTO(locationList, null);
    }
    @Override
    public Map<Long, LocationReadDTO> mapLocationIdLocationDTOByIdIn(Collection<Long> locationIdCollection) throws Exception {
        List<LocationReadDTO> locationDTOList = getAllDTOByIdIn(locationIdCollection);

        if (locationDTOList == null)
            return new HashMap<>();

        return locationDTOList.stream()
                .collect(Collectors.toMap(LocationReadDTO::getLocationId, Function.identity()));
    }

    @Override
    public boolean existsByCoordinate(String coordinate) throws Exception {
        return locationRepository
                .existsByCoordinateAndStatusNotIn(coordinate, N_D_S_STATUS_LIST);
    }
    @Override
    public Location getByCoordinate(String coordinate) throws Exception {
        return locationRepository
                .findByCoordinateAndStatusNotIn(coordinate, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public LocationReadDTO getDTOByCoordinate(String coordinate) throws Exception {
        Location location = getByCoordinate(coordinate);

        if (location == null) 
            return null;

        return fillDTO(location);
    }

    @Override
    public String checkDuplicate(String addressNumber) {
        String result = "No duplicate";
        Location checkDuplicateLocation = locationRepository.getByAddressNumberAndStatusNotIn(addressNumber, N_D_S_STATUS_LIST);
        if (checkDuplicateLocation != null)
        {
            result = "Existed address number";
            return result;
        }
        return result;
    }

    @Override
    public boolean checkCoordinate(String coordinate) {
        Location checkDuplicateLocation = locationRepository.getByCoordinateAndStatusNotIn(coordinate, N_D_S_STATUS_LIST);
        if (checkDuplicateLocation != null)
        {
            return true;
        }
        return false;
    }

    /* UPDATE */
    @Override
    public void updateLocation(UpdateLocationModel updateLocationModel) {
          Location location = locationRepository.findById(updateLocationModel.getLocationId()).get();
          location.setAddressNumber(updateLocationModel.getAddressNumber());
          location.setStreet(updateLocationModel.getStreet());
          location.setArea(updateLocationModel.getArea());
          location.setWard(updateLocationModel.getWard());
          location.setDistrict(updateLocationModel.getDistrict());
          location.setCity(updateLocationModel.getCity());
          location.setProvince(updateLocationModel.getProvince());
          location.setCountry(updateLocationModel.getCountry());
          location.setCoordinate(updateLocationModel.getCoordinate());
          location.setUpdatedAt(LocalDateTime.now());
          location.setUpdatedBy(updateLocationModel.getUserId());
          locationRepository.saveAndFlush(location);

    }

    @Override
    public Location updateLocation(Location updatedLocation) throws Exception {
        Location oldLocation = getById(updatedLocation.getLocationId());

        if (oldLocation == null) 
            return null;

        String errorMsg = "";

        /* Check FK */
        if (oldLocation.getUpdatedBy() != null) {
            if (!oldLocation.getUpdatedBy().equals(updatedLocation.getUpdatedBy())) {
                if (!userService.existsById(updatedLocation.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedLocation.getUpdatedBy()
                            + "'. Which violate constraint: FK_Location_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedLocation.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedLocation.getUpdatedBy()
                        + "'. Which violate constraint: FK_Location_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (!oldLocation.getCoordinate().equals(updatedLocation.getCoordinate())) {
            if (locationRepository
                    .existsByCoordinateAndLocationIdIsNotAndStatusNotIn(
                            updatedLocation.getCoordinate(),
                            updatedLocation.getLocationId(),
                            N_D_S_STATUS_LIST)) {
                errorMsg += "Already exists another Location with coordinate: '" + updatedLocation.getCoordinate() + "'. ";
            }
        }
        if (locationRepository
                .existsByCountryAndProvinceAndCityAndDistrictAndWardAndAreaAndStreetAndAddressNumberAndLocationIdIsNotAndStatusNotIn(
                        updatedLocation.getCountry(), updatedLocation.getProvince(), updatedLocation.getCity(),
                        updatedLocation.getDistrict(), updatedLocation.getWard(), updatedLocation.getArea(),
                        updatedLocation.getStreet(), updatedLocation.getAddressNumber(), updatedLocation.getLocationId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another Location with exact address: '"
                    + updatedLocation.getAddressNumber() + ", " + updatedLocation.getStreet() + ", " + updatedLocation.getArea()
                    + updatedLocation.getWard() + ", " + updatedLocation.getDistrict() + ", " + updatedLocation.getCity()
                    + updatedLocation.getProvince() + ", " + updatedLocation.getCountry() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedLocation.setCreatedAt(oldLocation.getCreatedAt());
        updatedLocation.setCreatedBy(oldLocation.getCreatedBy());

        return locationRepository.saveAndFlush(updatedLocation);
    }
    @Override
    public LocationReadDTO updateLocationByDTO(LocationUpdateDTO updatedLocationDTO) throws Exception {
        Location updatedLocation = modelMapper.map(updatedLocationDTO, Location.class);

        updatedLocation = updateLocation(updatedLocation);

        if (updatedLocation == null) 
            return null;

        return fillDTO(updatedLocation);
    }

    /* DELETE */
    @Override
    public boolean deleteLocation(long locationId) throws Exception {
        Location location = getById(locationId);

        if (location == null) {
            return false;
            /* Not found with Id */
        }

        location.setStatus(Status.DELETED);
        locationRepository.saveAndFlush(location);

        return true;
    }

    /* Utils */
    private LocationReadDTO fillDTO(Location location) throws Exception {
        return modelMapper.map(location, LocationReadDTO.class);
    }

    private List<LocationReadDTO> fillAllDTO(Collection<Location> locationCollection, Integer totalPage) throws Exception {
        return locationCollection.stream()
                .map(location -> {
                    LocationReadDTO locationDTO =
                            modelMapper.map(location, LocationReadDTO.class);

                    locationDTO.setTotalPage(totalPage);

                    return locationDTO;})
                .collect(Collectors.toList());
    }
}
