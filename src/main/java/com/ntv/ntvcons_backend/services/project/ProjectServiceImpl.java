package com.ntv.ntvcons_backend.services.project;

import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project createProject(String projectName, int locationId, Timestamp startDate, Timestamp endDate, int blueprintId, Double estimateCost) {
        return null;
    }

    @Override
    public List<Project> getAll() {
        return null;
    }

    @Override
    public List<Project> getAllByIdIn(Collection<Integer> projectIdCollection) {
        return null;
    }

    @Override
    public List<Project> getAllByNameLike(String projectName) {
        return null;
    }

    @Override
    public List<Project> getAllByLocationIdIn(Collection<Integer> locationIdCollection) {
        return null;
    }

    @Override
    public List<Project> getAllByStartDateBetween(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<Project> getAllByEndDateBetween(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<Project> getAllByEstimateCostBetween(double from, double to) {
        return null;
    }

    @Override
    public Project getByLocationId(int locationId) {
        return null;
    }

    @Override
    public Project getByBlueprintId(int blueprintId) {
        return null;
    }

    @Override
    public Project getById(int projectId) {
        return null;
    }

    @Override
    public Project updateProject(int projectId, String projectName, int locationId, Timestamp startDate, Timestamp endDate, int blueprintId, Double estimateCost) {
        return null;
    }

    @Override
    public boolean deleteProject(int projectId) {
        return false;
    }
}
