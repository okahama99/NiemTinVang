package com.ntv.ntvcons_backend.services.project;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectCreateDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectReadDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectUpdateDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import com.ntv.ntvcons_backend.entities.*;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.UpdateProjectModel;
import com.ntv.ntvcons_backend.entities.UserModels.ListUserIDAndName;
import com.ntv.ntvcons_backend.repositories.BlueprintRepository;
import com.ntv.ntvcons_backend.repositories.LocationRepository;
import com.ntv.ntvcons_backend.repositories.ProjectRepository;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintService;
import com.ntv.ntvcons_backend.services.location.LocationService;
import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
import com.ntv.ntvcons_backend.services.report.ReportService;
import com.ntv.ntvcons_backend.services.request.RequestService;
import com.ntv.ntvcons_backend.services.task.TaskService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private BlueprintService blueprintService;
    @Autowired
    private BlueprintRepository blueprintRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectManagerService projectManagerService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private RequestService requestService;

    /* READ */
    @Override
    public boolean createProject(CreateProjectModel createProjectModel) throws Exception {
                CreateLocationModel locationModel = new CreateLocationModel();
                locationModel.setAddressNumber(createProjectModel.getAddressNumber());
                locationModel.setStreet(createProjectModel.getStreet());
                locationModel.setArea(createProjectModel.getArea());
                locationModel.setWard(createProjectModel.getWard());
                locationModel.setDistrict(createProjectModel.getDistrict());
                locationModel.setCity(createProjectModel.getCity());
                locationModel.setProvince(createProjectModel.getProvince());
                locationModel.setCountry(createProjectModel.getCountry());
                locationModel.setCoordinate(createProjectModel.getCoordinate());
                locationService.createLocation(locationModel);

                // TODO
                // Sửa xong blueprint thì mở
//                CreateBlueprintModel blueprintModel = new CreateBlueprintModel();
//                blueprintModel.setDesignerName(createProjectModel.getDesignerName());
//                blueprintModel.setProjectBlueprintName(createProjectModel.getProjectBlueprintName());
//                blueprintModel.setEstimateCost(createProjectModel.getBlueprintEstimateCost());
//                blueprintService.createProjectBlueprint(blueprintModel);

                Location location = locationRepository.getByAddressNumberAndIsDeletedIsFalse(locationModel.getAddressNumber());

                Project project = new Project();
                project.setProjectName(createProjectModel.getProjectName());
                project.setLocationId(location.getLocationId());

                project.setPlanStartDate(
                        LocalDateTime.parse(createProjectModel.getPlanStartDate(),dateTimeFormatter));
                project.setPlanEndDate(
                        LocalDateTime.parse(createProjectModel.getPlanEndDate(),dateTimeFormatter));
                project.setActualStartDate(
                        LocalDateTime.parse(createProjectModel.getActualStartDate(),dateTimeFormatter));
                project.setActualEndDate(
                        LocalDateTime.parse(createProjectModel.getActualEndDate(),dateTimeFormatter));

                project.setActualCost(createProjectModel.getProjectActualCost());
                project.setEstimatedCost(createProjectModel.getProjectEstimateCost());
                projectRepository.saveAndFlush(project);

        ProjectManagerCreateDTO projectManagerDTO = new ProjectManagerCreateDTO();
        projectManagerDTO.setProjectId(project.getProjectId());
        projectManagerDTO.setManagerId(createProjectModel.getUserId());
        projectManagerDTO.setAssignDate(LocalDateTime.now().format(dateTimeFormatter));
        projectManagerService.createProjectManager(modelMapper.map(projectManagerDTO, ProjectManager.class));
        return true;
    }

    @Override
    public Project createProject(Project newProject) throws Exception {
        /* TODO: also create EntityWrapper for project */

        String errorMsg = "";

        /* Check FK */
        if (!userService.existsById(newProject.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newProject.getCreatedBy()
                    + "'. Which violate constraint: FK_Project_User_CreatedBy. ";
        }
        if (!locationService.existsById(newProject.getLocationId())) {
            /* Should not happen, Location are 1st created in createProjectByDTO before this are executed */
            errorMsg += "No Location found with Id: '" + newProject.getLocationId()
                    + "'. Which violate constraint: FK_Project_Location. ";
        }

        /* Check duplicate */
        if (projectRepository
                .existsByProjectNameAndIsDeletedIsFalse(
                        newProject.getProjectName())) {
            errorMsg += "Already exists another Project with name: '" + newProject.getProjectName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return projectRepository.saveAndFlush(newProject);
    }
    @Override
    public ProjectReadDTO createProjectByDTO(ProjectCreateDTO newProjectDTO) throws Exception {
        modelMapper.typeMap(ProjectCreateDTO.class, Project.class)
                .addMappings(mapper -> {
                    mapper.skip(Project::setPlanStartDate);
                    mapper.skip(Project::setPlanEndDate);
                    mapper.skip(Project::setActualStartDate);
                    mapper.skip(Project::setActualEndDate);});

        Project newProject = modelMapper.map(newProjectDTO, Project.class);

        if (newProjectDTO.getPlanStartDate() != null) {
            newProject.setPlanStartDate(
                    LocalDateTime.parse(newProjectDTO.getPlanStartDate(), dateTimeFormatter));
        }

        if (newProjectDTO.getPlanEndDate() != null) {
            newProject.setPlanEndDate(
                    LocalDateTime.parse(newProjectDTO.getPlanEndDate(), dateTimeFormatter));
        }

        if (newProjectDTO.getActualStartDate() != null) {
            newProject.setActualStartDate(
                    LocalDateTime.parse(newProjectDTO.getActualStartDate(), dateTimeFormatter));
        }

        if (newProjectDTO.getActualEndDate() != null) {
            newProject.setActualEndDate(
                    LocalDateTime.parse(newProjectDTO.getActualEndDate(), dateTimeFormatter));
        }

        LocationReadDTO locationDTO;
        /* Create Location first (to get locationId) */
        if (newProjectDTO.getLocation().getIsNewLocation()) {
            locationDTO = locationService.createLocationByDTO(newProjectDTO.getLocation().getNewLocation());
        } else { /* Or use existing Location */
            locationDTO = locationService.getDTOById(newProjectDTO.getLocation().getExistingLocationId());
        }
        /* Set locationId because createProject() check FK */
        newProject.setLocationId(locationDTO.getLocationId());

        newProject = createProject(newProject);

        ProjectReadDTO projectDTO = modelMapper.map(newProject, ProjectReadDTO.class);

        /* TODO: Get associated Blueprint */
//        projectDTO.setBlueprint(blueprintService.getDTOByProjectId(newProject.getProjectId()));

        /* Set associated Location */
        projectDTO.setLocation(locationDTO);

        return projectDTO;
    }

    /* READ */
    @Override
    public List<ProjectModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Project> pagingResult = projectRepository.findAllByIsDeletedIsFalse(paging);

        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);

            Page<ProjectModel> modelResult =
                    pagingResult.map(new Converter<Project, ProjectModel>() {

                @Override
                protected ProjectModel doForward(Project project) {
                    ProjectModel model = new ProjectModel();

                    model.setProjectId(project.getProjectId());
                    model.setProjectName(project.getProjectName());
                    model.setPlanStartDate(project.getPlanStartDate());
                    model.setPlanEndDate(project.getPlanEndDate());
                    model.setActualStartDate(project.getActualStartDate());
                    model.setActualEndDate(project.getActualEndDate());
                    model.setProjectEstimateCost(project.getEstimatedCost());
                    model.setActualCost(project.getActualCost());
                    model.setIsDelete(project.getIsDeleted());

                    Optional<Location> location = locationRepository.findById(project.getLocationId());
                    if (location.isPresent()) { /* MISSING null check */
                        model.setLocationId(project.getLocationId());
                        model.setAddressNumber(location.get().getAddressNumber());
                        model.setStreet(location.get().getStreet());
                        model.setArea(location.get().getArea());
                        model.setWard(location.get().getWard());
                        model.setDistrict(location.get().getDistrict());
                        model.setCity(location.get().getCity());
                        model.setProvince(location.get().getProvince());
                        model.setCountry(location.get().getCountry());
                        model.setCoordinate(location.get().getCoordinate());
                    } else {
                        model.setLocationId(null);
                        model.setAddressNumber(null);
                        model.setStreet(null);
                        model.setArea(null);
                        model.setWard(null);
                        model.setDistrict(null);
                        model.setCity(null);
                        model.setProvince(null);
                        model.setCountry(null);
                        model.setCoordinate(null);
                    }

                    /* Vì đỏi FK Blueprint nên tạm bỏ qua*/

                    model.setCreatedAt(project.getCreatedAt());
                    model.setCreatedBy(project.getCreatedBy());
                    model.setUpdatedAt(project.getCreatedAt());
                    model.setUpdatedBy(project.getUpdatedBy());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Project doBackward(ProjectModel showProjectModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ProjectModel>();
        }
    }

    @Override
    public boolean existsById(long projectId) {
        return projectRepository.existsByProjectIdAndIsDeletedIsFalse(projectId);
    }
    @Override
    public Project getById(long projectId) {
        return projectRepository
                .findByProjectIdAndIsDeletedIsFalse(projectId)
                .orElse(null);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> projectIdCollection) {
        return projectRepository.existsAllByProjectIdInAndIsDeletedIsFalse(projectIdCollection);
    }
    @Override
    public List<Project> getAllByIdIn(Collection<Long> projectIdCollection) {
        return null;
    }

    @Override
    public Project getByLocationId(long locationId) {
        return null;
    }

    @Override
    public boolean checkDuplicate(String projectName) {
        Project checkDuplicateProject = projectRepository.getByProjectNameAndIsDeletedIsFalse(projectName);
        if(checkDuplicateProject != null) {
            return true;
        }
        return false;
    }

    /* get all gì? theo id thì là 1. Các entity con/phụ thuộc là chuyện khác */
    @Override
    public List<ProjectModel> getAllById(long projectId, int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Project> pagingResult = projectRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId, paging);
        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);
            Page<ProjectModel> modelResult =
                    pagingResult.map(new Converter<Project, ProjectModel>() {

                        @Override
                        protected ProjectModel doForward(Project project) {
                            ProjectModel model = new ProjectModel();

                            model.setProjectId(project.getProjectId());
                            model.setProjectName(project.getProjectName());
                            model.setPlanStartDate(project.getPlanStartDate());
                            model.setPlanEndDate(project.getPlanEndDate());
                            model.setActualStartDate(project.getActualStartDate());
                            model.setActualEndDate(project.getActualEndDate());
                            model.setProjectEstimateCost(project.getEstimatedCost());
                            model.setActualCost(project.getActualCost());
                            model.setIsDelete(project.getIsDeleted());

                            Location location = locationRepository.findById(project.getLocationId()).get();
                            model.setLocationId(project.getLocationId());
                            model.setAddressNumber(location.getAddressNumber());
                            model.setStreet(location.getStreet());
                            model.setArea(location.getArea());
                            model.setWard(location.getWard());
                            model.setDistrict(location.getDistrict());
                            model.setCity(location.getCity());
                            model.setProvince(location.getProvince());
                            model.setCountry(location.getCountry());
                            model.setCoordinate(location.getCoordinate());

                            model.setCreatedAt(project.getCreatedAt());
                            model.setCreatedBy(project.getCreatedBy());
                            model.setUpdatedAt(project.getCreatedAt());
                            model.setUpdatedBy(project.getUpdatedBy());
                            model.setTotalPage(totalPage);
                            return model;
                        }

                        @Override
                        protected Project doBackward(ProjectModel showProjectModel) {
                            return null;
                        }
                    });
            return modelResult.getContent();
        }else{
            return new ArrayList<ProjectModel>();
        }
    }

    @Override
    public List<Project> getAllByNameContains(String projectName) {
        return null;
    }

    @Override
    public List<Project> getAllByLocationIdIn(Collection<Long> locationIdCollection) {
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

    /* UPDATE */
    @Override
    public boolean updateProject(UpdateProjectModel updateProjectModel) {
            Project project = projectRepository.findById(updateProjectModel.getProjectId()).orElse(null);
            /* optional.get() mà null -> ra Exception, khỏi check null luôn  */
            if(project != null) {
                UpdateLocationModel locationModel = new UpdateLocationModel();
                locationModel.setLocationId(updateProjectModel.getLocationId());
                locationModel.setAddressNumber(updateProjectModel.getAddressNumber());
                locationModel.setStreet(updateProjectModel.getStreet());
                locationModel.setArea(updateProjectModel.getArea());
                locationModel.setWard(updateProjectModel.getWard());
                locationModel.setDistrict(updateProjectModel.getDistrict());
                locationModel.setCity(updateProjectModel.getCity());
                locationModel.setProvince(updateProjectModel.getProvince());
                locationModel.setCountry(updateProjectModel.getCountry());
                locationModel.setCoordinate(updateProjectModel.getCoordinate());
                locationModel.setUserId(updateProjectModel.getUserId());
                locationService.updateLocation(locationModel);

                project.setProjectName(updateProjectModel.getProjectName());
                project.setLocationId(updateProjectModel.getLocationId());


                project.setPlanStartDate(
                        LocalDateTime.parse(updateProjectModel.getPlanStartDate(),dateTimeFormatter));
                project.setPlanEndDate(
                        LocalDateTime.parse(updateProjectModel.getPlanEndDate(),dateTimeFormatter));
                project.setActualStartDate(
                        LocalDateTime.parse(updateProjectModel.getActualStartDate(),dateTimeFormatter));
                project.setActualEndDate(
                        LocalDateTime.parse(updateProjectModel.getActualEndDate(),dateTimeFormatter));

                project.setActualCost(updateProjectModel.getActualCost());
                project.setEstimatedCost(updateProjectModel.getEstimateCost());

                project.setUpdatedAt(LocalDateTime.now());

                project.setUpdatedBy(updateProjectModel.getUserId());
                projectRepository.saveAndFlush(project);
                return true;
            }
            return false;
    }

    @Override
    public Project updateProject(Project updatedProject) throws Exception {
        Project oldProject = getById(updatedProject.getProjectId());

        if (oldProject == null) {
            return null;
        }

        String errorMsg = "";

        /* Check FK (if changed) */
        if (!userService.existsById(updatedProject.getUpdatedBy())) {
            errorMsg += "No User (UpdatedBy) found with Id: '" + updatedProject.getUpdatedBy()
                    + "'. Which violate constraint: FK_Project_User_UpdatedBy. ";
        }
        if (updatedProject.getLocationId() != null) {
            if (!oldProject.getLocationId().equals(updatedProject.getLocationId())) {
                if (!locationService.existsById(updatedProject.getLocationId())) {
                    /* Should not happen, location are already validated beforehand */
                    errorMsg += "No Location found with Id: '" + updatedProject.getLocationId()
                            + "'. Which violate constraint: FK_Project_Location. ";
                }
            }
        } else {
            oldProject.setLocationId(oldProject.getLocationId());
        }

        /* Check duplicate */
        if (projectRepository
                .existsByProjectNameAndProjectIdIsNotAndIsDeletedIsFalse(
                        updatedProject.getProjectName(),
                        updatedProject.getProjectId())) {
            errorMsg += "Already exists another Project with name: '" + updatedProject.getProjectName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return projectRepository.saveAndFlush(updatedProject);
    }
    @Override
    public ProjectReadDTO updateProjectByDTO(ProjectUpdateDTO updatedProjectDTO) throws Exception {
        modelMapper.typeMap(ProjectUpdateDTO.class, Project.class)
                .addMappings(mapper -> {
                    mapper.skip(Project::setPlanStartDate);
                    mapper.skip(Project::setPlanEndDate);
                    mapper.skip(Project::setActualStartDate);
                    mapper.skip(Project::setActualEndDate);});

        Project updatedProject = modelMapper.map(updatedProjectDTO, Project.class);

        if (updatedProjectDTO.getPlanStartDate() != null) {
            updatedProject.setPlanStartDate(
                    LocalDateTime.parse(updatedProjectDTO.getPlanStartDate(), dateTimeFormatter));
        }

        if (updatedProjectDTO.getPlanEndDate() != null) {
            updatedProject.setPlanEndDate(
                    LocalDateTime.parse(updatedProjectDTO.getPlanEndDate(), dateTimeFormatter));
        }

        if (updatedProjectDTO.getActualStartDate() != null) {
            updatedProject.setActualStartDate(
                    LocalDateTime.parse(updatedProjectDTO.getActualStartDate(), dateTimeFormatter));
        }

        if (updatedProjectDTO.getActualEndDate() != null) {
            updatedProject.setActualEndDate(
                    LocalDateTime.parse(updatedProjectDTO.getActualEndDate(), dateTimeFormatter));
        }

        /* Update Location if changed / Get associated Location  */
        LocationReadDTO locationDTO;
        if (updatedProjectDTO.getLocation() != null) {
            locationDTO = locationService.updateLocationByDTO(updatedProjectDTO.getLocation());

            if (locationDTO == null) {
                /* Not found location with Id, NEED TO STOP */
                throw new IllegalArgumentException("Invalid locationId, No location found with Id to update");
            }

            updatedProject.setLocationId(locationDTO.getLocationId());

            updatedProject = updateProject(updatedProject);
        } else {
            updatedProject = updateProject(updatedProject);

            locationDTO = locationService.getDTOById(updatedProject.getLocationId());
        }

        if (updatedProject == null) {
            return null;
            /* Not found with Id */
        }

        ProjectReadDTO projectDTO = modelMapper.map(updatedProject, ProjectReadDTO.class);

        /* Set associated Location */
        projectDTO.setLocation(locationDTO);

        /* TODO: Get associated Blueprint */
//        projectDTO.setBlueprint(blueprintService.getDTOByProjectId(updatedProject.getProjectId()));

        projectDTO.setTaskList(taskService.getAllDTOByProjectId(updatedProject.getProjectId()));

        projectDTO.setReportList(reportService.getAllDTOByProjectId(updatedProject.getProjectId()));

        /* TODO: Get associated Request */
//        projectDTO.setRequestList(requestService.getDTOByProjectId(updatedProject.getProjectId()));

        return projectDTO;
    }

    /* DELETE */
    @Override
    public boolean deleteProject(long projectId) {
        Project project = getById(projectId);

        if (project == null) {
            return false;
            /* Not found with Id */
        }

        project.setIsDeleted(true);
        projectRepository.saveAndFlush(project);

        return true;
    }

    @Override
    public List<ListUserIDAndName> getUserForDropdownSelection() {
        List<User> listUser = userRepository.findByIsDeletedFalse();
        List<ListUserIDAndName> list = new ArrayList<>();
        ListUserIDAndName model;
        for (User user : listUser) {
            model = new ListUserIDAndName();
            model.setUserId(user.getUserId());
            model.setUserName(user.getUsername());
            list.add(model);
        }
        return list;
    }
}
