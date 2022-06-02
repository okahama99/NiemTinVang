package com.ntv.ntvcons_backend.services.location;

import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.locationModels.LocationModel;
import com.ntv.ntvcons_backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Location createLocation(String addressNumber, String street, String ward, String district, String city, String province, String coordinate) {
        return null;
    }

    @Override
    public List<Location> getAll() {
        return null;
    }

    @Override
    public List<Location> getAllByWardLike(String ward) {
        return null;
    }

    @Override
    public List<Location> getAllByDistrictLike(String district) {
        return null;
    }

    @Override
    public List<Location> getAllByCityLike(String city) {
        return null;
    }

    @Override
    public List<Location> getAllByProvinceLike(String province) {
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

    @Override
    public boolean updateLocation(LocationModel locationModel) {
        return true;
    }

    @Override
    public boolean deleteLocation(int locationId) {
        return false;
    }
}
