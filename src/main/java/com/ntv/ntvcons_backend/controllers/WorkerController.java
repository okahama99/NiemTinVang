package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.dtos.worker.WorkerCreateDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerReadDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerUpdateDTO;
import com.ntv.ntvcons_backend.services.worker.WorkerService;
import com.ntv.ntvcons_backend.utils.ThanhUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/worker")
public class WorkerController {
    @Autowired
    private WorkerService workerService;
    @Autowired
    private ThanhUtil thanhUtil;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PostMapping(value = "/v1/createWorker", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createWorker(@Valid @RequestBody WorkerCreateDTO workerDTO){
        try {
            WorkerReadDTO newWorkerDTO = workerService.createWorkerByDTO(workerDTO);

            return ResponseEntity.ok().body(newWorkerDTO);
        } catch (IllegalArgumentException iAE) {
            /* Catch not found Role by roleId, which violate FK constraint */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error creating Worker", e.getMessage()));
        }
    }

    /* READ */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<WorkerReadDTO> workerDTOList =
                    workerService.getAllDTOInPaging(
                            thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc));

            if (workerDTOList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Worker found");
            }

            return ResponseEntity.ok().body(workerDTOList);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Worker", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.WORKER searchType) {
        try {
            WorkerReadDTO workerDTO;

            switch (searchType) {
                case BY_ID:
                    workerDTO = workerService.getDTOById(Long.parseLong(searchParam));

                    if (workerDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with Id: '" + searchParam + "'. ");
                    }
                    break;

                case BY_CITIZEN_ID:
                    workerDTO = workerService.getDTOByCitizenId(searchParam);

                    if (workerDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with citizenId: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Worker");
            }

            return ResponseEntity.ok().body(workerDTO);
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
            String errorMsg = "Error searching for Worker with ";

            switch (searchType) {
                case BY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;

                case BY_CITIZEN_ID:
                    errorMsg += "citizenId: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_WORKER searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortTypeAsc) {
        try {
            Pageable paging = thanhUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);

            List<WorkerReadDTO> workerDTOList;

            switch (searchType) {
                case BY_FULL_NAME:
                    workerDTOList =
                            workerService.getAllDTOInPagingByFullName(paging, searchParam);

                    if (workerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with fullName : '" + searchParam + "'. ");
                    }
                    break;

                case BY_FULL_NAME_CONTAINS:
                    workerDTOList =
                            workerService.getAllDTOInPagingByFullNameContains(paging, searchParam);

                    if (workerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with fullName contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_CITIZEN_ID_CONTAINS:
                    workerDTOList =
                            workerService.getAllDTOInPagingByCitizenIdContains(paging, searchParam);

                    if (workerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with citizenId contains: '" + searchParam + "'. ");
                    }
                    break;

                case BY_ADDRESS_ID:
                    workerDTOList =
                            workerService.getAllDTOInPagingByAddressId(paging, Long.parseLong(searchParam));

                    if (workerDTOList == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Worker found with locationId (addressId): '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Worker");
            }

            return ResponseEntity.ok().body(workerDTOList);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                  new ErrorResponse(
                          "Invalid parameter type for searchType: '" + searchType
                              + "'. Expecting parameter of type: Long",
                          nFE.getMessage()));
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for Worker with ";

            switch (searchType) {
                case BY_FULL_NAME:
                    errorMsg += "fullName: '" + searchParam + "'. ";
                    break;

                case BY_FULL_NAME_CONTAINS:
                    errorMsg += "fullName contains: '" + searchParam + "'. ";
                    break;

                case BY_CITIZEN_ID_CONTAINS:
                    errorMsg += "citizenId contains: '" + searchParam + "'. ";
                    break;

                case BY_ADDRESS_ID:
                    errorMsg += "locationId (addressId): '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updateWorker", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateWorker(@Valid @RequestBody WorkerUpdateDTO workerDTO){
        try {
            WorkerReadDTO updatedWorkerDTO = workerService.updateWorkerByDTO(workerDTO);

            if (updatedWorkerDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Worker found with Id: '" + workerDTO.getWorkerId() + "'. ");
            }

            return ResponseEntity.ok().body(updatedWorkerDTO);
        } catch (IllegalArgumentException iAE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error updating Worker with Id: '" + workerDTO.getWorkerId() + "'. ",
                            e.getMessage()));
        }
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @DeleteMapping(value = "/v1/deleteWorker/{workerId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteWorker(@PathVariable(name = "workerId") long workerId){
        try {
            if (!workerService.deleteWorker(workerId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Worker found with Id: '" + workerId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Worker with Id: '" + workerId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Worker with Id: '" + workerId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */
}
