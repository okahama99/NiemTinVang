package com.ntv.ntvcons_backend.services.projectManager;

import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerCreateDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerUpdateDTO;
import com.ntv.ntvcons_backend.entities.ProjectManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProjectManagerService {
    /* CREATE */
    ProjectManager createProjectManager(ProjectManager newProjectManager) throws Exception;
    ProjectManagerReadDTO createProjectManagerByDTO(ProjectManagerCreateDTO newProjectManagerDTO) throws Exception;

    List<ProjectManager> createBulkProjectManager(List<ProjectManager> newProjectManagerList) throws Exception;
    List<ProjectManagerReadDTO> createBulkProjectManagerByDTO(List<ProjectManagerCreateDTO> newProjectManagerDTOList) throws Exception;

    /* READ */
    Page<ProjectManager> getPageAll(Pageable paging) throws Exception;
    List<ProjectManagerReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    ProjectManager getById(long projectManagerId) throws Exception;
    ProjectManagerReadDTO getDTOById(long projectManagerId) throws Exception;

    List<ProjectManager> getAllByIdIn(Collection<Long> projectManagerIdCollection) throws Exception;
    List<ProjectManagerReadDTO> getAllDTOByIdIn(Collection<Long> projectManagerIdCollection) throws Exception;

    List<ProjectManager> getAllByManagerId(long managerId) throws Exception;
    List<ProjectManagerReadDTO> getAllDTOByManagerId(long managerId) throws Exception;
    Page<ProjectManager> getPageAllByManagerId(Pageable paging, long managerId) throws Exception;
    List<ProjectManagerReadDTO> getAllDTOInPagingByManagerId(Pageable paging, long managerId) throws Exception;

    List<ProjectManager> getAllByManagerIdIn(Collection<Long> managerIdCollection) throws Exception;
    List<ProjectManagerReadDTO> getAllDTOByManagerIdIn(Collection<Long> managerIdCollection) throws Exception;
    Page<ProjectManager> getPageAllByManagerIdIn(Pageable paging, Collection<Long> managerIdCollection) throws Exception;
    List<ProjectManagerReadDTO> getAllDTOInPagingByManagerIdIn(Pageable paging, Collection<Long> managerIdCollection) throws Exception;

    List<ProjectManager> getAllByProjectId(long projectId) throws Exception;
    List<ProjectManagerReadDTO> getAllDTOByProjectId(long projectId) throws Exception;
    Page<ProjectManager> getPageAllByProjectId(Pageable paging, long projectId) throws Exception;
    List<ProjectManagerReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception;

    List<ProjectManager> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    List<ProjectManagerReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    Map<Long, List<ProjectManagerReadDTO>> mapProjectIdProjectManagerDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    Page<ProjectManager> getPageAllByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception;
    List<ProjectManagerReadDTO> getAllDTOInPagingByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception;

    /* UPDATE */
    ProjectManager updateProjectManager(ProjectManager updatedProjectManager) throws Exception;
    ProjectManagerReadDTO updateProjectManagerByDTO(ProjectManagerUpdateDTO updatedProjectManagerDTO) throws Exception;

    List<ProjectManager> updateBulkProjectManager(List<ProjectManager> updatedProjectManagerList) throws Exception;
    List<ProjectManagerReadDTO> updateBulkProjectManagerByDTO(List<ProjectManagerUpdateDTO> updatedProjectManagerDTOList) throws Exception;

    /* DELETE */
    boolean removeProjectManager(long projectManagerId) throws Exception;

    boolean removeAllByUserId(long userId) throws Exception;
    boolean removeAllByUserIdIn(Collection<Long> userIdCollection) throws Exception;

    boolean removeAllByProjectId(long projectId) throws Exception;
    boolean removeAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;

    boolean deleteProjectManager(long projectManagerId) throws Exception;

    /** Cascade when delete User */
    boolean deleteAllByUserId(long userId) throws Exception;

    /** Cascade when delete Project */
    boolean deleteAllByProjectId(long projectId) throws Exception;
}
