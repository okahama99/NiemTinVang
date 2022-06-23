package com.ntv.ntvcons_backend.services.location;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationUpdateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;
import com.ntv.ntvcons_backend.entities.Report;
import com.ntv.ntvcons_backend.repositories.LocationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ModelMapper modelMapper;

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
    public Location createLocation(Location newLocation) {
        String errorMsg = "";

        /* Check duplicate */
        if (locationRepository
                .existsByCoordinateAndIsDeletedIsFalse(newLocation.getCoordinate())) {
            errorMsg += "Already exists another Location with coordinate: " + newLocation.getCoordinate() + ". ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return locationRepository.saveAndFlush(newLocation);
    }
    @Override
    public LocationReadDTO createLocationByDTO(LocationCreateDTO newLocationDTO) {
        Location newLocation = modelMapper.map(newLocationDTO, Location.class);

        newLocation = createLocation(newLocation);

        return modelMapper.map(newLocation, LocationReadDTO.class);
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

        Page<Location> pagingResult = locationRepository.findAllByIsDeletedIsFalse(paging);

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
    public boolean existsById(long locationId) {
        return locationRepository
                .existsByLocationIdAndIsDeletedIsFalse(locationId);
    }
    @Override
    public Location getById(long locationId) {
        return locationRepository
                .findByLocationIdAndIsDeletedIsFalse(locationId)
                .orElse(null);
    }
    @Override
    public LocationReadDTO getDTOById(long locationId) {
        Location location = getById(locationId);

        if (location == null) {
            return null;
        }

        return modelMapper.map(location, LocationReadDTO.class);
    }

    @Override
    public List<Location> getAllByWardContains(String ward) {
        return null;
    }

    @Override
    public List<Location> getAllByDistrictContains(String district) {
        return null;
    }

    @Override
    public List<Location> getAllByCityContains(String city) {
        return null;
    }

    @Override
    public List<Location> getAllByProvinceContains(String province) {
        return null;
    }

    @Override
    public Location getByCoordinate(String coordinate) {
        return null;
    }
    @Override
    public boolean existsByCoordinate(String coordinate) {
        return locationRepository.existsByCoordinateAndIsDeletedIsFalse(coordinate);
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
          Date date = new Date();
          location.setUpdatedAt(date);
          location.setUpdatedBy(updateLocationModel.getUserId());
          locationRepository.saveAndFlush(location);

    }

    @Override
    public Location updateLocation(Location updatedLocation) {
        Location oldLocation = getById(updatedLocation.getLocationId());

        if (oldLocation == null) {
            return null;
        }

        String errorMsg = "";

        /* Check duplicate */
        if (!oldLocation.getCoordinate().equals(updatedLocation.getCoordinate())) {
            if (locationRepository
                    .existsByCoordinateAndLocationIdIsNotAndIsDeletedIsFalse(
                            updatedLocation.getCoordinate(),
                            updatedLocation.getLocationId())) {
                errorMsg += "Already exists another Location with coordinate: " + updatedLocation.getCoordinate() + ". ";
            }
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return locationRepository.saveAndFlush(updatedLocation);
    }
    @Override
    public LocationReadDTO updateLocationByDTO(LocationUpdateDTO updatedLocationDTO) {
        Location updatedLocation = modelMapper.map(updatedLocationDTO, Location.class);

        updatedLocation = updateLocation(updatedLocation);

        if (updatedLocation == null) {
            return null;
        }

        return modelMapper.map(updatedLocation, LocationReadDTO.class);
    }

    /* DELETE */
    @Override
    public boolean deleteLocation(long locationId) {
        Location location = getById(locationId);

        if (location == null) {
            return false;
            /* Not found with Id */
        }

        location.setIsDeleted(true);
        locationRepository.saveAndFlush(location);

        return true;
    }

    @Override
    public String checkDuplicate(String addressNumber)
    {
        String result = "No duplicate";
        Location checkDuplicateLocation = locationRepository.getByAddressNumberAndIsDeletedIsFalse(addressNumber);
        if(checkDuplicateLocation != null)
        {
            result = "Existed address number";
            return result;
        }
        return result;
    }
}
