package com.ntv.ntvcons_backend.services.project;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBluePrintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.UpdateBlueprintModel;
import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;
import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.projectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.repositories.BlueprintRepository;
import com.ntv.ntvcons_backend.repositories.LocationRepository;
import com.ntv.ntvcons_backend.repositories.ProjectRepository;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintService;
import com.ntv.ntvcons_backend.services.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private BlueprintService blueprintService;

    @Autowired
    private BlueprintRepository blueprintRepository;

    /* READ */
    @Override
    public boolean createProject(CreateProjectModel createProjectModel) {
                CreateLocationModel createLocationModel = new CreateLocationModel();
                createLocationModel.setAddressNumber(createProjectModel.getAddressNumber());
                createLocationModel.setStreet(createProjectModel.getStreet());
                createLocationModel.setArea(createProjectModel.getArea());
                createLocationModel.setWard(createProjectModel.getWard());
                createLocationModel.setDistrict(createProjectModel.getDistrict());
                createLocationModel.setCity(createProjectModel.getCity());
                createLocationModel.setProvince(createProjectModel.getProvince());
                createLocationModel.setCountry(createProjectModel.getCountry());
                createLocationModel.setCoordinate(createProjectModel.getCoordinate());
                locationService.createLocation(createLocationModel);

                CreateBluePrintModel createBluePrintModel = new CreateBluePrintModel();
                createBluePrintModel.setDesignerName(createProjectModel.getDesignerName());
                createBluePrintModel.setProjectBlueprintName(createProjectModel.getProjectBlueprintName());
                createBluePrintModel.setEstimateCost(createProjectModel.getBlueprintEstimateCost());
                blueprintService.createProjectBlueprint(createBluePrintModel);

                Location location = locationRepository.getByAddressNumber(createLocationModel.getAddressNumber());
                Blueprint blueprint = blueprintRepository.getByBlueprintName(createBluePrintModel.getProjectBlueprintName());
                Project project = new Project();
                project.setProjectName(createProjectModel.getProjectName());
                project.setBlueprintId(blueprint.getBlueprintId());
                project.setLocationId(location.getLocationId());
                project.setPlanStartDate(createProjectModel.getPlanStartDate());
                project.setPlanEndDate(createProjectModel.getPlanEndDate());
                project.setActualStartDate(createProjectModel.getActualStartDate());
                project.setActualEndDate(createProjectModel.getActualEndDate());
                project.setActualCost(createProjectModel.getProjectActualCost());
                project.setEstimatedCost(createProjectModel.getProjectEstimateCost());
                projectRepository.saveAndFlush(project);
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
                    model.setDelete(project.getIsDeleted());

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

                    Blueprint blueprint = blueprintRepository.findById(project.getBlueprintId()).get();
                    model.setBlueprintId(project.getBlueprintId());
                    model.setDesignerName(blueprint.getDesignerName());
                    model.setBlueprintName(blueprint.getBlueprintName());
                    model.setBlueprintEstimateCost(blueprint.getEstimatedCost());

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
    public List<Project> getAllByIdIn(Collection<Integer> projectIdCollection) {
        return null;
    }

    @Override
    public List<Project> getAllByNameContains(String projectName) {
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

    /* UPDATE */
    @Override
    public boolean updateProject(ProjectModel projectModel) {
            Project project = projectRepository.findById(projectModel.getProjectId()).get();
            if(project!=null) {
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
                locationModel.setUpdatedAt(projectModel.getUpdatedAt());
                locationService.updateLocation(locationModel);

                UpdateBlueprintModel blueprintModel = new UpdateBlueprintModel();
                blueprintModel.setBlueprintId(projectModel.getBlueprintId());
                blueprintModel.setBlueprintName(projectModel.getBlueprintName());
                blueprintModel.setDesignerName(projectModel.getDesignerName());
                blueprintModel.setEstimateCost(projectModel.getBlueprintEstimateCost());
                blueprintModel.setUserId(projectModel.getUserId());
                blueprintModel.setUpdatedAt(projectModel.getUpdatedAt());
                blueprintService.updateProjectBlueprint(blueprintModel);

                project.setProjectName(projectModel.getProjectName());
                project.setBlueprintId(projectModel.getBlueprintId());
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
        Project project = projectRepository.findById(projectId).get();
        if(project!=null){
            project.setIsDeleted(true);
            projectRepository.saveAndFlush(project);
            return true;
        }
        return false;
    }
}
