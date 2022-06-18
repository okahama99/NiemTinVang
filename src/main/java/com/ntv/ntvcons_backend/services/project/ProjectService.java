package com.ntv.ntvcons_backend.services.project;

import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.entities.UserModels.ListUserIDAndName;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface ProjectService {
    /* CREATE */
    boolean createProject(CreateProjectModel createProjectModel);

    /* READ */
    List<ProjectModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    boolean existsById(long projectId);
    Project getById(long projectId);

    List<Project> getAllByIdIn(Collection<Long> projectIdCollection);

    Project getByLocationId(long locationId);

    boolean checkDuplicate(String projectName);

    List<ProjectModel> getAllById(long projectId, int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Project> getAllByNameContains(String projectName);

    List<Project> getAllByLocationIdIn(Collection<Long> locationIdCollection);

    List<Project> getAllByStartDateBetween(Timestamp from, Timestamp to);

    List<Project> getAllByEndDateBetween(Timestamp from, Timestamp to);

    List<Project> getAllByEstimateCostBetween(double from, double to);

    /* UPDATE */
    boolean updateProject(ProjectModel projectModel);

    /* DELETE */
    boolean deleteProject(long projectId);

    List<ListUserIDAndName> getUserForDropdownSelection();
}
