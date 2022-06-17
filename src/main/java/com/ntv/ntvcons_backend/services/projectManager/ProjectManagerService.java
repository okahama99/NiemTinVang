package com.ntv.ntvcons_backend.services.projectManager;

import com.ntv.ntvcons_backend.entities.ProjectManager;
import com.ntv.ntvcons_backend.entities.ProjectManagerModels.CreateProjectManagerModel;

import java.util.Collection;
import java.util.List;

public interface ProjectManagerService {
    /* CREATE */
    void createProjectManager(CreateProjectManagerModel createProjectManagerModel);

    /* READ */
    List<ProjectManager> getAll();

    List<ProjectManager> getAllByIdIn(Collection<Integer> projectManagerIdCollection);

    List<ProjectManager> getAllByManagerId(int managerId);

    List<ProjectManager> getAllByProjectId(int projectId);

    ProjectManager getById(int projectManagerId);

    /* UPDATE */
    boolean updateProjectManager(long projectManagerId, long userId);

    /* DELETE */
    boolean deleteProjectManager(long projectManagerId);
}
