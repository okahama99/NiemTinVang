package com.ntv.ntvcons_backend.services.blueprint;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintCreateDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintReadDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintUpdateDTO;
import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.ShowBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.UpdateBlueprintModel;
import com.ntv.ntvcons_backend.repositories.BlueprintRepository;
import com.ntv.ntvcons_backend.services.entityWrapper.EntityWrapperService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BlueprintServiceImpl implements BlueprintService {
    @Autowired
    private BlueprintRepository blueprintRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ProjectService projectService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Autowired
    private EntityWrapperService entityWrapperService;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;

    private final EntityType ENTITY_TYPE = EntityType.BLUEPRINT_ENTITY;
    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public void createProjectBlueprint(CreateBlueprintModel createBluePrintModel) {
        Blueprint blueprint = new Blueprint();
        blueprint.setBlueprintName(createBluePrintModel.getProjectBlueprintName());
        blueprint.setDesignerName(createBluePrintModel.getDesignerName());
        blueprint.setEstimatedCost(createBluePrintModel.getEstimateCost());
        blueprintRepository.saveAndFlush(blueprint);
    }

    @Override
    public Blueprint createBlueprint(Blueprint newBlueprint) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!projectService.existsById(newBlueprint.getProjectId())) {
            errorMsg += "No Project found with Id: '" + newBlueprint.getProjectId()
                    + "'. Which violate constraint: FK_Blueprint_Project. ";
        }
        if (!userService.existsById(newBlueprint.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newBlueprint.getCreatedBy()
                    + "'. Which violate constraint: FK_Blueprint_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (blueprintRepository
                .existsByProjectIdOrBlueprintNameAndStatusNotIn(
                        newBlueprint.getProjectId(),
                        newBlueprint.getBlueprintName(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another Blueprint with projectId: '" + newBlueprint.getProjectId()
                    + "'. Or with blueprintName: '" + newBlueprint.getBlueprintName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return blueprintRepository.saveAndFlush(newBlueprint);
    }
    @Override
    public BlueprintReadDTO createBlueprintByDTO(BlueprintCreateDTO newBlueprintDTO) throws Exception {
        Blueprint newBlueprint = modelMapper.map(newBlueprintDTO, Blueprint.class);

        newBlueprint = createBlueprint(newBlueprint);
        long newBlueprintId = newBlueprint.getBlueprintId();

        /* Create associate EntityWrapper */
        entityWrapperService
                .createEntityWrapper(newBlueprintId, ENTITY_TYPE, newBlueprint.getCreatedBy());

        return fillDTO(newBlueprint);
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

        Page<Blueprint> pagingResult = blueprintRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (pagingResult.hasContent()) {
            double totalPage = Math.ceil((double) pagingResult.getTotalElements() / pageSize);

            Page<ShowBlueprintModel> modelResult =
                    pagingResult.map(new Converter<Blueprint, ShowBlueprintModel>() {

                        @Override
                        protected ShowBlueprintModel doForward(Blueprint blueprint) {
                            ShowBlueprintModel model = new ShowBlueprintModel();

                            model.setProjectBlueprintId(blueprint.getBlueprintId());
                            model.setProjectBlueprintName(blueprint.getBlueprintName());
                            model.setProjectBlueprintCost(blueprint.getEstimatedCost());
                            model.setDesignerName(blueprint.getDesignerName());
                            model.setCreatedAt(blueprint.getCreatedAt());
                            model.setCreatedBy(blueprint.getCreatedBy());
                            model.setUpdatedAt(blueprint.getUpdatedAt());
                            model.setUpdatedBy(blueprint.getUpdatedBy());
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
    public Page<Blueprint> getPageAll(Pageable paging) throws Exception {
        Page<Blueprint> blueprintPage = blueprintRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (blueprintPage.isEmpty()) 
            return null;

        return blueprintPage;
    }
    @Override
    public List<BlueprintReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<Blueprint> blueprintPage = getPageAll(paging);

        if (blueprintPage == null ) 
            return null;

        List<Blueprint> blueprintList = blueprintPage.getContent();

        if (blueprintList.isEmpty()) 
            return null;

        return fillAllDTO(blueprintList, blueprintPage.getTotalPages());
    }

    @Override
    public boolean existsById(long blueprintId) throws Exception {
        return blueprintRepository
                .existsByBlueprintIdAndStatusNotIn(blueprintId, N_D_S_STATUS_LIST);
    }
    @Override
    public Blueprint getById(long blueprintId) throws Exception {
        return blueprintRepository
                .findByBlueprintIdAndStatusNotIn(blueprintId, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public BlueprintReadDTO getDTOById(long blueprintId) throws Exception {
        Blueprint blueprint = getById(blueprintId);

        if (blueprint == null) 
            return null;

        return fillDTO(blueprint);
    }

    @Override
    public List<Blueprint> getAllByIdIn(Collection<Long> blueprintIdCollection) throws Exception {
        List<Blueprint> blueprintList = 
                blueprintRepository.findAllByBlueprintIdInAndStatusNotIn(blueprintIdCollection, N_D_S_STATUS_LIST);
        
        if (blueprintList.isEmpty()) 
            return null;

        return blueprintList;
    }
    @Override
    public List<BlueprintReadDTO> getAllDTOByIdIn(Collection<Long> blueprintIdCollection) throws Exception {
        List<Blueprint> blueprintList = getAllByIdIn(blueprintIdCollection);

        if (blueprintList == null) 
            return null;

        return fillAllDTO(blueprintList, null);
    }

    @Override
    public Blueprint getByProjectId(long projectId) throws Exception {
        return blueprintRepository
                .findByProjectIdAndStatusNotIn(projectId, N_D_S_STATUS_LIST)
                .orElse(null);

    }
    @Override
    public BlueprintReadDTO getDTOByProjectId(long projectId) throws Exception {
        Blueprint blueprint = getByProjectId(projectId);

        if (blueprint == null) 
            return null;

        return fillDTO(blueprint);
    }

    @Override
    public List<Blueprint> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Blueprint> blueprintList =
                blueprintRepository.findAllByProjectIdInAndStatusNotIn(projectIdCollection, N_D_S_STATUS_LIST);

        if (blueprintList.isEmpty()) 
            return null;

        return blueprintList;
    }
    @Override
    public List<BlueprintReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Blueprint> blueprintList = getAllByProjectIdIn(projectIdCollection);

        if (blueprintList == null) 
            return null;

        return fillAllDTO(blueprintList, null);
    }
    @Override
    public Map<Long, BlueprintReadDTO> mapProjectIdBlueprintDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<BlueprintReadDTO> blueprintDTOList = getAllDTOByProjectIdIn(projectIdCollection);

        if (blueprintDTOList == null) 
            return new HashMap<>();

        return blueprintDTOList.stream()
                .collect(Collectors.toMap(BlueprintReadDTO::getProjectId, Function.identity()));
    }

    @Override
    public Blueprint getByBlueprintName(String blueprintName) throws Exception {
        return blueprintRepository
                .findByBlueprintNameAndStatusNotIn(blueprintName, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public BlueprintReadDTO getDTOByBlueprintName(String blueprintName) throws Exception {
        Blueprint blueprint = getByBlueprintName(blueprintName);

        if (blueprint == null) 
            return null;

        return fillDTO(blueprint);
    }

    @Override
    public List<Blueprint> getAllByBlueprintNameContains(String blueprintName) throws Exception {
        List<Blueprint> blueprintList =
                blueprintRepository.findAllByBlueprintNameContainsAndStatusNotIn(blueprintName, N_D_S_STATUS_LIST);

        if (blueprintList.isEmpty()) 
            return null;

        return blueprintList;
    }
    @Override
    public List<BlueprintReadDTO> getAllDTOByBlueprintNameContains(String blueprintName) throws Exception {
        List<Blueprint> blueprintList = getAllByBlueprintNameContains(blueprintName);

        if (blueprintList == null) 
            return null;

        return fillAllDTO(blueprintList, null);
    }
    @Override
    public Page<Blueprint> getPageAllByBlueprintNameContains(Pageable paging, String blueprintName) throws Exception {
        Page<Blueprint> blueprintPage =
                blueprintRepository.findAllByBlueprintNameContainsAndStatusNotIn(blueprintName, N_D_S_STATUS_LIST, paging);

        if (blueprintPage.isEmpty()) 
            return null;

        return blueprintPage;
    }
    @Override
    public List<BlueprintReadDTO> getAllDTOInPagingByBlueprintNameContains(Pageable paging, String blueprintName) throws Exception {
        Page<Blueprint> blueprintPage = getPageAllByBlueprintNameContains(paging, blueprintName);

        if (blueprintPage == null) 
            return null;

        List<Blueprint> blueprintList = blueprintPage.getContent();

        if (blueprintList.isEmpty()) 
            return null;

        return fillAllDTO(blueprintList, blueprintPage.getTotalPages());
    }

    @Override
    public List<Blueprint> getAllByDesignerName(String designerName) throws Exception {
        List<Blueprint> blueprintList =
                blueprintRepository.findAllByDesignerNameAndStatusNotIn(designerName, N_D_S_STATUS_LIST);

        if (blueprintList.isEmpty()) 
            return null;

        return blueprintList;
    }
    @Override
    public List<BlueprintReadDTO> getAllDTOByDesignerName(String designerName) throws Exception {
        List<Blueprint> blueprintList = getAllByDesignerName(designerName);

        if (blueprintList == null) 
            return null;

        return fillAllDTO(blueprintList, null);
    }
    @Override
    public Page<Blueprint> getPageAllByDesignerName(Pageable paging, String designerName) throws Exception {
        Page<Blueprint> blueprintPage =
                blueprintRepository.findAllByDesignerNameAndStatusNotIn(designerName, N_D_S_STATUS_LIST, paging);

        if (blueprintPage.isEmpty()) 
            return null;

        return blueprintPage;
    }
    @Override
    public List<BlueprintReadDTO> getAllDTOInPagingByDesignerName(Pageable paging, String designerName) throws Exception {
        Page<Blueprint> blueprintPage = getPageAllByDesignerName(paging, designerName);

        if (blueprintPage == null) 
            return null;

        List<Blueprint> blueprintList = blueprintPage.getContent();

        if (blueprintList.isEmpty()) 
            return null;

        return fillAllDTO(blueprintList, blueprintPage.getTotalPages());
    }

    @Override
    public List<Blueprint> getAllByDesignerNameContains(String designerName) throws Exception {
        List<Blueprint> blueprintList =
                blueprintRepository.findAllByDesignerNameContainsAndStatusNotIn(designerName, N_D_S_STATUS_LIST);

        if (blueprintList.isEmpty()) 
            return null;

        return blueprintList;
    }
    @Override
    public List<BlueprintReadDTO> getAllDTOByDesignerNameContains(String designerName) throws Exception {
        List<Blueprint> blueprintList = getAllByDesignerNameContains(designerName);

        if (blueprintList == null) 
            return null;

        return fillAllDTO(blueprintList, null);
    }
    @Override
    public Page<Blueprint> getPageAllByDesignerNameContains(Pageable paging, String designerName) throws Exception {
        Page<Blueprint> blueprintPage =
                blueprintRepository.findAllByDesignerNameContainsAndStatusNotIn(designerName, N_D_S_STATUS_LIST, paging);

        if (blueprintPage.isEmpty()) 
            return null;

        return blueprintPage;
    }
    @Override
    public List<BlueprintReadDTO> getAllDTOInPagingByDesignerNameContains(Pageable paging, String designerName) throws Exception {
        Page<Blueprint> blueprintPage = getPageAllByDesignerNameContains(paging, designerName);

        if (blueprintPage == null) 
            return null;

        List<Blueprint> blueprintList = blueprintPage.getContent();

        if (blueprintList.isEmpty()) 
            return null;

        return fillAllDTO(blueprintList, blueprintPage.getTotalPages());
    }

    @Override
    public List<Blueprint> getAllByEstimatedCostBetween(double from, double to) {
        List<Blueprint> blueprintList =
                blueprintRepository.findAllByEstimatedCostBetweenAndStatusNotIn(from, to, N_D_S_STATUS_LIST);

        if (blueprintList.isEmpty()) 
            return null;

        return blueprintList;
    }
    @Override
    public List<BlueprintReadDTO> getAllDTOByEstimatedCostBetween(double from, double to) {
        List<Blueprint> blueprintList = getAllByEstimatedCostBetween(from, to);

        if (blueprintList == null) 
            return null;

        return blueprintList.stream()
                .map(blueprint -> modelMapper.map(blueprint, BlueprintReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public String checkDuplicate(String blueprintName) {
        String result = "No duplicate";
        Blueprint checkDuplicate =
                blueprintRepository.getByBlueprintNameAndStatusNotIn(blueprintName, N_D_S_STATUS_LIST);

        if (checkDuplicate != null) {
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
        blueprint.setUpdatedAt(LocalDateTime.now());
        blueprintRepository.saveAndFlush(blueprint);
    }

    @Override
    public Blueprint updateBlueprint(Blueprint updatedBlueprint) throws Exception {
        Blueprint oldBlueprint = getById(updatedBlueprint.getBlueprintId());

        if (oldBlueprint == null) 
            return null;

        String errorMsg = "";

        /* Check FK (if changed) */
        if (!oldBlueprint.getProjectId().equals(updatedBlueprint.getProjectId())) {
            if (!projectService.existsById(updatedBlueprint.getProjectId())) {
                errorMsg += "No Project found with Id: '" + updatedBlueprint.getProjectId()
                        + "'. Which violate constraint: FK_Blueprint_Project. ";
            }
        }
        if (oldBlueprint.getUpdatedBy() != null) {
            if (!oldBlueprint.getUpdatedBy().equals(updatedBlueprint.getUpdatedBy())) {
                if (!userService.existsById(updatedBlueprint.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedBlueprint.getUpdatedBy()
                            + "'. Which violate constraint: FK_Blueprint_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedBlueprint.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedBlueprint.getUpdatedBy()
                        + "'. Which violate constraint: FK_Blueprint_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (blueprintRepository
                .existsByProjectIdOrBlueprintNameAndBlueprintIdIsNotAndStatusNotIn(
                        updatedBlueprint.getProjectId(),
                        updatedBlueprint.getBlueprintName(),
                        updatedBlueprint.getBlueprintId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another Blueprint with projectId: '" + updatedBlueprint.getProjectId()
                    + "'. Or with blueprintName: '" + updatedBlueprint.getBlueprintName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedBlueprint.setCreatedAt(oldBlueprint.getCreatedAt());
        updatedBlueprint.setCreatedBy(oldBlueprint.getCreatedBy());

        return blueprintRepository.saveAndFlush(updatedBlueprint);
    }
    @Override
    public BlueprintReadDTO updateBlueprintByDTO(BlueprintUpdateDTO updatedBlueprintDTO) throws Exception {
        Blueprint updatedBlueprint = modelMapper.map(updatedBlueprintDTO, Blueprint.class);

        updatedBlueprint = updateBlueprint(updatedBlueprint);

        if (updatedBlueprint == null) 
            return null;

        return fillDTO(updatedBlueprint);
    }

    /* DELETE */
    @Override
    public boolean deleteBlueprint(long blueprintId) throws Exception {
        Blueprint blueprint = getById(blueprintId);

        if (blueprint == null) {
            return false;
            /* Not found with Id */
        }

        blueprint.setStatus(Status.DELETED);
        blueprintRepository.saveAndFlush(blueprint);

        return true;
    }

    @Override
    public boolean deleteByProjectId(long projectId) throws Exception {
        Blueprint blueprint = getByProjectId(projectId);

        if (blueprint == null) {
            return false;
            /* Not found with Id */
        }

        blueprint.setStatus(Status.DELETED);
        blueprintRepository.saveAndFlush(blueprint);

        return true;
    }
    @Override
    public boolean deleteAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Blueprint> blueprintList = getAllByProjectIdIn(projectIdCollection);

        if (blueprintList == null) {
            return false;
            /* Not found with Id */
        }

        blueprintList =
                blueprintList.stream()
                        .peek(blueprint -> blueprint.setStatus(Status.DELETED))
                        .collect(Collectors.toList());

        blueprintRepository.saveAllAndFlush(blueprintList);

        return true;
    }

    /* Utils */
    private BlueprintReadDTO fillDTO(Blueprint blueprint) throws Exception {
        long blueprintId = blueprint.getBlueprintId();

        BlueprintReadDTO blueprintDTO = modelMapper.map(blueprint, BlueprintReadDTO.class);

//        blueprintDTO.setFileList(
//                eFEWPairingService
//                        .getAllExternalFileDTOByEntityIdAndEntityType(blueprintId, ENTITY_TYPE));

        return blueprintDTO;
    }

    private List<BlueprintReadDTO> fillAllDTO(Collection<Blueprint> blueprintCollection, Integer totalPage) throws Exception {
        Set<Long> blueprintIdSet = new HashSet<>();

        for (Blueprint blueprint : blueprintCollection) {
            blueprintIdSet.add(blueprint.getBlueprintId());
        }

        /* Get associated ExternalFile */
//        Map<Long, List<ExternalFileReadDTO>> projectIdExternalFileDTOListMap =
//                eFEWPairingService
//                        .mapEntityIdExternalFileDTOListByEntityIdInAndEntityType(blueprintIdSet, ENTITY_TYPE);

        return blueprintCollection.stream()
                .map(blueprint -> {
                    BlueprintReadDTO blueprintDTO =
                            modelMapper.map(blueprint, BlueprintReadDTO.class);

//                    blueprintDTO.setFileList(
//                            projectIdExternalFileDTOListMap.get(blueprint.getBlueprintId()));

                    blueprintDTO.setTotalPage(totalPage);

                    return blueprintDTO;})
                .collect(Collectors.toList());
    }
}
