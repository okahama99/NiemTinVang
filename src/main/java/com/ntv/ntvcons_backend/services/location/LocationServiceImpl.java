package com.ntv.ntvcons_backend.services.location;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

    /* CREATE */
    @Override
    public Location createLocation(String addressNumber, String street, String ward, String district, String city, String province, String coordinate) {
        return null;
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
    public boolean updateLocation(ShowLocationModel showLocationModel) {
        return true;
    }

    /* DELETE */
    @Override
    public boolean deleteLocation(int locationId) {
        return false;
    }
}
