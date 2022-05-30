package com.ntv.ntvcons_backend.services.projectBlueprint;

import com.ntv.ntvcons_backend.entities.ProjectBlueprint;
import com.ntv.ntvcons_backend.repositories.ProjectBlueprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProjectBlueprintServiceImpl implements ProjectBlueprintService {
    @Autowired
    private ProjectBlueprintRepository projectBlueprintRepository;

    @Override
    public ProjectBlueprint createProjectBlueprint(String projectBlueprintName, int designerId, double projectBlueprintCost) {
        return null;
    }

    @Override
    public List<ProjectBlueprint> getAll() {
        return null;
    }

    @Override
    public List<ProjectBlueprint> getAllByIdIn(Collection<Integer> projectBlueprintIdCollection) {
        return null;
    }

    @Override
    public List<ProjectBlueprint> getAllByProjectBlueprintNameLike(String projectBlueprintName) {
        return null;
    }

    @Override
    public List<ProjectBlueprint> getAllByProjectBlueprintCostBetween(double from, double to) {
        return null;
    }

    @Override
    public ProjectBlueprint getByDesignerId(int designerId) {
        return null;
    }

    @Override
    public ProjectBlueprint getById(int projectBlueprintId) {
        return null;
    }

    @Override
    public ProjectBlueprint updateProjectBlueprint(int projectBlueprintId, String projectBlueprintName, int designerId, double projectBlueprintCost) {
        return null;
    }

    @Override
    public boolean deleteProjectBlueprint(int projectBlueprintId) {
        return false;
    }
}
