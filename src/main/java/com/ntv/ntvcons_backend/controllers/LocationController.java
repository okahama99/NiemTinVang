package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.services.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Location")
public class LocationController {
    @Autowired
    LocationService locationService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAllLocation", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowLocationModel> getAllLocation(@RequestParam int pageNo,
                          @RequestParam int pageSize,
                          @RequestParam String sortBy,
                          @RequestParam boolean sortType) {
        List<ShowLocationModel> locations = locationService.getAll(pageNo, pageSize, sortBy, sortType);
        return locations;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteLocation/{locationId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> deleteLocation(@PathVariable(name = "locationId") int locationId){
        if(locationService.deleteLocation(locationId))
        {
            return new ResponseEntity<>("Xóa thành công.",HttpStatus.OK);
        }
        return new ResponseEntity<>("Xóa thất bại.",HttpStatus.BAD_REQUEST);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/checkDuplicate", produces = "application/json;charset=UTF-8")
    public @ResponseBody String checkDuplicate(@RequestParam String addressNumber) {
        String result = locationService.checkDuplicate(addressNumber);
        return result;
    }
}
