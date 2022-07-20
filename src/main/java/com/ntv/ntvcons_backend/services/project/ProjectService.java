package com.ntv.ntvcons_backend.services.project;

import com.ntv.ntvcons_backend.dtos.project.ProjectCreateDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectReadDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectUpdateDTO;
import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.ProjectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.UpdateProjectModel;
import com.ntv.ntvcons_backend.entities.UserModels.ListUserIDAndName;
import com.ntv.ntvcons_backend.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface ProjectService extends BaseService {
    /* CREATE */
    boolean createProject(CreateProjectModel createProjectModel) throws Exception;

    Project createProject(Project newProject) throws Exception;
    ProjectReadDTO createProjectByDTO(ProjectCreateDTO newProjectDTO) throws Exception;

    /* READ */
    List<ProjectModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    Page<Project> getPageAll(Pageable paging) throws Exception;
    List<ProjectReadDTO> getAllInPaging(Pageable paging) throws Exception;

    boolean existsById(long projectId) throws Exception;
    Project getById(long projectId) throws Exception;
    ProjectReadDTO getDTOById(long projectId) throws Exception;

    List<ProjectModel> getAllById(long projectId, int pageNo, int pageSize, String sortBy, boolean sortType);

    boolean existsAllByIdIn(Collection<Long> projectIdCollection) throws Exception;
    List<Project> getAllByIdIn(Collection<Long> projectIdCollection) throws Exception;
    List<ProjectReadDTO> getAllDTOByIdIn(Collection<Long> projectIdCollection) throws Exception;

    List<Project> getAllByLocationId(long locationId) throws Exception;
    List<ProjectReadDTO> getAllDTOByLocationId(long locationId) throws Exception;
    Page<Project> getPageAllByLocationId(Pageable paging, long locationId) throws Exception;
    List<ProjectReadDTO> getAllDTOInPagingByLocationId(Pageable paging, long locationId) throws Exception;

    List<Project> getAllByLocationIdIn(Collection<Long> locationIdCollection) throws Exception;
    List<ProjectReadDTO> getAllDTOByLocationIdIn(Collection<Long> locationIdCollection) throws Exception;
    Page<Project> getPageAllByLocationIdIn(Pageable paging, Collection<Long> locationIdCollection) throws Exception;
    List<ProjectReadDTO> getAllDTOInPagingByLocationIdIn(Pageable paging, Collection<Long> locationIdCollection) throws Exception;

    boolean checkDuplicate(String projectName) throws Exception;

    Project getByProjectName(String projectName) throws Exception;
    ProjectReadDTO getDTOByProjectName(String projectName) throws Exception;

    List<Project> getAllByProjectNameContains(String projectName) throws Exception;
    List<ProjectReadDTO> getAllDTOByProjectNameContains(String projectName) throws Exception;
    Page<Project> getPageAllByProjectNameContains(Pageable paging, String projectName) throws Exception;
    List<ProjectReadDTO> getAllDTOInPagingByProjectNameContains(Pageable paging, String projectName) throws Exception;

    List<Project> getAllByStartDateBetween(Timestamp from, Timestamp to);

    List<Project> getAllByEndDateBetween(Timestamp from, Timestamp to);

    List<Project> getAllByEstimateCostBetween(double from, double to);

    List<ListUserIDAndName> getUserForDropdownSelection();

    /* UPDATE */
    boolean updateProject(UpdateProjectModel updateProjectModel);

    Project updateProject(Project UpdatedProject) throws Exception;
    ProjectReadDTO updateProjectByDTO(ProjectUpdateDTO UpdatedProjectDTO) throws Exception;

    /* DELETE */
    boolean deleteProject(long projectId) throws Exception;
}
