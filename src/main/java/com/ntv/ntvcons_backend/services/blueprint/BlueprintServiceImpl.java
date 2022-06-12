package com.ntv.ntvcons_backend.services.blueprint;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.ProjectBlueprintModels.ShowProjectBlueprintModel;
import com.ntv.ntvcons_backend.repositories.BlueprintRepository;
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
public class BlueprintServiceImpl implements BlueprintService {
    @Autowired
    private BlueprintRepository blueprintRepository;

    /* CREATE */
    @Override
    public Blueprint createProjectBlueprint(String projectBlueprintName, int designerId, double projectBlueprintCost) {
        return null;
    }

    /* READ */
    @Override
    public List<ShowProjectBlueprintModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Blueprint> pagingResult = blueprintRepository.findAllByIsDeletedIsFalse(paging);

        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / pageSize);

            Page<ShowProjectBlueprintModel> modelResult =
                    pagingResult.map(new Converter<Blueprint, ShowProjectBlueprintModel>() {

                        @Override
                        protected ShowProjectBlueprintModel doForward(Blueprint projectBlueprint) {
                            ShowProjectBlueprintModel model = new ShowProjectBlueprintModel();

                            model.setProjectBlueprintId(projectBlueprint.getBlueprintId());
                            model.setProjectBlueprintName(projectBlueprint.getBlueprintName());
                            model.setProjectBlueprintCost(projectBlueprint.getEstimatedCost());
                            model.setDesignerId(projectBlueprint.getDesignerId());
                            model.setCreatedAt(projectBlueprint.getCreatedAt());
                            model.setCreatedBy(projectBlueprint.getCreatedBy());
                            model.setUpdatedAt(projectBlueprint.getCreatedAt());
                            model.setUpdatedBy(projectBlueprint.getUpdatedBy());
                            model.setTotalPage(totalPage);

                            return model;
                        }

                        @Override
                        protected Blueprint doBackward(ShowProjectBlueprintModel showProjectBlueprintModel) {
                            /* TODO: Fill or explain why null */
                            return null;
                        }
                    });

            return modelResult.getContent();

        } else {
            return new ArrayList<ShowProjectBlueprintModel>();
        }
    }

    @Override
    public List<Blueprint> getAllByIdIn(Collection<Integer> projectBlueprintIdCollection) {
        return null;
    }

    @Override
    public List<Blueprint> getAllByProjectBlueprintNameContains(String projectBlueprintName) {
        return null;
    }

    @Override
    public List<Blueprint> getAllByProjectBlueprintCostBetween(double from, double to) {
        return null;
    }

    @Override
    public Blueprint getByDesignerId(int designerId) {
        return null;
    }

    @Override
    public Blueprint getById(int projectBlueprintId) {
        return null;
    }

    /* UPDATE */
    @Override
    public boolean updateProjectBlueprint(ShowProjectBlueprintModel showProjectBlueprintModel) {
        return true;
    }

    /* DELETE */
    @Override
    public boolean deleteProjectBlueprint(int projectBlueprintId) {
        return false;
    }
}
