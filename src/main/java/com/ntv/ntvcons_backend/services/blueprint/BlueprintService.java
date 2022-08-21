package com.ntv.ntvcons_backend.services.blueprint;

import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintCreateDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintReadDTO;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintUpdateDTO;
import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.ShowBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.UpdateBlueprintModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BlueprintService {
    /* CREATE */
    void createProjectBlueprint(CreateBlueprintModel createBluePrintModel);

    Blueprint createBlueprint(Blueprint newBlueprint) throws Exception;
    BlueprintReadDTO createBlueprintByDTO(BlueprintCreateDTO newBlueprintDTO) throws Exception;

    /* READ */
    List<ShowBlueprintModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType);

    Page<Blueprint> getPageAll(Pageable paging)throws Exception;
    List<BlueprintReadDTO> getAllDTOInPaging(Pageable paging)throws Exception;

    boolean existsById(long blueprintId)throws Exception;
    Blueprint getById(long blueprintId)throws Exception;
    BlueprintReadDTO getDTOById(long blueprintId)throws Exception;

    List<Blueprint> getAllByIdIn(Collection<Long> blueprintIdCollection) throws Exception;
    List<BlueprintReadDTO> getAllDTOByIdIn(Collection<Long> blueprintIdCollection)throws Exception;

    Blueprint getByProjectId(long projectId)throws Exception;
    BlueprintReadDTO getDTOByProjectId(long projectId)throws Exception;

    List<Blueprint> getAllByProjectIdIn(Collection<Long> projectIdCollection)throws Exception;
    List<BlueprintReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection)throws Exception;
    Map<Long, BlueprintReadDTO> mapProjectIdBlueprintDTOByProjectIdIn(Collection<Long> projectIdCollection)throws Exception;

    Blueprint getByBlueprintName(String blueprintName) throws Exception;
    BlueprintReadDTO getDTOByBlueprintName(String blueprintName) throws Exception;

    List<Blueprint> getAllByBlueprintNameContains(String blueprintName) throws Exception;
    List<BlueprintReadDTO> getAllDTOByBlueprintNameContains(String blueprintName) throws Exception;
    Page<Blueprint> getPageAllByBlueprintNameContains(Pageable paging, String blueprintName) throws Exception;
    List<BlueprintReadDTO> getAllDTOInPagingByBlueprintNameContains(Pageable paging, String blueprintName) throws Exception;

    List<Blueprint> getAllByDesignerName(String designerName) throws Exception;
    List<BlueprintReadDTO> getAllDTOByDesignerName(String designerName) throws Exception;
    Page<Blueprint> getPageAllByDesignerName(Pageable paging, String designerName) throws Exception;
    List<BlueprintReadDTO> getAllDTOInPagingByDesignerName(Pageable paging, String designerName) throws Exception;

    List<Blueprint> getAllByDesignerNameContains(String designerName) throws Exception;
    List<BlueprintReadDTO> getAllDTOByDesignerNameContains(String designerName) throws Exception;
    Page<Blueprint> getPageAllByDesignerNameContains(Pageable paging, String designerName) throws Exception;
    List<BlueprintReadDTO> getAllDTOInPagingByDesignerNameContains(Pageable paging, String designerName) throws Exception;

    List<Blueprint> getAllByEstimatedCostBetween(double from, double to);
    List<BlueprintReadDTO> getAllDTOByEstimatedCostBetween(double from, double to);

    String checkDuplicate(String blueprintName);

    /* UPDATE */
    void updateBlueprint(UpdateBlueprintModel updateBlueprintModel);

    Blueprint updateBlueprint(Blueprint updatedBlueprint) throws Exception;
    BlueprintReadDTO updateBlueprintByDTO(BlueprintUpdateDTO updatedBlueprintDTO) throws Exception;

    /* DELETE */
    boolean deleteBlueprint(long blueprintId) throws Exception;
    boolean deleteByProjectId(long projectId) throws Exception;
    boolean deleteAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
}
