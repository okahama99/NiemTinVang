package com.ntv.ntvcons_backend.services.worker;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationCreateDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationUpdateDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerCreateDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerReadDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerUpdateDTO;
import com.ntv.ntvcons_backend.entities.ProjectWorker;
import com.ntv.ntvcons_backend.entities.Worker;
import com.ntv.ntvcons_backend.repositories.WorkerRepository;
import com.ntv.ntvcons_backend.services.entityWrapper.EntityWrapperService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.location.LocationService;
import com.ntv.ntvcons_backend.services.misc.FileCombineService;
import com.ntv.ntvcons_backend.services.projectWorker.ProjectWorkerService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WorkerServiceImpl implements WorkerService {
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Lazy /* To avoid circular injection exception */
    @Autowired
    private UserService userService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ProjectWorkerService projectWorkerService;
    @Autowired
    private EntityWrapperService entityWrapperService;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;
    @Autowired
    private FileCombineService fileCombineService;

    private final EntityType ENTITY_TYPE = EntityType.WORKER_ENTITY;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

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
                .existsByCitizenIdAndStatusNotIn(
                        newWorker.getCitizenId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another Worker with citizenId: '"
                    + newWorker.getCitizenId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return workerRepository.saveAndFlush(newWorker);
    }
    @Override
    public WorkerReadDTO createWorkerByDTO(WorkerCreateDTO newWorkerDTO) throws Exception {
        modelMapper.typeMap(WorkerCreateDTO.class, Worker.class)
            .addMappings(mapper -> {
                mapper.skip(Worker::setBirthday);});

        Worker newWorker = modelMapper.map(newWorkerDTO, Worker.class);

        /* Check input */
        if (newWorkerDTO.getBirthday() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            newWorker.setBirthday(new java.sql.Date(
                    sdf.parse(newWorkerDTO.getBirthday()).getTime()));

            /* Now */
            Calendar calendar = Calendar.getInstance();
            /* Tuổi tối thiểu lao động TODO:(18 hay 16?) */
            /* Perform addition/subtraction (số dương +, số âm -) */
            calendar.add(Calendar.YEAR, -18);

            /* Convert calendar to Date */
            Date minLegalAgeBirthday = calendar.getTime();

            if (newWorker.getBirthday().after(minLegalAgeBirthday)) {
                throw new IllegalArgumentException(
                        "This worker birthday: '" + newWorker.getBirthday()
                                + "' mean they are younger than 18. Which violate labour law. "
                                + " Valid birthday must be before: '" + sdf.format(minLegalAgeBirthday) + "'. ");
            }
        }

        /* Get/Create address (Location) */
        LocationCreateDTO locationCreateDTO = newWorkerDTO.getAddress();
        LocationReadDTO locationDTO = locationService.getDTOByCoordinate(locationCreateDTO.getCoordinate());

        if (locationDTO == null) {
            locationDTO = locationService.createLocationByDTO(locationCreateDTO);
        }

        newWorker.setAddressId(locationDTO.getLocationId());

        newWorker = createWorker(newWorker);

        /* Create associated EntityWrapper */
        entityWrapperService
                .createEntityWrapper(newWorker.getWorkerId(), ENTITY_TYPE, newWorker.getCreatedBy());

        return fillDTO(newWorker);
    }

    /* READ */
    @Override
    public Page<Worker> getPageAll(Pageable paging) throws Exception {
        Page<Worker> workerPage =
                workerRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

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
                .existsByWorkerIdAndStatusNotIn(workerId, N_D_S_STATUS_LIST);
    }
    @Override
    public Worker getById(long workerId) throws Exception {
        return workerRepository
                .findByWorkerIdAndStatusNotIn(workerId, N_D_S_STATUS_LIST)
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
        return workerRepository
                .existsAllByWorkerIdInAndStatusNotIn(workerIdCollection, N_D_S_STATUS_LIST);
    }
    @Override
    public List<Worker> getAllByIdIn(Collection<Long> workerIdCollection) throws Exception {
        List<Worker> workerList =
                workerRepository.findAllByWorkerIdInAndStatusNotIn(workerIdCollection, N_D_S_STATUS_LIST);

        if (workerList.isEmpty())
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
        List<Worker> workerList =
                workerRepository.findAllByAddressIdAndStatusNotIn(addressId, N_D_S_STATUS_LIST);

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
                workerRepository.findAllByAddressIdAndStatusNotIn(addressId, N_D_S_STATUS_LIST, paging);

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
        List<Worker> workerList = workerRepository.findAllByFullNameAndStatusNotIn(fullName, N_D_S_STATUS_LIST);

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
                workerRepository.findAllByFullNameAndStatusNotIn(fullName, N_D_S_STATUS_LIST, paging);

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
        List<Worker> workerList = workerRepository.findAllByFullNameContainsAndStatusNotIn(fullName, N_D_S_STATUS_LIST);

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
                workerRepository.findAllByFullNameContainsAndStatusNotIn(fullName, N_D_S_STATUS_LIST, paging);

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
                .findByCitizenIdAndStatusNotIn(citizenId, N_D_S_STATUS_LIST)
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
        List<Worker> workerList = workerRepository.findAllByCitizenIdContainsAndStatusNotIn(citizenId, N_D_S_STATUS_LIST);

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
                workerRepository.findAllByCitizenIdContainsAndStatusNotIn(citizenId, N_D_S_STATUS_LIST, paging);

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
        if (updatedWorker.getUpdatedBy() != null) {
            if (oldWorker.getUpdatedBy() != null) {
                if (!oldWorker.getUpdatedBy().equals(updatedWorker.getUpdatedBy())) {
                    if (!userService.existsById(updatedWorker.getUpdatedBy())) {
                        errorMsg += "No User (UpdatedBy) found with Id: '" + updatedWorker.getUpdatedBy()
                                + "'. Which violate constraint: FK_Worker_User_UpdatedBy. ";
                    }
                }
            } else {
                if (!userService.existsById(updatedWorker.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedWorker.getUpdatedBy()
                            + "'. Which violate constraint: FK_Worker_User_UpdatedBy. ";
                }
            }
        }

        /* Check duplicate */
        if (workerRepository
                .existsByCitizenIdAndWorkerIdIsNotAndStatusNotIn(
                        updatedWorker.getCitizenId(),
                        updatedWorker.getWorkerId(),
                        N_D_S_STATUS_LIST)) {
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
        modelMapper.typeMap(WorkerUpdateDTO.class, Worker.class)
                .addMappings(mapper -> {
                    mapper.skip(Worker::setBirthday);});

        Worker updatedWorker = modelMapper.map(updatedWorkerDTO, Worker.class);

        if (updatedWorkerDTO.getBirthday() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            updatedWorker.setBirthday(new java.sql.Date(
                    sdf.parse(updatedWorkerDTO.getBirthday()).getTime()));

            /* Now */
            Calendar calendar = Calendar.getInstance();
            /* Tuổi tối thiểu lao động TODO:(18 hay 16?) */
            /* Perform addition/subtraction (số dương +, số âm -) */
            calendar.add(Calendar.YEAR, -18);

            /* Convert calendar to Date */
            Date minLegalAgeBirthday = calendar.getTime();

            if (updatedWorker.getBirthday().after(minLegalAgeBirthday)) {
                throw new IllegalArgumentException(
                        "This worker birthday: '" + updatedWorker.getBirthday()
                                + "' mean they are younger than 18. Which violate labour law. "
                                + " Valid birthday must be before: '" + sdf.format(minLegalAgeBirthday) + "'. ");
            }
        }

        /* Update associated Location if changed */
        LocationUpdateDTO address = updatedWorkerDTO.getAddress();
        if (address != null) {
            if (locationService.updateLocationByDTO(address) == null) {
                /* Not found location with Id, NEED TO STOP */
                throw new IllegalArgumentException("No Location found with Id: '"
                        + address.getLocationId() + "' to update");
            }
        }

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

        /* Delete associated File (In DB And Firebase) */
        List<ExternalFileReadDTO> fileDTOList =
                eFEWPairingService
                        .getAllExternalFileDTOByEntityIdAndEntityType(workerId, ENTITY_TYPE);

        if (fileDTOList != null && !fileDTOList.isEmpty()) {
            fileCombineService.deleteAllFileInDBAndFirebaseByFileDTO(fileDTOList);
        }

        /* Delete associated EntityWrapper => All EFEWPairing */
        entityWrapperService.deleteByEntityIdAndEntityType(workerId, ENTITY_TYPE);

        worker.setStatus(Status.DELETED);
        workerRepository.saveAndFlush(worker);

        return true;
    }

    /* Utils */
    private WorkerReadDTO fillDTO(Worker worker) throws Exception {
        modelMapper.typeMap(Worker.class, WorkerReadDTO.class)
            .addMappings(mapper -> {
                mapper.skip(WorkerReadDTO::setCreatedAt);
                mapper.skip(WorkerReadDTO::setUpdatedAt);});

        long workerId = worker.getWorkerId();

        WorkerReadDTO workerDTO = modelMapper.map(worker, WorkerReadDTO.class);

        if (worker.getCreatedAt() != null)
            workerDTO.setCreatedAt(worker.getCreatedAt().format(dateTimeFormatter));
        if (worker.getUpdatedAt() != null)
            workerDTO.setUpdatedAt(worker.getUpdatedAt().format(dateTimeFormatter));

        /* Get associated Location */
        workerDTO.setAddress(
                locationService.getDTOById(worker.getAddressId()));
        /* Get associated ExternalFile */
        List<ExternalFileReadDTO> fileReadDTOList =
                eFEWPairingService
                        .getAllExternalFileDTOByEntityIdAndEntityType(workerId, ENTITY_TYPE);

        if (fileReadDTOList != null && !fileReadDTOList.isEmpty()) {
            /* if (fileReadDTOList.size() > 1)
                Log Error, worker only have 1 avatar file at a time */
            workerDTO.setFile(fileReadDTOList.get(0));
        }

        /* Đang bận làm / rảnh */
        workerDTO.setIsAvailable(projectWorkerService.getAllByWorkerId(workerId) == null);

        return workerDTO;
    }

    private List<WorkerReadDTO> fillAllDTO(Collection<Worker> workerCollection, Integer totalPage) throws Exception {
        modelMapper.typeMap(Worker.class, WorkerReadDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(WorkerReadDTO::setCreatedAt);
                    mapper.skip(WorkerReadDTO::setUpdatedAt);});

        Set<Long> locationIdSet = new HashSet<>();
        Set<Long> workerIdSet = new HashSet<>();

        for (Worker worker : workerCollection) {
            locationIdSet.add(worker.getAddressId());
            workerIdSet.add(worker.getWorkerId());
        }

        /* Get associated Location */
        Map<Long, LocationReadDTO> locationIdLocationDTOMap =
                locationService.mapLocationIdLocationDTOByIdIn(locationIdSet);
        /* Get associated ExternalFile */
        Map<Long, List<ExternalFileReadDTO>> workerIdExternalFileDTOListMap =
                eFEWPairingService
                        .mapEntityIdExternalFileDTOListByEntityIdInAndEntityType(workerIdSet, ENTITY_TYPE);

        Map<Long, List<ProjectWorker>> workerIdProjectWorkerMap =
                projectWorkerService.mapWorkerIdProjectWorkerListByWorkerIdIn(workerIdSet);

        return workerCollection.stream()
                .map(worker -> {
                    WorkerReadDTO workerDTO =
                            modelMapper.map(worker, WorkerReadDTO.class);

                    if (worker.getCreatedAt() != null)
                        workerDTO.setCreatedAt(worker.getCreatedAt().format(dateTimeFormatter));
                    if (worker.getUpdatedAt() != null)
                        workerDTO.setUpdatedAt(worker.getUpdatedAt().format(dateTimeFormatter));

                    long workerId = worker.getWorkerId();

                    workerDTO.setAddress(locationIdLocationDTOMap.get(worker.getAddressId()));

                    List<ExternalFileReadDTO> fileReadDTOList =
                            workerIdExternalFileDTOListMap.get(workerId);

                    if (fileReadDTOList != null && !fileReadDTOList.isEmpty()) {
                        /* if (fileReadDTOList.size() > 1)
                            Log Error, worker only have 1 avatar file at a time */
                        workerDTO.setFile(fileReadDTOList.get(0));
                    }

                    /* Đang bận làm / rảnh */
                    workerDTO.setIsAvailable(workerIdProjectWorkerMap.get(workerId) == null);

                    workerDTO.setTotalPage(totalPage);

                    return workerDTO;})
                .collect(Collectors.toList());
    }
}
