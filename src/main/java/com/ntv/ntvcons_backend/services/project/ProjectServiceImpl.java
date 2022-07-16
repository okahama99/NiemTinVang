package com.ntv.ntvcons_backend.services.project;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationUpdateDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectCreateDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectReadDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectUpdateDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerCreateDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerUpdateDTO;
import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerCreateDTO;
import com.ntv.ntvcons_backend.dtos.report.ReportReadDTO;
import com.ntv.ntvcons_backend.dtos.request.RequestReadDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;
import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.ProjectManager;
import com.ntv.ntvcons_backend.entities.ProjectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.UpdateProjectModel;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.entities.UserModels.ListUserIDAndName;
import com.ntv.ntvcons_backend.repositories.LocationRepository;
import com.ntv.ntvcons_backend.repositories.ProjectRepository;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintService;
import com.ntv.ntvcons_backend.services.location.LocationService;
import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
import com.ntv.ntvcons_backend.services.projectWorker.ProjectWorkerService;
import com.ntv.ntvcons_backend.services.report.ReportService;
import com.ntv.ntvcons_backend.services.request.RequestService;
import com.ntv.ntvcons_backend.services.task.TaskService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectManagerService projectManagerService;
    @Autowired
    private ProjectWorkerService projectWorkerService;
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
//        projectManagerDTO.setAssignDate(LocalDateTime.now().format(dateTimeFormatter));
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

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return projectRepository.saveAndFlush(newProject);
    }
    @Override
    public ProjectReadDTO createProjectByDTO(ProjectCreateDTO newProjectDTO) throws Exception {
        modelMapper.typeMap(ProjectCreateDTO.class, Project.class)
                .addMappings(mapper -> {
                    mapper.skip(Project::setPlanStartDate);
                    mapper.skip(Project::setPlanEndDate);});

        Project newProject = modelMapper.map(newProjectDTO, Project.class);

        /* Already check NOT NULL */
        newProject.setPlanStartDate(
                LocalDateTime.parse(newProjectDTO.getPlanStartDate(), dateTimeFormatter));

        if (newProjectDTO.getPlanEndDate() != null) {
            newProject.setPlanEndDate(
                    LocalDateTime.parse(newProjectDTO.getPlanEndDate(), dateTimeFormatter));

            if (newProject.getPlanEndDate().isBefore(newProject.getPlanEndDate())) {
                throw new IllegalArgumentException("planEndDate is before planStartDate");
            }
        }

        /* Associated Location */
        LocationReadDTO locationDTO;
        /* TODO: reuse later */
        /* switch (newProjectDTO.getLocation().getCreateOption()) {
            case CREATE_NEW_LOCATION:
                if (newProjectDTO.getLocation().getNewLocation() == null) {
                    throw new IllegalArgumentException("Missing REQUIRED newLocation");
                }

                /* Create Location first (to get locationId) * /
                locationDTO = locationService.createLocationByDTO(newProjectDTO.getLocation().getNewLocation());
                break;

            case SELECT_EXISTING_LOCATION:
                if (newProjectDTO.getLocation().getExistingLocationId() == null) {
                    throw new IllegalArgumentException("Missing REQUIRED existingLocationId");
                }

                /* Get associated Location * /
                locationDTO = locationService.getDTOById(newProjectDTO.getLocation().getExistingLocationId());

                if (locationDTO == null) {
                    /* Not found location with Id, NEED TO STOP * /
                    throw new IllegalArgumentException("No location found with Id: '"
                            + newProjectDTO.getLocation().getExistingLocationId() + "'. ");
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid createOption used");
        }*/

        /* Create Location first (to get locationId) */
        locationDTO = locationService.createLocationByDTO(newProjectDTO.getLocation());
        /* Set locationId because createProject() check FK */
        newProject.setLocationId(locationDTO.getLocationId());

        newProject = createProject(newProject);

        long newProjectId = newProject.getProjectId();

        /* Set REQUIRED FK projectId to blueprint after create Project */
        newProjectDTO.getBlueprint().setProjectId(newProjectId);
        /* Create associated Blueprint */
        blueprintService.createBlueprintByDTO(newProjectDTO.getBlueprint());

        /* Create associated ProjectManager (if present) */
        List<ProjectManagerCreateDTO> projectManagerDTOList = newProjectDTO.getProjectManagerList();
        if (projectManagerDTOList != null) {
            projectManagerDTOList =
                    projectManagerDTOList.stream()
                            .peek(projectManagerDTO -> projectManagerDTO.setProjectId(newProjectId))
                            .collect(Collectors.toList());

            /* Create associated ProjectManager; Set required FK projectId */
            projectManagerService.createBulkProjectManagerByDTO(projectManagerDTOList);
        }

        /* Create associated ProjectWorker (if present) */
        List<ProjectWorkerCreateDTO> projectWorkerDTOList = newProjectDTO.getProjectWorkerList();
        if (projectWorkerDTOList != null) {
            projectWorkerDTOList =
                    projectWorkerDTOList.stream()
                            .peek(projectWorkerDTO -> projectWorkerDTO.setProjectId(newProjectId))
                            .collect(Collectors.toList());

            /* Create associated ProjectWorker; Set required FK projectId */
            projectWorkerService.createBulkProjectWorkerByDTO(projectWorkerDTOList);
        }

        return fillDTO(newProject);
    }

    /* READ */
    @Override
    public List<ProjectModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Project> pagingResult = projectRepository.findAllByIsDeletedIsFalse(paging);

        if (pagingResult.hasContent()){
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
    public Page<Project> getPageAll(Pageable paging) throws Exception {
        Page<Project> projectPage = projectRepository.findAllByIsDeletedIsFalse(paging);

        if (projectPage.isEmpty()) 
            return null;

        return projectPage;
    }
    @Override
    public List<ProjectReadDTO> getAllInPaging(Pageable paging) throws Exception {
        Page<Project> projectPage = getPageAll(paging);

        if (projectPage == null) 
            return null;

        List<Project> projectList = projectPage.getContent();

        if (projectList.isEmpty()) 
            return null;

        return fillAllDTO(projectList, projectPage.getTotalPages());
    }

    @Override
    public boolean existsById(long projectId) throws Exception {
        return projectRepository.existsByProjectIdAndIsDeletedIsFalse(projectId);
    }
    @Override
    public Project getById(long projectId) throws Exception {
        return projectRepository
                .findByProjectIdAndIsDeletedIsFalse(projectId)
                .orElse(null);
    }
    @Override
    public ProjectReadDTO getDTOById(long projectId) throws Exception {
        Project project = getById(projectId);

        if (project == null) 
            return null;

        return fillDTO(project);
    }

    /* get all gì? theo id thì là 1. Các entity con/phụ thuộc là chuyện khác */
    @Override
    public List<ProjectModel> getAllById(long projectId, int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Project> pagingResult = projectRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId, paging);
        if (pagingResult.hasContent()){
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
    public boolean existsAllByIdIn(Collection<Long> projectIdCollection) throws Exception {
        return projectRepository.existsAllByProjectIdInAndIsDeletedIsFalse(projectIdCollection);
    }
    @Override
    public List<Project> getAllByIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Project> projectList =
                projectRepository.findAllByProjectIdInAndIsDeletedIsFalse(projectIdCollection);

        if (projectList.isEmpty()) 
            return null;

        return projectList;
    }
    @Override
    public List<ProjectReadDTO> getAllDTOByIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Project> projectList = getAllByIdIn(projectIdCollection);

        if (projectList.isEmpty()) 
            return null;

        return fillAllDTO(projectList, null);
    }

    @Override
    public List<Project> getAllByLocationId(long locationId) throws Exception {
        List<Project> projectList =
                projectRepository.findAllByLocationIdAndIsDeletedIsFalse(locationId);

        if (projectList.isEmpty()) 
            return null;

        return projectList;
    }
    @Override
    public List<ProjectReadDTO> getAllDTOByLocationId(long locationId) throws Exception {
        List<Project> projectList = getAllByLocationId(locationId);

        if (projectList.isEmpty()) 
            return null;

        return fillAllDTO(projectList, null);
    }
    @Override
    public Page<Project> getPageAllByLocationId(Pageable paging, long locationId) throws Exception {
        Page<Project> projectPage = projectRepository.findAllByLocationIdAndIsDeletedIsFalse(locationId, paging);

        if (projectPage.isEmpty()) 
            return null;

        return projectPage;
    }
    @Override
    public List<ProjectReadDTO> getAllDTOInPagingByLocationId(Pageable paging, long locationId) throws Exception {
        Page<Project> projectPage = getPageAllByLocationId(paging, locationId);

        if (projectPage == null) 
            return null;

        List<Project> projectList = projectPage.getContent();

        if (projectList.isEmpty()) 
            return null;

        return fillAllDTO(projectList, projectPage.getTotalPages());
    }

    @Override
    public List<Project> getAllByLocationIdIn(Collection<Long> locationIdCollection) throws Exception {
        List<Project> projectList =
                projectRepository.findAllByLocationIdInAndIsDeletedIsFalse(locationIdCollection);

        if (projectList.isEmpty()) 
            return null;

        return projectList;
    }
    @Override
    public List<ProjectReadDTO> getAllDTOByLocationIdIn(Collection<Long> locationIdCollection) throws Exception {
        List<Project> projectList = getAllByLocationIdIn(locationIdCollection);

        if (projectList.isEmpty()) 
            return null;

        return fillAllDTO(projectList, null);
    }
    @Override
    public Page<Project> getPageAllByLocationIdIn(Pageable paging, Collection<Long> locationIdCollection) throws Exception {
        Page<Project> projectPage =
                projectRepository.findAllByLocationIdInAndIsDeletedIsFalse(locationIdCollection, paging);

        if (projectPage.isEmpty()) 
            return null;

        return projectPage;
    }
    @Override
    public List<ProjectReadDTO> getAllDTOInPagingByLocationIdIn(Pageable paging, Collection<Long> locationIdCollection) throws Exception {
        Page<Project> projectPage = getPageAllByLocationIdIn(paging, locationIdCollection);

        if (projectPage == null) 
            return null;

        List<Project> projectList = projectPage.getContent();

        if (projectList.isEmpty()) 
            return null;

        return fillAllDTO(projectList, projectPage.getTotalPages());
    }

    @Override
    public boolean checkDuplicate(String projectName) {
        Project checkDuplicateProject = projectRepository.getByProjectNameAndIsDeletedIsFalse(projectName);
        if (checkDuplicateProject != null) {
            return true;
        }
        return false;
    }

    @Override
    public Project getByProjectName(String projectName) throws Exception {
        return projectRepository
                .findByProjectNameAndIsDeletedIsFalse(projectName)
                .orElse(null);
    }
   @Override
    public ProjectReadDTO getDTOByProjectName(String projectName) throws Exception {
        Project project = getByProjectName(projectName);

        if (project == null) 
            return null;

        return fillDTO(project);
    }

    @Override
    public List<Project> getAllByProjectNameContains(String projectName) throws Exception {
        List<Project> projectList =
                projectRepository.findAllByProjectNameContainsAndIsDeletedIsFalse(projectName);

        if (projectList.isEmpty()) 
            return null;

        return projectList;
    }
    @Override
    public List<ProjectReadDTO> getAllDTOByProjectNameContains(String projectName) throws Exception {
        List<Project> projectList = getAllByProjectNameContains(projectName);

        if (projectList.isEmpty()) 
            return null;

        return fillAllDTO(projectList, null);
    }
    @Override
    public Page<Project> getPageAllByProjectNameContains(Pageable paging, String projectName) throws Exception {
        Page<Project> projectPage =
                projectRepository.findAllByProjectNameContainsAndIsDeletedIsFalse(projectName, paging);

        if (projectPage.isEmpty()) 
            return null;

        return projectPage;
    }
    @Override
    public List<ProjectReadDTO> getAllDTOInPagingByProjectNameContains(Pageable paging, String projectName) throws Exception {
        Page<Project> projectPage = getPageAllByProjectNameContains(paging, projectName);

        if (projectPage == null) 
            return null;

        List<Project> projectList = projectPage.getContent();

        if (projectList.isEmpty()) 
            return null;

        return fillAllDTO(projectList, projectPage.getTotalPages());
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

    /* UPDATE */
    @Override
    public boolean updateProject(UpdateProjectModel updateProjectModel) {
            Project project = projectRepository.findById(updateProjectModel.getProjectId()).orElse(null);
            /* optional.get() mà null -> ra Exception, khỏi check null luôn  */
            if (project != null) {
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

        if (oldProject == null) 
            return null;

        String errorMsg = "";

        /* Check FK (if changed) */
        if (oldProject.getUpdatedBy() != null) {
            if (!oldProject.getUpdatedBy().equals(updatedProject.getUpdatedBy())) {
                if (!userService.existsById(updatedProject.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedProject.getUpdatedBy()
                            + "'. Which violate constraint: FK_Project_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedProject.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedProject.getUpdatedBy()
                        + "'. Which violate constraint: FK_Project_User_UpdatedBy. ";
            }
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
            updatedProject.setLocationId(oldProject.getLocationId());
        }

        /* Check duplicate */
        if (projectRepository
                .existsByProjectNameAndProjectIdIsNotAndIsDeletedIsFalse(
                        updatedProject.getProjectName(),
                        updatedProject.getProjectId())) {
            errorMsg += "Already exists another Project with name: '" + updatedProject.getProjectName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedProject.setCreatedBy(oldProject.getCreatedBy());
        updatedProject.setCreatedAt(oldProject.getCreatedAt());

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

        /* Already check NOT NULL */
        updatedProject.setPlanStartDate(
                LocalDateTime.parse(updatedProjectDTO.getPlanStartDate(), dateTimeFormatter));

        if (updatedProjectDTO.getPlanEndDate() != null) {
            updatedProject.setPlanEndDate(
                    LocalDateTime.parse(updatedProjectDTO.getPlanEndDate(), dateTimeFormatter));

            if (updatedProject.getPlanEndDate().isBefore(updatedProject.getPlanStartDate())) {
                throw new IllegalArgumentException("planEndDate is before planStartDate");
            }
        }

        boolean hasActualStartDate = false;
        if (updatedProjectDTO.getActualStartDate() != null) {
            hasActualStartDate = true;

            updatedProject.setActualStartDate(
                    LocalDateTime.parse(updatedProjectDTO.getActualStartDate(), dateTimeFormatter));
        }

        if (updatedProjectDTO.getActualEndDate() != null) {
            updatedProject.setActualEndDate(
                    LocalDateTime.parse(updatedProjectDTO.getActualEndDate(), dateTimeFormatter));
            
            if (hasActualStartDate)
                if (updatedProject.getActualEndDate().isBefore(updatedProject.getActualStartDate()))
                    throw new IllegalArgumentException("actualEndDate is before actualStartDate");
        }

        /* TODO: reuse later */
        /*
        /* Update Location if changed / Get associated Location  * /
        if (updatedProjectDTO.getLocation() != null) {
            switch (updatedProjectDTO.getLocation().getUpdateOption()) {
                case CREATE_NEW_LOCATION:
                    if (updatedProjectDTO.getLocation().getNewLocation() == null) {
                        throw new IllegalArgumentException("Missing REQUIRED newLocation");
                    }

                    /* Create Location first (to get locationId) * /
                    locationDTO = locationService.createLocationByDTO(updatedProjectDTO.getLocation().getNewLocation());
                    break;

                case SELECT_NEW_EXISTING_LOCATION:
                    if (updatedProjectDTO.getLocation().getExistingLocationId() == null) {
                        throw new IllegalArgumentException("Missing REQUIRED existingLocationId");
                    }

                    /* Get associated Location * /
                    locationDTO = locationService.getDTOById(updatedProjectDTO.getLocation().getExistingLocationId());

                    if (locationDTO == null) {
                        /* Not found location with Id, NEED TO STOP * /
                        throw new IllegalArgumentException("No location found with Id: '"
                                + updatedProjectDTO.getLocation().getExistingLocationId() + "'. ");
                    }
                    break;

                case UPDATE_EXISTING_LOCATION_USED:
                    if (updatedProjectDTO.getLocation().getExistingLocationId() == null) {
                        throw new IllegalArgumentException("Missing REQUIRED existingLocationId");
                    }

                    /* Update associated Location * /
                    locationDTO =
                            locationService.updateLocationByDTO(updatedProjectDTO.getLocation().getUpdatedLocation());

                    if (locationDTO == null) {
                        /* Not found location with Id, NEED TO STOP * /
                        throw new IllegalArgumentException("Invalid locationId, No location found with Id to update");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid updateOption used");
            }

            /* Set locationId because updateProject() check FK * /
            updatedProject.setLocationId(locationDTO.getLocationId());

            updatedProject = updateProject(updatedProject);
        } else {
            updatedProject = updateProject(updatedProject);

            /* Get associated Location * /
            locationDTO = locationService.getDTOById(updatedProject.getLocationId());
        } */

        /* Update associated Location if changed */
        LocationUpdateDTO location = updatedProjectDTO.getLocation();
        if (location != null) {
            if (locationService.updateLocationByDTO(location) == null) {
                /* Not found location with Id, NEED TO STOP */
                throw new IllegalArgumentException("No location found with Id: '"
                        + location.getLocationId() + "' to update");
            }
        }

        updatedProject = updateProject(updatedProject);

        if (updatedProject == null)
            return null; /* Not found with Id */

        long updatedProjectId = updatedProject.getProjectId();

        /* Update associated Blueprint if changed */
        if (updatedProjectDTO.getBlueprint() != null) {
            /* Just in case */
            updatedProjectDTO.getBlueprint().setProjectId(updatedProjectId);

            blueprintService.updateBlueprintByDTO(updatedProjectDTO.getBlueprint());
        }

        /* Update associated ProjectManager if changed */
        if (updatedProjectDTO.getProjectManagerList() != null) {
            /* Just in case */
            updatedProjectDTO.setProjectManagerList(
                    updatedProjectDTO.getProjectManagerList().stream()
                            .peek(projectManagerDTO -> projectManagerDTO.setProjectId(updatedProjectId))
                            .collect(Collectors.toList()));

            /* TODO: reuse when login done
            modelMapper.typeMap(ProjectManagerUpdateDTO.class, ProjectManagerCreateDTO.class)
                    .addMappings(mapper -> {
                        mapper.map(ProjectManagerUpdateDTO::getUpdatedBy, ProjectManagerCreateDTO::setCreatedBy);});*/

            List<ProjectManagerCreateDTO> newProjectManagerDTOList = new ArrayList<>();
            List<ProjectManagerUpdateDTO> updatedProjectManagerDTOList = new ArrayList<>();

            for (ProjectManagerUpdateDTO projectManagerDTO : updatedProjectDTO.getProjectManagerList()) {
                if (projectManagerDTO.getProjectManagerId() <= 0) {
                    newProjectManagerDTOList.add(modelMapper.map(projectManagerDTO, ProjectManagerCreateDTO.class));
                } else {
                    updatedProjectManagerDTOList.add(projectManagerDTO);
                }
            }

            /* Create associated ProjectManager */
            if (!newProjectManagerDTOList.isEmpty()) {
                projectManagerService.createBulkProjectManagerByDTO(newProjectManagerDTOList);
            }

            /* Update associated ProjectManager */
            if (!updatedProjectManagerDTOList.isEmpty()) {
                projectManagerService.updateBulkProjectManagerByDTO(updatedProjectManagerDTOList);
            }
        }

        /* TODO: Update associated ProjectWorker if changed */

        return fillDTO(updatedProject);
    }

    /* DELETE */
    @Override
    public boolean deleteProject(long projectId) throws Exception {
        Project project = getById(projectId);

        if (project == null) {
            return false;
            /* Not found with Id */
        }

        project.setIsDeleted(true);
        projectRepository.saveAndFlush(project);

        return true;
    }

    /* Utils */
    private ProjectReadDTO fillDTO(Project project) throws Exception {
        long projectId = project.getProjectId();

        ProjectReadDTO projectDTO = modelMapper.map(project, ProjectReadDTO.class);

        /* NOT NULL */
        projectDTO.setLocation(locationService.getDTOById(project.getLocationId()));
        projectDTO.setBlueprint(blueprintService.getDTOByProjectId(projectId));

        /* Nullable */
        projectDTO.setTaskList(taskService.getAllDTOByProjectId(projectId));
        projectDTO.setReportList(reportService.getAllDTOByProjectId(projectId));
        projectDTO.setRequestList(requestService.getAllDTOByProjectId(projectId));
        projectDTO.setProjectManagerList(projectManagerService.getAllDTOByProjectId(projectId));
        /* TODO: ProjectWorkerList */

        return projectDTO;
    }

    private List<ProjectReadDTO> fillAllDTO(List<Project> projectList, Integer totalPage) throws Exception {
        Set<Long> locationIdSet = new HashSet<>();
        Set<Long> projectIdSet = new HashSet<>();

        for (Project project : projectList) {
            locationIdSet.add(project.getLocationId());
            projectIdSet.add(project.getProjectId());
        }

        /* Get associated Location */
        Map<Long, LocationReadDTO> locationIdLocationDTOMap =
                locationService.mapLocationIdLocationDTOByIdIn(locationIdSet);
        /* Get associated Blueprint */
        Map<Long, BlueprintReadDTO> projectIdBlueprintDTOMap =
                blueprintService.mapProjectIdBlueprintDTOByProjectIdIn(projectIdSet);

        /* Get associated Task */
        Map<Long, List<TaskReadDTO>> projectIdTaskDTOListMap =
                taskService.mapProjectIdTaskDTOListByProjectIdIn(projectIdSet);
        /* Get associated Report */
        Map<Long, List<ReportReadDTO>> projectIdReportDTOListMap =
                reportService.mapProjectIdReportDTOListByProjectIdIn(projectIdSet);
        /* Get associated Request */
        Map<Long, List<RequestReadDTO>> projectIdRequestDTOListMap =
                requestService.mapProjectIdRequestDTOListByProjectIdIn(projectIdSet);
        /* Get associated ProjectManager */
        Map<Long, List<ProjectManagerReadDTO>> projectIdProjectManagerDTOListMap =
                projectManagerService.mapProjectIdProjectManagerDTOListByProjectIdIn(projectIdSet);
        /* Get associated ProjectWorker */
        /* TODO: ProjectWorkerMap
        Map<Long, List<ProjectWorkerReadDTO>> projectIdProjectWorkerDTOListMap =
                projectWorkerService.mapProjectIdProjectWorkerDTOListByProjectIdIn(projectIdSet);*/

        return projectList.stream()
                .map(project -> {
                    ProjectReadDTO projectDTO =
                            modelMapper.map(project, ProjectReadDTO.class);

                    long tmpProjectId = project.getProjectId();

                    /* NOT NULL */
                    projectDTO.setLocation(locationIdLocationDTOMap.get(project.getLocationId()));
                    projectDTO.setBlueprint(projectIdBlueprintDTOMap.get(tmpProjectId));

                    /* Nullable */
                    projectDTO.setTaskList(projectIdTaskDTOListMap.get(tmpProjectId));
                    projectDTO.setReportList(projectIdReportDTOListMap.get(tmpProjectId));
                    projectDTO.setRequestList(projectIdRequestDTOListMap.get(tmpProjectId));
                    projectDTO.setProjectManagerList(projectIdProjectManagerDTOListMap.get(tmpProjectId));
                    /* TODO: ProjectWorkerList */

                    projectDTO.setTotalPage(totalPage);

                    return projectDTO;})
                .collect(Collectors.toList());
    }
}
