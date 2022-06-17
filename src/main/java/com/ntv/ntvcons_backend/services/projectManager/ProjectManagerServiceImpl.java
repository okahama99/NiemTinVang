package com.ntv.ntvcons_backend.services.projectManager;

import com.ntv.ntvcons_backend.entities.ProjectManager;
import com.ntv.ntvcons_backend.entities.ProjectManagerModels.CreateProjectManagerModel;
import com.ntv.ntvcons_backend.repositories.ProjectManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {
    @Autowired
    private ProjectManagerRepository projectManagerRepository;

    /* CREATE */
    @Override
    public void createProjectManager(CreateProjectManagerModel createProjectManagerModel) {
        ProjectManager projectManager = new ProjectManager();
        projectManager.setProjectId(createProjectManagerModel.getProjectId());
        projectManager.setManagerId(createProjectManagerModel.getManagerId());
        projectManager.setAssignDate(createProjectManagerModel.getAssignDate());
        projectManagerRepository.saveAndFlush(projectManager);
    }

    /* READ */
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

    /* UPDATE */
    @Override
    public boolean updateProjectManager(long projectManagerId, long userId) {
        ProjectManager projectManager = projectManagerRepository.findById(projectManagerId).get();
        if(projectManager!=null)
        {
            projectManager.setRemoveDate(LocalDateTime.now());
            Date date = new Date();
            projectManager.setUpdatedAt(date);
            projectManager.setUpdatedBy(userId);
            projectManagerRepository.saveAndFlush(projectManager);
            return true;
        }
        return false;
    }

    /* DELETE */
    @Override
    public boolean deleteProjectManager(long projectManagerId) {
        ProjectManager projectManager = projectManagerRepository.findById(projectManagerId).get();
        if(projectManager!=null)
        {
            projectManager.setIsDeleted(true);
            projectManagerRepository.saveAndFlush(projectManager);
            return true;
        }
        return false;
    }
}
