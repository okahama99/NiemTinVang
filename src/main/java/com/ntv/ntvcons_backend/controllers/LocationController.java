package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationUpdateDTO;
import com.ntv.ntvcons_backend.entities.LocationModels.ShowLocationModel;
import com.ntv.ntvcons_backend.services.location.LocationService;
import com.ntv.ntvcons_backend.utils.MiscUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private LocationService locationService;
    @Autowired
    private MiscUtil miscUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/createLocation", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createLocation(@RequestBody @Valid LocationCreateDTO locationDTO) {
        try {
            LocationReadDTO newLocationDTO = locationService.createLocationByDTO(locationDTO);

            return ResponseEntity.ok().body(newLocationDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (createdBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Location", e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('54','24','14')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowLocationModel> locations = locationService.getAll(pageNo, pageSize, sortBy, sortTypeAsc);

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

    @PreAuthorize("hasAnyAuthority('54','24','14')")
    @GetMapping(value = "/v1.1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllAlt1(@RequestParam int pageNo,
                                             @RequestParam int pageSize,
                                             @RequestParam String sortBy,
                                             @RequestParam boolean sortTypeAsc) {
        try {
            List<LocationReadDTO> locationList =
                    locationService.getAllInPaging(
                            miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (locationList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Location found");
            }

            return ResponseEntity.ok().body(locationList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Location", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24','14')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.LOCATION searchType) {
        try {
            LocationReadDTO locationDTO;

            switch (searchType) {
                case BY_ID:
                    locationDTO = locationService.getDTOById(Long.parseLong(searchParam));

                    if (locationDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Location found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_COORDINATE:
                    locationDTO = locationService.getDTOByCoordinate(searchParam);

                    if (locationDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Location found with coordinate: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Location");
            }

            return ResponseEntity.ok().body(locationDTO);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for Location with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;

                case BY_COORDINATE:
                    errorMsg += "coordinate: '" + searchParam + "'. ";
                    break;

            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

//   TODO: @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
//                                                @RequestParam SearchType.ALL_LOCATION searchType,
//                                                @RequestParam int pageNo,
//                                                @RequestParam int pageSize,
//                                                @RequestParam String sortBy,
//                                                @RequestParam boolean sortTypeAsc) {
//        try {
//            Pageable paging = thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);
//
//            List<LocationReadDTO> locationDTOList;
//
//            switch (searchType) {
//                case BY_COUNTRY:
//                    locationDTOList =
//                            locationService.getAllDTOInPagingByCountry(paging, searchParam);
//
//                    if (locationDTOList == null) {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                                .body("No Location found with name contains: '" + searchParam + "'. ");
//                    }
//                    break;
//
//                case BY_PROVINCE:
//                    locationDTOList =
//                            locationService.getAllDTOInPagingByProvince(paging, searchParam);
//
//                    if (locationDTOList == null) {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                                .body("No Location found with name contains: '" + searchParam + "'. ");
//                    }
//                    break;
//
//                default:
//                    throw new IllegalArgumentException("Invalid SearchType used for entity Location");
//            }
//
//            return ResponseEntity.ok().body(locationDTOList);
//        } catch (NumberFormatException nFE) {
//            return ResponseEntity.badRequest().body(
//                    new ErrorResponse(
//                            "Invalid parameter type for searchType: '" + searchType
//                                    + "'. Expecting parameter of type: Long",
//                            nFE.getMessage()));
//        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
//            /* Catch invalid sortBy || searchType */
//            return ResponseEntity.badRequest().body(
//                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
//        } catch (Exception e) {
//            String errorMsg = "Error searching for Location with ";
//
//            switch (searchType) {
//                case BY_COUNTRY:
//                    errorMsg += "country: '" + searchParam + "'. ";
//                    break;
//
//                case BY_PROVINCE:
//                    errorMsg += "province: '" + searchParam + "'. ";
//                    break;
//            }
//
//            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
//        }
//    }

    @PreAuthorize("hasAnyAuthority('54','24','14')")
    @GetMapping(value = "/v1/checkDuplicate", produces = "application/json;charset=UTF-8")
    public @ResponseBody String checkDuplicate(@RequestParam String addressNumber) {
        String result = locationService.checkDuplicate(addressNumber);
        return result;
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updateLocation", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateLocation(@RequestBody @Valid LocationUpdateDTO locationDTO) {
        try {
            LocationReadDTO updatedLocationDTO = locationService.updateLocationByDTO(locationDTO);

            if (updatedLocationDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Location found with Id: '" + locationDTO.getLocationId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedLocationDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found User by Id (createdBy), which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Location", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deleteLocation/{locationId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteLocation(@PathVariable(name = "locationId") long locationId) {
        try {
            if (!locationService.deleteLocation(locationId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Location found with Id: '" + locationId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Location with Id: '" + locationId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Location with Id: '" + locationId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}
