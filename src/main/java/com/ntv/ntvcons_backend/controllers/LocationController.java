package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.services.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Location")
public class LocationController {
    @Autowired
    LocationService locationService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowLocationModel> getAll(@RequestBody int pageNo,
                          @RequestBody int pageSize,
                          @RequestBody String sortBy,
                          @RequestBody boolean sortType) {
        List<ShowLocationModel> locations = locationService.getAll(pageNo, pageSize, sortBy, sortType);
        return locations;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteLocation/{locationId}", produces = "application/json;charset=UTF-8")
    public HttpStatus deleteLocation(@PathVariable(name = "locationId") int locationId){
        if(locationService.deleteLocation(locationId))
        {
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }
}
