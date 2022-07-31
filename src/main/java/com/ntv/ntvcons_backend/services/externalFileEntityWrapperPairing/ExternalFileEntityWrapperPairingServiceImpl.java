package com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.entities.EntityWrapper;
import com.ntv.ntvcons_backend.entities.ExternalFileEntityWrapperPairing;
import com.ntv.ntvcons_backend.repositories.ExternalFileEntityWrapperPairingRepository;
import com.ntv.ntvcons_backend.services.entityWrapper.EntityWrapperService;
import com.ntv.ntvcons_backend.services.externalFile.ExternalFileService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExternalFileEntityWrapperPairingServiceImpl implements ExternalFileEntityWrapperPairingService {
    @Autowired
    private ExternalFileEntityWrapperPairingRepository eFEWPairingRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private EntityWrapperService entityWrapperService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ExternalFileService externalFileService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public ExternalFileEntityWrapperPairing createPairing(long entityId, EntityType type, long fileId, long createdBy) throws Exception {
        String errorMsg = "";

        EntityWrapper entityWrapper = entityWrapperService.getByEntityIdAndEntityType(entityId, type);

        if (entityWrapper == null)
            entityWrapper = entityWrapperService.createEntityWrapper(entityId, type, createdBy);

        /* Check FK */
        if (!entityWrapperService.existsById(entityWrapper.getEntityWrapperId())) {
            errorMsg += "No EntityWrapper found with Id: '" + entityWrapper.getEntityWrapperId()
                    + "'. Which violate constraint: FK_EFEWP_EntityWrapper. ";
        }
        if (!externalFileService.existsById(fileId)) {
            errorMsg += "No ExternalFile found with Id: '" + fileId
                    + "'. Which violate constraint: FK_EFEWP_ExternalFile. ";
        }
        if (!userService.existsById(createdBy)) {
            errorMsg += "No User (CreatedBy) found with Id: '" + createdBy
                    + "'. Which violate constraint: FK_EFEWP_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (eFEWPairingRepository
                .existsByEntityWrapperIdAndExternalFileIdAndStatusNotIn(
                        entityWrapper.getEntityWrapperId(),
                        fileId,
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another EWEFPairing relationship between with EntityWrapper with Id: '"
                    + entityWrapper.getEntityWrapperId()
                    + "' and ExternalFile with Id: '" + entityWrapper + "'. ";
        }

        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        ExternalFileEntityWrapperPairing newPairing =
                new ExternalFileEntityWrapperPairing(fileId, entityWrapper.getEntityWrapperId());
        newPairing.setStatus(Status.ACTIVE);
        newPairing.setCreatedBy(createdBy);
        newPairing.setCreatedAt(LocalDateTime.now());

        return eFEWPairingRepository.saveAndFlush(newPairing);
    }

    /* READ */
    @Override
    public List<ExternalFileEntityWrapperPairing> getAll() throws Exception {
        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                eFEWPairingRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST);

        if (eFEWPairingList.isEmpty())
            return null;

        return eFEWPairingList;
    }

    @Override
    public boolean existsById(long pairingId) throws Exception {
        return eFEWPairingRepository
                .existsByPairingIdAndStatusNotIn(pairingId, N_D_S_STATUS_LIST);
    }
    @Override
    public ExternalFileEntityWrapperPairing getById(long pairingId) throws Exception {
        return eFEWPairingRepository
                .findByPairingIdAndStatusNotIn(pairingId, N_D_S_STATUS_LIST)
                .orElse(null);
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperId(long entityWrapperId) throws Exception {
        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                eFEWPairingRepository.findAllByEntityWrapperIdAndStatusNotIn(entityWrapperId, N_D_S_STATUS_LIST);

        if (eFEWPairingList.isEmpty())
            return null;

        return eFEWPairingList;
    }
    @Override
    public List<ExternalFileReadDTO> getAllExternalFileDTOByEntityIdAndEntityType(long entityId, EntityType type) throws Exception {
        EntityWrapper entityWrapper =
                entityWrapperService.getByEntityIdAndEntityType(entityId, type);

        if (entityWrapper == null)
            return null;

        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                getAllByEntityWrapperId(entityWrapper.getEntityWrapperId());

        if (eFEWPairingList == null)
            return null;

        Set<Long> fileIdSet = new HashSet<>();

        for (ExternalFileEntityWrapperPairing eFEWPairing : eFEWPairingList) {
            fileIdSet.add(eFEWPairing.getExternalFileId());
        }

        return externalFileService.getAllDTOByIdIn(fileIdSet);
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperIdIn(Collection<Long> entityWrapperIdCollection) throws Exception {
        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                eFEWPairingRepository.findAllByEntityWrapperIdInAndStatusNotIn(entityWrapperIdCollection, N_D_S_STATUS_LIST);

        if (eFEWPairingList.isEmpty())
            return null;

        return eFEWPairingList;
    }
    @Override
    public Map<Long, List<ExternalFileReadDTO>> mapEntityWrapperIdExternalFileDTOListByEntityWrapperIdIn(Collection<Long> entityWrapperIdCollection) throws Exception {
        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                getAllByEntityWrapperIdIn(entityWrapperIdCollection);

        if (eFEWPairingList == null)
            return new HashMap<>();

        Set<Long> fileIdSet = new HashSet<>();

        for (ExternalFileEntityWrapperPairing eFEWPairing : eFEWPairingList) {
            fileIdSet.add(eFEWPairing.getExternalFileId());
        }

        Map<Long, ExternalFileReadDTO> fileIdFileDTOMap =
                externalFileService.mapFileIdExternalFileDTOByIdIn(fileIdSet);

        if (fileIdFileDTOMap.isEmpty())
            return new HashMap<>();

        Map<Long, List<ExternalFileReadDTO>> entityWrapperIdExternalFileDTOListMap = new HashMap<>();

        long tmpEntityWrapperId;
        long tmpFileId;
        ExternalFileReadDTO tmpFileDTO;
        List<ExternalFileReadDTO> tmpFileDTOList;

        for (ExternalFileEntityWrapperPairing eFEWPairing : eFEWPairingList) {
            tmpEntityWrapperId = eFEWPairing.getEntityWrapperId();
            tmpFileId = eFEWPairing.getExternalFileId();
            tmpFileDTOList = entityWrapperIdExternalFileDTOListMap.get(tmpEntityWrapperId);

            if (tmpFileDTOList == null) {
                tmpFileDTO = fileIdFileDTOMap.get(tmpFileId);

                if (tmpFileDTO != null) {
                    entityWrapperIdExternalFileDTOListMap
                            .put(tmpEntityWrapperId, new ArrayList<>(Collections.singletonList(tmpFileDTO)));
                }
            } else {
                tmpFileDTO = fileIdFileDTOMap.get(tmpFileId);

                if (tmpFileDTO != null) {
                    tmpFileDTOList.add(tmpFileDTO);

                    entityWrapperIdExternalFileDTOListMap
                            .put(tmpEntityWrapperId, tmpFileDTOList);
                }
            }
        }

        return entityWrapperIdExternalFileDTOListMap;
    }
    @Override
    public Map<Long, List<ExternalFileReadDTO>> mapEntityIdExternalFileDTOListByEntityIdInAndEntityType(Collection<Long> entityIdCollection, EntityType type) throws Exception {
        Map<Long, Long> entityWrapperIdEntityIdMap = new HashMap<>();
        Map<Long, List<ExternalFileReadDTO>> entityWrapperIdExternalFileDTOListMap = new HashMap<>();
        Map<Long, List<ExternalFileReadDTO>> projectIdExternalFileDTOListMap = new HashMap<>();

        entityWrapperIdEntityIdMap =
                entityWrapperService
                        .mapEntityWrapperIdEntityIdByEntityIdInAndEntityType(entityIdCollection, type);

        Set<Long> entityWrapperIdSet = entityWrapperIdEntityIdMap.keySet();

        if (!entityWrapperIdSet.isEmpty()) {
            entityWrapperIdExternalFileDTOListMap =
                    mapEntityWrapperIdExternalFileDTOListByEntityWrapperIdIn(entityWrapperIdSet);

            if (!entityWrapperIdExternalFileDTOListMap.isEmpty()) {
                for (Long entityWrapperId : entityWrapperIdSet) {
                    projectIdExternalFileDTOListMap.put(
                            entityWrapperIdEntityIdMap.get(entityWrapperId),
                            entityWrapperIdExternalFileDTOListMap.get(entityWrapperId));
                }
            }
        }

        return projectIdExternalFileDTOListMap;
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAllByExternalFileId(long externalFileId) throws Exception {
        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                eFEWPairingRepository.findAllByExternalFileIdAndStatusNotIn(externalFileId, N_D_S_STATUS_LIST);

        if (eFEWPairingList.isEmpty())
            return null;

        return eFEWPairingList;
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAllByExternalFileIdIn(Collection<Long> externalFileIdCollection) throws Exception {
        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                eFEWPairingRepository.findAllByExternalFileIdInAndStatusNotIn(externalFileIdCollection, N_D_S_STATUS_LIST);

        if (eFEWPairingList.isEmpty())
            return null;

        return eFEWPairingList;
    }

    /* UPDATE */
    @Override
    public ExternalFileEntityWrapperPairing updatePairing(ExternalFileEntityWrapperPairing updatedPairing) throws Exception {
        ExternalFileEntityWrapperPairing oldPairing = getById(updatedPairing.getPairingId());

        if (oldPairing == null)
            return null;

        String errorMsg = "";

        /* Check FK */
        if (!oldPairing.getEntityWrapperId().equals(updatedPairing.getEntityWrapperId())) {
            if (!entityWrapperService.existsById(updatedPairing.getEntityWrapperId())) {
                errorMsg += "No EntityWrapper found with Id: '" + updatedPairing.getEntityWrapperId()
                        + "'. Which violate constraint: FK_EFEWP_EntityWrapper. ";
            }
        }
        if (!oldPairing.getEntityWrapperId().equals(updatedPairing.getEntityWrapperId())) {
            if (!externalFileService.existsById(updatedPairing.getExternalFileId())) {
                errorMsg += "No ExternalFile found with Id: '" + updatedPairing.getExternalFileId()
                        + "'. Which violate constraint: FK_EFEWP_ExternalFile. ";
            }
        }
        if (oldPairing.getUpdatedBy() != null) {
            if (oldPairing.getUpdatedBy().equals(updatedPairing.getUpdatedBy())) {
                if (userService.existsById(updatedPairing.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedPairing.getUpdatedBy()
                            + "'. Which violate constraint: FK_EFEWP_User_UpdatedBy. ";
                }
            }
        } else {
            if (userService.existsById(updatedPairing.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedPairing.getUpdatedBy()
                        + "'. Which violate constraint: FK_EFEWP_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (eFEWPairingRepository
                .existsByEntityWrapperIdAndExternalFileIdAndPairingIdIsNotAndStatusNotIn(
                        updatedPairing.getEntityWrapperId(),
                        updatedPairing.getExternalFileId(),
                        updatedPairing.getPairingId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another EWEFPairing relationship between with EntityWrapper with Id: '"
                    + updatedPairing.getEntityWrapperId()
                    + "' and ExternalFile with Id: '"
                    + updatedPairing.getExternalFileId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        return eFEWPairingRepository.saveAndFlush(updatedPairing);
    }

    /* DELETE */
    @Override
    public boolean deletePairing(long pairingId) throws Exception {
        ExternalFileEntityWrapperPairing eFEWPairing = getById(pairingId);

        if (eFEWPairing == null)
            return false;

        eFEWPairing.setStatus(Status.DELETED);
        eFEWPairingRepository.saveAndFlush(eFEWPairing);

        return true;
    }

    @Override
    public boolean deleteAllByEntityWrapperId(long entityWrapperId) throws Exception {
        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                getAllByEntityWrapperId(entityWrapperId);

        if (eFEWPairingList == null)
            return false;

        eFEWPairingList = eFEWPairingList.stream()
                .peek(eFEWPairing -> eFEWPairing.setStatus(Status.DELETED))
                .collect(Collectors.toList());

        eFEWPairingRepository.saveAllAndFlush(eFEWPairingList);

        return true;
    }
    @Override
    public boolean deleteAllByEntityWrapperIdIn(Collection<Long> entityWrapperIdCollection) throws Exception {
        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                getAllByEntityWrapperIdIn(entityWrapperIdCollection);

        if (eFEWPairingList == null)
            return false;

        eFEWPairingList = eFEWPairingList.stream()
                .peek(eFEWPairing -> eFEWPairing.setStatus(Status.DELETED))
                .collect(Collectors.toList());

        eFEWPairingRepository.saveAllAndFlush(eFEWPairingList);

        return true;
    }

    @Override
    public boolean deleteAllByExternalFileId(long fileId) throws Exception {
        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                getAllByExternalFileId(fileId);

        if (eFEWPairingList == null)
            return false;

        eFEWPairingList = eFEWPairingList.stream()
                .peek(eFEWPairing -> eFEWPairing.setStatus(Status.DELETED))
                .collect(Collectors.toList());

        eFEWPairingRepository.saveAllAndFlush(eFEWPairingList);

        return true;
    }
    @Override
    public boolean deleteAllByExternalFileIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFileEntityWrapperPairing> eFEWPairingList =
                getAllByExternalFileIdIn(fileIdCollection);

        if (eFEWPairingList == null)
            return false;

        eFEWPairingList = eFEWPairingList.stream()
                .peek(eFEWPairing -> eFEWPairing.setStatus(Status.DELETED))
                .collect(Collectors.toList());

        eFEWPairingRepository.saveAllAndFlush(eFEWPairingList);

        return true;
    }
}
