package com.ntv.ntvcons_backend.services.projectManager;

import com.ntv.ntvcons_backend.repositories.ProjectManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {
    @Autowired
    private ProjectManagerRepository projectManagerRepository;

    @Override
    public ProjectManager createProjectManager(int managerId, int projectId) {
        return null;
    }

    @Override
    public List<ProjectManager> getAll() {
        return null;
    }

    @Override
    public List<ProjectManager> getAllByIdIn(Collection<Integer> projectManagerIdCollection) {
        return null;
    }

    @Override
    public List<ProjectManager> getAllByManagerId(int managerId) {
        return null;
    }

    @Override
    public List<ProjectManager> getAllByProjectId(int projectId) {
        return null;
    }

    @Override
    public ProjectManager getById(int projectManagerId) {
        return null;
    }

    @Override
    public ProjectManager updateProjectManager(int projectManagerId, int managerId, int projectId) {
        return null;
    }

    @Override
    public boolean deleteProjectManager(int projectManagerId) {
        return false;
    }
}
