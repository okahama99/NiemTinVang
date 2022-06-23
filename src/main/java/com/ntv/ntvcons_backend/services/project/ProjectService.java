package com.ntv.ntvcons_backend.services.project;

import com.ntv.ntvcons_backend.dtos.project.ProjectCreateDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectReadDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectUpdateDTO;
import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.ProjectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.UpdateProjectModel;
import com.ntv.ntvcons_backend.entities.UserModels.ListUserIDAndName;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface ProjectService {
    /* CREATE */
    boolean createProject(CreateProjectModel createProjectModel) throws Exception;
    Project createProject(Project newProject) throws Exception;
    ProjectReadDTO createProjectByDTO(ProjectCreateDTO newProjectDTO) throws Exception;

    /* READ */
    List<ProjectModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    boolean existsById(long projectId);
    Project getById(long projectId);

    boolean existsAllByIdIn(Collection<Long> projectIdCollection);
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
    boolean updateProject(UpdateProjectModel updateProjectModel);

    Project updateProject(Project UpdatedProject) throws Exception;
    ProjectReadDTO updateProjectByDTO(ProjectUpdateDTO UpdatedProjectDTO) throws Exception;

    /* DELETE */
    boolean deleteProject(long projectId);

    List<ListUserIDAndName> getUserForDropdownSelection();
}
