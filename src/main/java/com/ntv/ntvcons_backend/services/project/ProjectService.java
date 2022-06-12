package com.ntv.ntvcons_backend.services.project;

import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.projectModels.ShowProjectModel;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface ProjectService {
    /* CREATE */
    Project createProject(String projectName, int locationId, Timestamp startDate, Timestamp endDate,
                          int blueprintId, Double estimateCost);

    /* READ */
    List<ShowProjectModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Project> getAllByIdIn(Collection<Integer> projectIdCollection);

    List<Project> getAllByNameContains(String projectName);

    List<Project> getAllByLocationIdIn(Collection<Integer> locationIdCollection);

    List<Project> getAllByStartDateBetween(Timestamp from, Timestamp to);

    List<Project> getAllByEndDateBetween(Timestamp from, Timestamp to);

    List<Project> getAllByEstimateCostBetween(double from, double to);

    Project getByLocationId(int locationId);

    Project getByBlueprintId(int blueprintId);

    Project getById(int projectId);

    /* UPDATE */
    boolean updateProject(ShowProjectModel showProjectModel);

    /* DELETE */
    boolean deleteProject(int projectId);

}
