package com.ntv.ntvcons_backend.services.projectWorker;

import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerCreateDTO;
import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerUpdateDTO;
import com.ntv.ntvcons_backend.entities.ProjectWorker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProjectWorkerService {
    /* CREATE */
    ProjectWorker createProjectWorker(ProjectWorker newProjectWorker) throws Exception;
    ProjectWorkerReadDTO createProjectWorkerByDTO(ProjectWorkerCreateDTO newProjectWorkerDTO) throws Exception;

    List<ProjectWorker> createBulkProjectWorker(List<ProjectWorker> newProjectWorkerList) throws Exception;
    List<ProjectWorkerReadDTO> createBulkProjectWorkerByDTO(List<ProjectWorkerCreateDTO> newProjectWorkerDTOList) throws Exception;

    /* READ */
    Page<ProjectWorker> getPageAll(Pageable paging) throws Exception;
    List<ProjectWorkerReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    ProjectWorker getById(long projectWorkerId) throws Exception;
    ProjectWorkerReadDTO getDTOById(long projectWorkerId) throws Exception;

    List<ProjectWorker> getAllByIdIn(Collection<Long> projectWorkerIdCollection) throws Exception;
    List<ProjectWorkerReadDTO> getAllDTOByIdIn(Collection<Long> projectWorkerIdCollection) throws Exception;

    List<ProjectWorker> getAllByWorkerId(long workerId) throws Exception;
    List<ProjectWorkerReadDTO> getAllDTOByWorkerId(long workerId) throws Exception;
    Page<ProjectWorker> getPageAllByWorkerId(Pageable paging, long workerId) throws Exception;
    List<ProjectWorkerReadDTO> getAllDTOInPagingByWorkerId(Pageable paging, long workerId) throws Exception;

    List<ProjectWorker> getAllByWorkerIdIn(Collection<Long> workerIdCollection) throws Exception;
    List<ProjectWorkerReadDTO> getAllDTOByWorkerIdIn(Collection<Long> workerIdCollection) throws Exception;
    Page<ProjectWorker> getPageAllByWorkerIdIn(Pageable paging, Collection<Long> workerIdCollection) throws Exception;
    List<ProjectWorkerReadDTO> getAllDTOInPagingByWorkerIdIn(Pageable paging, Collection<Long> workerIdCollection) throws Exception;

    List<ProjectWorker> getAllByProjectId(long projectId) throws Exception;
    List<ProjectWorkerReadDTO> getAllDTOByProjectId(long projectId) throws Exception;
    Page<ProjectWorker> getPageAllByProjectId(Pageable paging, long projectId) throws Exception;
    List<ProjectWorkerReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception;

    List<ProjectWorker> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    List<ProjectWorkerReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    Map<Long, List<ProjectWorkerReadDTO>> mapProjectIdProjectWorkerDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    Page<ProjectWorker> getPageAllByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception;
    List<ProjectWorkerReadDTO> getAllDTOInPagingByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception;

    /* UPDATE */
    ProjectWorker updateProjectWorker(ProjectWorker updatedProjectWorker) throws Exception;
    ProjectWorkerReadDTO updateProjectWorkerByDTO(ProjectWorkerUpdateDTO updatedProjectWorkerDTO) throws Exception;

    List<ProjectWorker> updateBulkProjectWorker(List<ProjectWorker> updatedProjectWorkerList) throws Exception;
    List<ProjectWorkerReadDTO> updateBulkProjectWorkerByDTO(List<ProjectWorkerUpdateDTO> updatedProjectWorkerDTOList) throws Exception;

    /* DELETE */
    boolean removeProjectWorker(long projectWorkerId) throws Exception;

    boolean removeAllByWorkerId(long workerId) throws Exception;
    boolean removeAllByWorkerIdIn(Collection<Long> workerIdCollection) throws Exception;

    boolean removeAllByProjectId(long projectId) throws Exception;
    boolean removeAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;

    boolean deleteProjectWorker(long projectWorkerId) throws Exception;

    /** Cascade when delete Worker */
    boolean deleteAllByWorkerId(long workerId) throws Exception;

    /** Cascade when delete Project */
    boolean deleteAllByProjectId(long projectId) throws Exception;
}
