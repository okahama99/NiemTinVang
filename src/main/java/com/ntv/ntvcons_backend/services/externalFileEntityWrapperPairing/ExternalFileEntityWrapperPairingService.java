package com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.entities.ExternalFileEntityWrapperPairing;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ExternalFileEntityWrapperPairingService {
    /* CREATE */
    ExternalFileEntityWrapperPairing createPairing(long entityId, EntityType type, long fileId, long createdBy) throws Exception;

    List<ExternalFileEntityWrapperPairing> createBulkPairingByEntityIdAndEntityType(
            long entityId, EntityType type, Collection<Long> fileIdCollection, long createdBy) throws Exception;

    /* READ */
//    Page<ExternalFileEntityWrapperPairing> getPageAll(Pageable paging) throws Exception;
    List<ExternalFileEntityWrapperPairing> getAll() throws Exception;

    boolean existsById(long pairingId) throws Exception;
    ExternalFileEntityWrapperPairing getById(long pairingId) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperId(long entityWrapperId) throws Exception;
    List<ExternalFileReadDTO> getAllExternalFileDTOByEntityIdAndEntityType(long entityId, EntityType type) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperIdIn(Collection<Long> entityWrapperIdCollection) throws Exception;
    Map<Long, List<ExternalFileReadDTO>> mapEntityWrapperIdExternalFileDTOListByEntityWrapperIdIn
            (Collection<Long> entityWrapperIdCollection) throws Exception;
    Map<Long, List<ExternalFileReadDTO>> mapEntityIdExternalFileDTOListByEntityIdInAndEntityType
            (Collection<Long> entityIdCollection, EntityType type) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByExternalFileId(long externalFileId) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByExternalFileIdIn(Collection<Long> externalFileIdCollection) throws Exception;

    /* UPDATE */
    ExternalFileEntityWrapperPairing updatePairing(ExternalFileEntityWrapperPairing updatedPairing) throws Exception;

    /* DELETE */
    boolean deletePairing(long pairingId) throws Exception;

    boolean deleteAllByEntityWrapperId(long entityWrapperId) throws Exception;
    boolean deleteAllByEntityWrapperIdIn(Collection<Long> entityWrapperIdCollection) throws Exception;

    boolean deleteAllByExternalFileId(long fileId) throws Exception;
    boolean deleteAllByExternalFileIdIn(Collection<Long> fileIdCollection) throws Exception;
}
