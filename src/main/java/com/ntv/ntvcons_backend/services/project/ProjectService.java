package com.ntv.ntvcons_backend.services.project;

import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBluePrintModel;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface ProjectService {
    /* CREATE */
    String createProject(String projectName, CreateLocationModel createLocationModel,
                          CreateBluePrintModel createBluePrintModel, Instant planStartDate, Instant planEndDate,
                          Instant actualStartDate, Instant actualEndDate, double estimateCost, double actualCost);

    /* READ */
    List<ProjectModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

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
    String updateProject(ProjectModel projectModel);

    /* DELETE */
    boolean deleteProject(long projectId);

}
