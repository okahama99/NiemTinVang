package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;
import com.ntv.ntvcons_backend.services.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private LocationService locationService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyRole('Admin','Staff')")
    @PostMapping(value = "/v1/createLocation", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createLocation(@RequestBody CreateLocationModel location) {
        /* TODO: create location */
        return null;
    }

    /* READ */
    @PreAuthorize("hasAnyRole('Admin','Staff','Customer')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<ShowLocationModel> locations = locationService.getAll(pageNo, pageSize, sortBy, sortType);

            if (locations == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Location found");
            }

            return ResponseEntity.ok().body(locations);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Location", e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/checkDuplicate", produces = "application/json;charset=UTF-8")
    public @ResponseBody String checkDuplicate(@RequestParam String addressNumber) {
        String result = locationService.checkDuplicate(addressNumber);
        return result;
    }

    /* UPDATE */
    @PreAuthorize("hasAnyRole('Admin','Staff')")
    @PutMapping(value = "/v1/updateLocation", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateLocation(@RequestBody UpdateLocationModel location) {
        /* TODO: update Location */
        return null;
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @DeleteMapping(value = "/v1/deleteLocation/{locationId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteLocation(@PathVariable(name = "locationId") long locationId) {
        try {
            if (!locationService.deleteLocation(locationId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Location found with Id: '" + locationId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Location with Id: '" + locationId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Location with Id: '" + locationId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
