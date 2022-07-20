package com.ntv.ntvcons_backend.services.worker;

import com.ntv.ntvcons_backend.dtos.worker.WorkerCreateDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerReadDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerUpdateDTO;
import com.ntv.ntvcons_backend.entities.Worker;
import com.ntv.ntvcons_backend.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface WorkerService extends BaseService {
    /* CREATE */
    Worker createWorker(Worker newWorker) throws Exception;
    WorkerReadDTO createWorkerByDTO(WorkerCreateDTO newWorkerDTO) throws Exception;

    /* READ */
    Page<Worker> getPageAll(Pageable paging) throws Exception;
    List<WorkerReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    boolean existsById(long workerId) throws Exception;
    Worker getById(long workerId) throws Exception;
    WorkerReadDTO getDTOById(long workerId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> workerIdCollection) throws Exception;
    List<Worker> getAllByIdIn(Collection<Long> workerIdCollection) throws Exception;
    List<WorkerReadDTO> getAllDTOByIdIn(Collection<Long> workerIdCollection) throws Exception;
    Map<Long, WorkerReadDTO> mapWorkerIdWorkerDTOByIdIn(Collection<Long> workerIdCollection) throws Exception;

    List<Worker> getAllByAddressId(long addressId) throws Exception;
    List<WorkerReadDTO> getAllDTOByAddressId(long addressId) throws Exception;
    Page<Worker> getPageAllByAddressId(Pageable paging, long addressIdd) throws Exception;
    List<WorkerReadDTO> getAllDTOInPagingByAddressId(Pageable paging, long addressId) throws Exception;

    List<Worker> getAllByFullName(String fullName) throws Exception;
    List<WorkerReadDTO> getAllDTOByFullName(String fullName) throws Exception;
    Page<Worker> getPageAllByFullName(Pageable paging, String fullName) throws Exception;
    List<WorkerReadDTO> getAllDTOInPagingByFullName(Pageable paging, String fullName) throws Exception;

    List<Worker> getAllByFullNameContains(String fullName) throws Exception;
    List<WorkerReadDTO> getAllDTOByFullNameContains(String fullName) throws Exception;
    Page<Worker> getPageAllByFullNameContains(Pageable paging, String fullName) throws Exception;
    List<WorkerReadDTO> getAllDTOInPagingByFullNameContains(Pageable paging, String fullName) throws Exception;

    Worker getByCitizenId(String citizenId) throws Exception;
    WorkerReadDTO getDTOByCitizenId(String citizenId) throws Exception;

    List<Worker> getAllByCitizenIdContains(String citizenId) throws Exception;
    List<WorkerReadDTO> getAllDTOByCitizenIdContains(String citizenId) throws Exception;
    Page<Worker> getPageAllByCitizenIdContains(Pageable paging, String citizenId) throws Exception;
    List<WorkerReadDTO> getAllDTOInPagingByCitizenIdContains(Pageable paging, String citizenId) throws Exception;

    /* UPDATE */
    Worker updateWorker(Worker updatedWorker) throws Exception;
    WorkerReadDTO updateWorkerByDTO(WorkerUpdateDTO updatedWorkerDTO) throws Exception;

    /* DELETE */
    boolean deleteWorker(long workerId) throws Exception;
}
