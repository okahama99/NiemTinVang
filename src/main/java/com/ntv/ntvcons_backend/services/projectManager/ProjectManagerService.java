package com.ntv.ntvcons_backend.services.projectManager;

import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerUpdateDTO;
import com.ntv.ntvcons_backend.entities.ProjectManager;

import java.util.Collection;
import java.util.List;

public interface ProjectManagerService {
    /* CREATE */
    ProjectManager createProjectManager(ProjectManager newProjectManager) throws Exception;

    List<ProjectManager> createBulkProjectManager(List<ProjectManager> newProjectManagerList) throws Exception;

    /* READ */
    List<ProjectManager> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    ProjectManager getById(long projectManagerId) throws Exception;

    List<ProjectManager> getAllByIdIn(Collection<Long> projectManagerIdCollection) throws Exception;

    List<ProjectManager> getAllByManagerId(long managerId) throws Exception;

    List<ProjectManager> getAllByProjectId(long projectId) throws Exception;

    /* UPDATE */
    ProjectManager updateProjectManager(ProjectManager updatedProjectManager) throws Exception;
    ProjectManagerReadDTO updateProjectManagerByDTO(ProjectManagerUpdateDTO updatedProjectManagerDTO) throws Exception;

    List<ProjectManager> updateBulkProjectManager(List<ProjectManager> updatedProjectManagerList) throws Exception;

    /* DELETE */
    boolean deleteProjectManager(long projectManagerId) throws Exception;

    /** Cascade when delete User */
    boolean deleteAllByUserId(long userId) throws Exception;

    /** Cascade when delete Project */
    boolean deleteAllByProjectId(long projectId) throws Exception;
}
