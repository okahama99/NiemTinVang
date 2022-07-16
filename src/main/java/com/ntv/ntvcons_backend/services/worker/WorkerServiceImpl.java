package com.ntv.ntvcons_backend.services.worker;

import com.ntv.ntvcons_backend.dtos.location.LocationCreateOptionDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerCreateDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerReadDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerUpdateDTO;
import com.ntv.ntvcons_backend.entities.Worker;
import com.ntv.ntvcons_backend.repositories.WorkerRepository;
import com.ntv.ntvcons_backend.services.projectWorker.ProjectWorkerService;
import com.ntv.ntvcons_backend.services.location.LocationService;
import com.ntv.ntvcons_backend.services.taskAssignment.TaskAssignmentService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WorkerServiceImpl implements WorkerService {
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection exception */
    @Autowired
    private UserService userService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ProjectWorkerService projectWorkerService;

    /* CREATE */
    @Override
    public Worker createWorker(Worker newWorker) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!locationService.existsById(newWorker.getAddressId())) {
            errorMsg += "No Location (Address) found with Id: '" + newWorker.getAddressId()
                    + "'. Which violate constraint: FK_Worker_Location. ";
        }
        if (newWorker.getCreatedBy() != null) {
            if (!userService.existsById(newWorker.getCreatedBy())) {
                errorMsg += "No User (CreatedBy) found with Id: '" + newWorker.getCreatedBy()
                        + "'. Which violate constraint: FK_Worker_User_CreatedBy. ";
            }
        }

        /* Check duplicate */
        if (workerRepository
                .existsByCitizenIdAndIsDeletedIsFalse(newWorker.getCitizenId())) {
            errorMsg += "Already exists another Worker with citizenId: '"
                    + newWorker.getCitizenId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return workerRepository.saveAndFlush(newWorker);
    }
    @Override
    public WorkerReadDTO createWorkerByDTO(WorkerCreateDTO newWorkerDTO) throws Exception {
        Worker newWorker = modelMapper.map(newWorkerDTO, Worker.class);


        LocationCreateOptionDTO address = newWorkerDTO.getAddress();

        LocationReadDTO locationDTO;

        switch (newWorkerDTO.getAddress().getCreateOption()) {
            case CREATE_NEW_LOCATION:
                if (address.getNewLocation() == null) {
                    throw new IllegalArgumentException("Missing REQUIRED newLocation");
                }

                /* Create Location first (to get locationId) */
                locationDTO = locationService.createLocationByDTO(address.getNewLocation());
                break;

            case SELECT_EXISTING_LOCATION:
                if (address.getExistingLocationId() == null) {
                    throw new IllegalArgumentException("Missing REQUIRED existingLocationId");
                }

                /* Get associated Location */
                locationDTO = locationService.getDTOById(address.getExistingLocationId());

                if (locationDTO == null) {
                    /* Not found location with Id, NEED TO STOP */
                    throw new IllegalArgumentException("No location found with Id: '"
                            + address.getExistingLocationId() + "'. ");
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid createOption used");
        }

        newWorker.setAddressId(locationDTO.getLocationId());

        newWorker = createWorker(newWorker);

        return fillDTO(newWorker);
    }

    /* READ */
    @Override
    public Page<Worker> getPageAll(Pageable paging) throws Exception {
        Page<Worker> workerPage = workerRepository.findAllByIsDeletedIsFalse(paging);

        if (workerPage.isEmpty())
            return null;

        return workerPage;
    }
    @Override
    public List<WorkerReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<Worker> workerPage = getPageAll(paging);

        if (workerPage == null)
            return null;

        List<Worker> workerList = workerPage.getContent();

        if (workerList.isEmpty())
            return null;

        return fillAllDTO(workerList, workerPage.getTotalPages());
    }

    @Override
    public boolean existsById(long workerId) throws Exception {
        return workerRepository
                .existsByWorkerIdAndIsDeletedIsFalse(workerId);
    }
    @Override
    public Worker getById(long workerId) throws Exception {
        return workerRepository
                .findByWorkerIdAndIsDeletedIsFalse(workerId)
                .orElse(null);
    }
    @Override
    public WorkerReadDTO getDTOById(long workerId) throws Exception {
        Worker worker = getById(workerId);

        if (worker == null)
            return null; /* Not found with Id */

        return fillDTO(worker);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> workerIdCollection) throws Exception {
        return workerRepository.existsAllByWorkerIdInAndIsDeletedIsFalse(workerIdCollection);
    }
    @Override
    public List<Worker> getAllByIdIn(Collection<Long> workerIdCollection) throws Exception {
        List<Worker> workerList = workerRepository.findAllByWorkerIdInAndIsDeletedIsFalse(workerIdCollection);

        if (!workerList.isEmpty()) 
            return null;

        return workerList;
    }
    @Override
    public List<WorkerReadDTO> getAllDTOByIdIn(Collection<Long> workerIdCollection) throws Exception {
        List<Worker> workerList = getAllByIdIn(workerIdCollection);

        if (workerList == null) 
            return null;

        return fillAllDTO(workerList, null);
    }
    @Override
    public Map<Long, WorkerReadDTO> mapWorkerIdWorkerDTOByIdIn(Collection<Long> workerIdCollection) throws Exception {
        List<WorkerReadDTO> workerDTOList = getAllDTOByIdIn(workerIdCollection);

        if (workerDTOList == null) 
            return new HashMap<>();

        return workerDTOList.stream()
                .collect(Collectors.toMap(WorkerReadDTO::getWorkerId, Function.identity()));
    }

    @Override
    public List<Worker> getAllByAddressId(long addressId) throws Exception {
        List<Worker> workerList = workerRepository.findAllByAddressIdAndIsDeletedIsFalse(addressId);

        if (workerList.isEmpty())
            return null;

        return workerList;
    }
    @Override
    public List<WorkerReadDTO> getAllDTOByAddressId(long addressId) throws Exception {
        List<Worker> workerList = getAllByAddressId(addressId);

        if (workerList == null)
            return null;

        return fillAllDTO(workerList, null);
    }
    @Override
    public Page<Worker> getPageAllByAddressId(Pageable paging, long addressId) throws Exception {
        Page<Worker> workerPage =
                workerRepository.findAllByAddressIdAndIsDeletedIsFalse(addressId, paging);

        if (workerPage.isEmpty())
            return null;

        return workerPage;
    }
    @Override
    public List<WorkerReadDTO> getAllDTOInPagingByAddressId(Pageable paging, long addressId) throws Exception {
        Page<Worker> workerPage = getPageAllByAddressId(paging, addressId);

        if (workerPage == null)
            return null;

        List<Worker> workerList = workerPage.getContent();

        if (workerList.isEmpty())
            return null;

        return fillAllDTO(workerList, workerPage.getTotalPages());
    }

    @Override
    public List<Worker> getAllByFullName(String fullName) throws Exception {
        List<Worker> workerList = workerRepository.findAllByFullNameAndIsDeletedIsFalse(fullName);

        if (workerList.isEmpty())
            return null;

        return workerList;
    }
    @Override
    public List<WorkerReadDTO> getAllDTOByFullName(String fullName) throws Exception {
        List<Worker> workerList = getAllByFullName(fullName);

        if (workerList == null)
            return null;

        return fillAllDTO(workerList, null);
    }
    @Override
    public Page<Worker> getPageAllByFullName(Pageable paging, String fullName) throws Exception {
        Page<Worker> workerPage =
                workerRepository.findAllByFullNameAndIsDeletedIsFalse(fullName, paging);

        if (workerPage.isEmpty())
            return null;

        return workerPage;
    }
    @Override
    public List<WorkerReadDTO> getAllDTOInPagingByFullName(Pageable paging, String fullName) throws Exception {
        Page<Worker> workerPage = getPageAllByFullName(paging, fullName);

        if (workerPage == null)
            return null;

        List<Worker> workerList = workerPage.getContent();

        if (workerList.isEmpty())
            return null;

        return fillAllDTO(workerList, workerPage.getTotalPages());
    }

    @Override
    public List<Worker> getAllByFullNameContains(String fullName) throws Exception {
        List<Worker> workerList = workerRepository.findAllByFullNameContainsAndIsDeletedIsFalse(fullName);

        if (workerList.isEmpty())
            return null;

        return workerList;
    }
    @Override
    public List<WorkerReadDTO> getAllDTOByFullNameContains(String fullName) throws Exception {
        List<Worker> workerList = getAllByFullNameContains(fullName);

        if (workerList == null)
            return null;

        return fillAllDTO(workerList, null);
    }
    @Override
    public Page<Worker> getPageAllByFullNameContains(Pageable paging, String fullName) throws Exception {
        Page<Worker> workerPage =
                workerRepository.findAllByFullNameContainsAndIsDeletedIsFalse(fullName, paging);

        if (workerPage.isEmpty())
            return null;

        return workerPage;
    }
    @Override
    public List<WorkerReadDTO> getAllDTOInPagingByFullNameContains(Pageable paging, String fullName) throws Exception {
        Page<Worker> workerPage = getPageAllByFullNameContains(paging, fullName);

        if (workerPage == null)
            return null;

        List<Worker> workerList = workerPage.getContent();

        if (workerList.isEmpty())
            return null;

        return fillAllDTO(workerList, workerPage.getTotalPages());
    }

    @Override
    public Worker getByCitizenId(String citizenId) throws Exception {
        return workerRepository
                .findByCitizenIdAndIsDeletedIsFalse(citizenId)
                .orElse(null);
    }
    @Override
    public WorkerReadDTO getDTOByCitizenId(String citizenId) throws Exception {
        Worker worker = getByCitizenId(citizenId);

        if (worker == null)
            return null;

        return fillDTO(worker);
    }

    @Override
    public List<Worker> getAllByCitizenIdContains(String citizenId) throws Exception {
        List<Worker> workerList = workerRepository.findAllByCitizenIdContainsAndIsDeletedIsFalse(citizenId);

        if (workerList.isEmpty())
            return null;

        return workerList;
    }
    @Override
    public List<WorkerReadDTO> getAllDTOByCitizenIdContains(String citizenId) throws Exception {
        List<Worker> workerList = getAllByCitizenIdContains(citizenId);

        if (workerList == null)
            return null;

        return fillAllDTO(workerList, null);
    }
    @Override
    public Page<Worker> getPageAllByCitizenIdContains(Pageable paging, String citizenId) throws Exception {
        Page<Worker> workerPage =
                workerRepository.findAllByCitizenIdContainsAndIsDeletedIsFalse(citizenId, paging);

        if (workerPage.isEmpty())
            return null;

        return workerPage;
    }
    @Override
    public List<WorkerReadDTO> getAllDTOInPagingByCitizenIdContains(Pageable paging, String citizenId) throws Exception {
        Page<Worker> workerPage = getPageAllByCitizenIdContains(paging, citizenId);

        if (workerPage == null)
            return null;

        List<Worker> workerList = workerPage.getContent();

        if (workerList.isEmpty())
            return null;

        return fillAllDTO(workerList, workerPage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public Worker updateWorker(Worker updatedWorker) throws Exception {
        Worker oldWorker = getById(updatedWorker.getWorkerId());

        if (oldWorker == null) 
            return null;

        String errorMsg = "";

        /* Check FK (if changed) */
        if (!oldWorker.getAddressId().equals(updatedWorker.getAddressId())) {
            if (!locationService.existsById(updatedWorker.getAddressId())) {
                errorMsg += "No Location (Address) found with Id: '" + updatedWorker.getAddressId()
                        + "'. Which violate constraint: FK_Worker_Location. ";
            }
        }
        if (oldWorker.getUpdatedBy() != null) {
            if (!oldWorker.getUpdatedBy().equals(updatedWorker.getUpdatedBy())) {
                if (!userService.existsById(updatedWorker.getUpdatedBy())) {
                    errorMsg += "No Worker (UpdatedBy) found with Id: '" + updatedWorker.getUpdatedBy()
                            + "'. Which violate constraint: FK_Worker_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedWorker.getUpdatedBy())) {
                errorMsg += "No Worker (UpdatedBy) found with Id: '" + updatedWorker.getUpdatedBy()
                        + "'. Which violate constraint: FK_Worker_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (workerRepository
                .existsByCitizenIdAndWorkerIdIsNotAndIsDeletedIsFalse(
                        updatedWorker.getCitizenId(),
                        updatedWorker.getWorkerId())) {
            errorMsg += errorMsg += "Already exists another Worker with citizenId: '"
                    + updatedWorker.getCitizenId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedWorker.setCreatedAt(oldWorker.getCreatedAt());
        updatedWorker.setCreatedBy(oldWorker.getCreatedBy());

        return workerRepository.saveAndFlush(updatedWorker);
    }
    @Override
    public WorkerReadDTO updateWorkerByDTO(WorkerUpdateDTO updatedWorkerDTO) throws Exception {
        Worker updatedWorker = modelMapper.map(updatedWorkerDTO, Worker.class);

        updatedWorker = updateWorker(updatedWorker);

        if (updatedWorker == null)
            return null;

        return fillDTO(updatedWorker);
    }

    /* DELETE */
    @Override
    public boolean deleteWorker(long workerId) throws Exception {
        Worker worker = getById(workerId);

        if (worker == null) {
            return false;
            /* Not found by Id */
        }

        /* Delete all associated projectWorker */
        projectWorkerService.deleteAllByWorkerId(workerId);

        worker.setIsDeleted(true);
        workerRepository.saveAndFlush(worker);

        return true;
    }

    /* Utils */
    private WorkerReadDTO fillDTO(Worker worker) throws Exception {
        WorkerReadDTO workerDTO = modelMapper.map(worker, WorkerReadDTO.class);

        /* Get associated Location */
        workerDTO.setAddress(
                locationService.getDTOById(worker.getAddressId()));

        return workerDTO;
    }

    private List<WorkerReadDTO> fillAllDTO(Collection<Worker> workerCollection, Integer totalPage) throws Exception {
        Set<Long> locationIdSet = new HashSet<>();

        for (Worker worker : workerCollection) {
            locationIdSet.add(worker.getAddressId());
        }

        /* Get associated Location */
        Map<Long, LocationReadDTO> locationIdLocationDTOMap =
                locationService.mapLocationIdLocationDTOByIdIn(locationIdSet);

        return workerCollection.stream()
                .map(worker -> {
                    WorkerReadDTO workerReadDTO =
                            modelMapper.map(worker, WorkerReadDTO.class);

                    workerReadDTO.setAddress(locationIdLocationDTOMap.get(worker.getAddressId()));

                    workerReadDTO.setTotalPage(totalPage);

                    return workerReadDTO;})
                .collect(Collectors.toList());
    }
}
