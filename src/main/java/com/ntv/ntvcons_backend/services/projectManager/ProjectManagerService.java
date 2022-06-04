package com.ntv.ntvcons_backend.services.projectManager;

import com.ntv.ntvcons_backend.entities.ProjectManager;

import java.util.Collection;
import java.util.List;

public interface ProjectManagerService {
    /* CREATE */
    ProjectManager createProjectManager(int managerId, int projectId);

    /* READ */
    List<ProjectManager> getAll();

    List<ProjectManager> getAllByIdIn(Collection<Integer> projectManagerIdCollection);

    List<ProjectManager> getAllByManagerId(int managerId);

    List<ProjectManager> getAllByProjectId(int projectId);

    ProjectManager getById(int projectManagerId);

    /* UPDATE */
    ProjectManager updateProjectManager(int projectManagerId, int managerId, int projectId);

    /* DELETE */
    boolean deleteProjectManager(int projectManagerId);
}
