package com.ntv.ntvcons_backend.services.project;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerCreateDTO;
import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;
import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.ProjectManager;
import com.ntv.ntvcons_backend.entities.ProjectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.entities.UserModels.ListUserIDAndName;
import com.ntv.ntvcons_backend.repositories.BlueprintRepository;
import com.ntv.ntvcons_backend.repositories.LocationRepository;
import com.ntv.ntvcons_backend.repositories.ProjectRepository;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintService;
import com.ntv.ntvcons_backend.services.location.LocationService;
import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private BlueprintService blueprintService;

    @Autowired
    private BlueprintRepository blueprintRepository;

    @Autowired
    private ProjectManagerService projectManagerService;

    @Autowired
    private UserRepository userRepository;

    /* READ */
    @Override
    public boolean createProject(CreateProjectModel createProjectModel) throws Exception{
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

        /* TODO: Sửa xong blueprint thì mở */
//        CreateBlueprintModel blueprintModel = new CreateBlueprintModel();
//        blueprintModel.setDesignerName(createProjectModel.getDesignerName());
//        blueprintModel.setProjectBlueprintName(createProjectModel.getProjectBlueprintName());
//        blueprintModel.setEstimateCost(createProjectModel.getBlueprintEstimateCost());
//        blueprintService.createProjectBlueprint(blueprintModel);

        Location location = locationRepository.getByAddressNumberAndIsDeletedIsFalse(locationModel.getAddressNumber());

        Project project = new Project();
        project.setProjectName(createProjectModel.getProjectName());
        project.setLocationId(location.getLocationId());
        project.setPlanStartDate(createProjectModel.getPlanStartDate().atTime(LocalTime.now())); // convert from LocalDate to LocalDateTime
        project.setPlanEndDate(createProjectModel.getPlanEndDate().atTime(LocalTime.now()));
        project.setActualStartDate(createProjectModel.getActualStartDate().atTime(LocalTime.now()));
        project.setActualEndDate(createProjectModel.getActualEndDate().atTime(LocalTime.now()));
        project.setActualCost(createProjectModel.getProjectActualCost());
        project.setEstimatedCost(createProjectModel.getProjectEstimateCost());
        projectRepository.saveAndFlush(project);

        ProjectManagerCreateDTO projectManagerDTO = new ProjectManagerCreateDTO();
        projectManagerDTO.setProjectId(project.getProjectId());
        projectManagerDTO.setManagerId(createProjectModel.getUserId());
        projectManagerDTO.setAssignDate(LocalDateTime.now());
        projectManagerService.createProjectManager(modelMapper.map(projectManagerDTO, ProjectManager.class));
        return true;
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

                    /* Vì đỏi FK Blue print nên tạm bỏ qua*/

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
    public boolean updateProject(ProjectModel projectModel) {
            Project project = projectRepository.findById(projectModel.getProjectId()).orElse(null);
            /* optional.get() mà null -> ra Exception, khỏi check null luôn  */
            if(project != null) {
                UpdateLocationModel locationModel = new UpdateLocationModel();
                locationModel.setLocationId(projectModel.getLocationId());
                locationModel.setAddressNumber(projectModel.getAddressNumber());
                locationModel.setStreet(projectModel.getStreet());
                locationModel.setArea(projectModel.getArea());
                locationModel.setWard(projectModel.getWard());
                locationModel.setDistrict(projectModel.getDistrict());
                locationModel.setCity(projectModel.getCity());
                locationModel.setProvince(projectModel.getProvince());
                locationModel.setCountry(projectModel.getCountry());
                locationModel.setCoordinate(projectModel.getCoordinate());
                locationModel.setUserId(projectModel.getUserId());
                locationService.updateLocation(locationModel);

                project.setProjectName(projectModel.getProjectName());
                project.setLocationId(projectModel.getLocationId());
                project.setPlanStartDate(projectModel.getPlanStartDate());
                project.setPlanEndDate(projectModel.getPlanEndDate());
                project.setActualStartDate(projectModel.getActualStartDate());
                project.setActualEndDate(projectModel.getActualEndDate());
                project.setActualCost(projectModel.getActualCost());
                project.setEstimatedCost(projectModel.getProjectEstimateCost());
                project.setUpdatedAt(projectModel.getUpdatedAt());
                project.setUpdatedBy(projectModel.getUserId());
                projectRepository.saveAndFlush(project);
                return true;
            }
            return false;
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
        ListUserIDAndName model = new ListUserIDAndName();
        for (User user : listUser) {
            model.setUserId(user.getUserId());
            model.setUserName(user.getUsername());
            list.add(model);
        }
        return list;
    }
}
