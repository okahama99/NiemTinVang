package com.ntv.ntvcons_backend.services.blueprint;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBluePrintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.ShowBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.UpdateBlueprintModel;
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
    public void createProjectBlueprint(CreateBluePrintModel createBluePrintModel) {
        Blueprint blueprint = new Blueprint();
        blueprint.setBlueprintName(createBluePrintModel.getProjectBlueprintName());
        blueprint.setDesignerName(createBluePrintModel.getDesignerName());
        blueprint.setEstimatedCost(createBluePrintModel.getEstimateCost());
        blueprintRepository.saveAndFlush(blueprint);
    }

    /* READ */
    @Override
    public List<ShowBlueprintModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Blueprint> pagingResult = blueprintRepository.findAllByIsDeletedIsFalse(paging);

        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / pageSize);

            Page<ShowBlueprintModel> modelResult =
                    pagingResult.map(new Converter<Blueprint, ShowBlueprintModel>() {

                        @Override
                        protected ShowBlueprintModel doForward(Blueprint projectBlueprint) {
                            ShowBlueprintModel model = new ShowBlueprintModel();

                            model.setProjectBlueprintId(projectBlueprint.getBlueprintId());
                            model.setProjectBlueprintName(projectBlueprint.getBlueprintName());
                            model.setProjectBlueprintCost(projectBlueprint.getEstimatedCost());
                            model.setDesignerName(projectBlueprint.getDesignerName());
                            model.setCreatedAt(projectBlueprint.getCreatedAt());
                            model.setCreatedBy(projectBlueprint.getCreatedBy());
                            model.setUpdatedAt(projectBlueprint.getCreatedAt());
                            model.setUpdatedBy(projectBlueprint.getUpdatedBy());
                            model.setTotalPage(totalPage);

                            return model;
                        }

                        @Override
                        protected Blueprint doBackward(ShowBlueprintModel showProjectBlueprintModel) {
                            /* TODO: Fill or explain why null */
                            return null;
                        }
                    });

            return modelResult.getContent();

        } else {
            return new ArrayList<ShowBlueprintModel>();
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
    public void updateProjectBlueprint(UpdateBlueprintModel updateBlueprintModel) {
        Blueprint blueprint = blueprintRepository.findById(updateBlueprintModel.getBlueprintId()).get();
        blueprint.setBlueprintId(updateBlueprintModel.getBlueprintId());
        blueprint.setBlueprintName(updateBlueprintModel.getBlueprintName());
        blueprint.setDesignerName(updateBlueprintModel.getDesignerName());
        blueprint.setEstimatedCost(updateBlueprintModel.getEstimateCost());
        blueprint.setUpdatedBy(updateBlueprintModel.getUserId());
        blueprint.setUpdatedAt(updateBlueprintModel.getUpdatedAt());
        blueprintRepository.saveAndFlush(blueprint);
    }

    /* DELETE */
    @Override
    public boolean deleteProjectBlueprint(long projectBlueprintId) {
        Blueprint blueprint = blueprintRepository.findById(projectBlueprintId).get();
        if(blueprint!=null)
        {
            blueprint.setIsDeleted(true);
            blueprintRepository.saveAndFlush(blueprint);
        }
        return false;
    }
}
