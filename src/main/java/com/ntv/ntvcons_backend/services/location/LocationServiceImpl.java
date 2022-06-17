package com.ntv.ntvcons_backend.services.location;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;
import com.ntv.ntvcons_backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

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

    /* CREATE */
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
    public Location getById(int locationId) {
        return null;
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

    /* DELETE */
    @Override
    public boolean deleteLocation(long locationId) {
        Location location = locationRepository.findById(locationId).get();
        if(location!=null)
        {
            location.setIsDeleted(true);
            locationRepository.saveAndFlush(location);
        }
        return false;
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
