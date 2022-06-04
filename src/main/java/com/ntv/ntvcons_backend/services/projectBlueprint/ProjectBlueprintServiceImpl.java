package com.ntv.ntvcons_backend.services.projectBlueprint;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.ProjectBlueprint;
import com.ntv.ntvcons_backend.entities.ProjectBlueprintModels.ShowProjectBlueprintModel;
import com.ntv.ntvcons_backend.entities.projectModels.ShowProjectModel;
import com.ntv.ntvcons_backend.repositories.PagingRepositories.ProjectBlueprintPagingRepository;
import com.ntv.ntvcons_backend.repositories.ProjectBlueprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ProjectBlueprintServiceImpl implements ProjectBlueprintService {
    @Autowired
    private ProjectBlueprintRepository projectBlueprintRepository;

    @Autowired
    private ProjectBlueprintPagingRepository projectBlueprintPagingRepository;

    @Override
    public ProjectBlueprint createProjectBlueprint(String projectBlueprintName, int designerId, double projectBlueprintCost) {
        return null;
    }

    @Override
    public List<ShowProjectBlueprintModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType)
        {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
        Page<ProjectBlueprint> pagingResult = projectBlueprintPagingRepository.findAll(paging);
        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);
            Page<ShowProjectBlueprintModel> modelResult = pagingResult.map(new Converter<ProjectBlueprint, ShowProjectBlueprintModel>() {
                @Override
                protected ShowProjectBlueprintModel doForward(ProjectBlueprint projectBlueprint) {
                    ShowProjectBlueprintModel model = new ShowProjectBlueprintModel();
                    model.setProjectBlueprintId(projectBlueprint.getProjectBlueprintId());
                    model.setProjectBlueprintName(projectBlueprint.getProjectBlueprintName());
                    model.setProjectBlueprintCost(projectBlueprint.getProjectBlueprintCost());
                    model.setDesignerId(projectBlueprint.getDesignerId());
                    model.setCreatedAt(projectBlueprint.getCreatedAt());
                    model.setCreatedBy(projectBlueprint.getCreatedBy());
                    model.setUpdatedAt(projectBlueprint.getCreatedAt());
                    model.setUpdatedBy(projectBlueprint.getUpdatedBy());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected ProjectBlueprint doBackward(ShowProjectBlueprintModel showProjectBlueprintModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ShowProjectBlueprintModel>();
        }
    }

    @Override
    public List<ProjectBlueprint> getAllByIdIn(Collection<Integer> projectBlueprintIdCollection) {
        return null;
    }

    @Override
    public List<ProjectBlueprint> getAllByProjectBlueprintNameLike(String projectBlueprintName) {
        return null;
    }

    @Override
    public List<ProjectBlueprint> getAllByProjectBlueprintCostBetween(double from, double to) {
        return null;
    }

    @Override
    public ProjectBlueprint getByDesignerId(int designerId) {
        return null;
    }

    @Override
    public ProjectBlueprint getById(int projectBlueprintId) {
        return null;
    }

    @Override
    public boolean updateProjectBlueprint(ShowProjectBlueprintModel showProjectBlueprintModel) {
        return true;
    }

    @Override
    public boolean deleteProjectBlueprint(int projectBlueprintId) {
        return false;
    }
}
