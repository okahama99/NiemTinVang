package com.ntv.ntvcons_backend.services.project;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.UpdateBlueprintModel;
import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.LocationModels.CreateLocationModel;
import com.ntv.ntvcons_backend.entities.LocationModels.UpdateLocationModel;
import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
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
import java.time.Instant;
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
    public String createProject(String projectName, CreateLocationModel createLocationModel,
                                CreateBlueprintModel createBluePrintModel, Instant planStartDate, Instant planEndDate,
                                Instant actualStartDate, Instant actualEndDate, double estimateCost, double actualCost) {
        String result;
        Location checkDuplicateLocation = locationRepository.getByAddressNumber(createLocationModel.getAddressNumber());
        if(checkDuplicateLocation == null)
        {
            Blueprint checkDuplicateBlueprint = blueprintRepository.getByBlueprintName(createBluePrintModel.getProjectBlueprintName());
            if(checkDuplicateBlueprint == null)
            {
                locationService.createLocation(createLocationModel);
                blueprintService.createProjectBlueprint(createBluePrintModel);
                Location location = locationRepository.getByAddressNumber(createLocationModel.getAddressNumber());
                Blueprint blueprint = blueprintRepository.getByBlueprintName(createBluePrintModel.getProjectBlueprintName());
                Project project = new Project();
                project.setProjectName(projectName);
                project.setLocationId(location.getLocationId());
                project.setPlanStartDate(planStartDate);
                project.setPlanEndDate(planEndDate);
                project.setActualStartDate(actualStartDate);
                project.setActualEndDate(actualEndDate);
                project.setActualCost(actualCost);
                project.setEstimatedCost(estimateCost);
                projectRepository.saveAndFlush(project);
                result = "Create success";
                return result;
            }else{
                result = "Existed blueprint name";
                return result;
            }
        }else{
            result = "Existed address number";
            return result;
        }
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
    public Project getById(long projectId) {
        return projectRepository
                .findByProjectIdAndIsDeletedIsFalse(projectId)
                .orElse(null);
    }

    @Override
    public Project getByLocationId(long locationId) {
        return null;
    }

    @Override
    public Project getByBlueprintId(long blueprintId) {
        return null;
    }

    @Override
    public List<Project> getAllByIdIn(Collection<Long> projectIdCollection) {
        return null;
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
    public String updateProject(ProjectModel projectModel) {
            String result;
            Project project = projectRepository.findById(projectModel.getProjectId()).get();
            if(project!=null)
            {
                Location checkDuplicateLocation = locationRepository.getByAddressNumber(projectModel.getAddressNumber());
                if(checkDuplicateLocation == null)
                {

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
                        result = "Update success";
                        return result;

                }else{
                    result = "Existed address number";
                    return result;
                }
            }
            result = "ProjectID not existed";
            return result;
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
}
