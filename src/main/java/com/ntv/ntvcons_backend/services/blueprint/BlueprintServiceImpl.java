package com.ntv.ntvcons_backend.services.blueprint;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBlueprintModel;
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
import java.util.Date;
import java.util.List;

@Service
public class BlueprintServiceImpl implements BlueprintService {
    @Autowired
    private BlueprintRepository blueprintRepository;

    /* CREATE */
    @Override
    public void createProjectBlueprint(CreateBlueprintModel createBluePrintModel) {
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
                            model.setUpdatedAt(projectBlueprint.getUpdatedAt());
                            model.setUpdatedBy(projectBlueprint.getUpdatedBy());
                            model.setTotalPage(totalPage);

                            return model;
                        }

                        @Override
                        protected Blueprint doBackward(ShowBlueprintModel showProjectBlueprintModel) {
                            return null;
                        }
                    });

            return modelResult.getContent();

        } else {
            return new ArrayList<ShowBlueprintModel>();
        }
    }

    @Override
    public Blueprint getById(long projectBlueprintId) {
        return blueprintRepository
                .findByBlueprintIdAndIsDeletedIsFalse(projectBlueprintId)
                .orElse(null);
    }

    @Override
    public List<Blueprint> getAllByIdIn(Collection<Integer> projectBlueprintIdCollection) {
        return null;
    }

    @Override
    public List<Blueprint> getAllByBlueprintNameContains(String projectBlueprintName) {
        return null;
    }

    @Override
    public List<Blueprint> getAllByBlueprintCostBetween(double from, double to) {
        return null;
    }

    @Override
    public String checkDuplicate(String blueprintName)
    {
        String result = "No duplicate";
        Blueprint checkDuplicate = blueprintRepository.getByBlueprintNameAndIsDeletedIsFalse(blueprintName);
        if(checkDuplicate != null)
        {
            result = "Existed blueprint name";
            return result;
        }
        return result;
    }

    /* UPDATE */
    @Override
    public void updateBlueprint(UpdateBlueprintModel updateBlueprintModel) {
        Blueprint blueprint = blueprintRepository.findById(updateBlueprintModel.getBlueprintId()).get();
        blueprint.setBlueprintId(updateBlueprintModel.getBlueprintId());
        blueprint.setBlueprintName(updateBlueprintModel.getBlueprintName());
        blueprint.setDesignerName(updateBlueprintModel.getDesignerName());
        blueprint.setEstimatedCost(updateBlueprintModel.getEstimateCost());
        blueprint.setUpdatedBy(updateBlueprintModel.getUserId());
        Date date = new Date();
        blueprint.setUpdatedAt(date);
        blueprintRepository.saveAndFlush(blueprint);
    }

    /* DELETE */
    @Override
    public boolean deleteBlueprint(long blueprintId) {
        Blueprint blueprint = getById(blueprintId);

        if (blueprint == null) {
            return false;
            /* Not found with Id */
        }

        blueprint.setIsDeleted(true);
        blueprintRepository.saveAndFlush(blueprint);

        return true;
    }
}
