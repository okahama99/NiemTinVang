package com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing;

import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.entities.ExternalFileEntityWrapperPairing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ExternalFileEntityWrapperPairingService {
    /* CREATE */
    ExternalFileEntityWrapperPairing createPairing(ExternalFileEntityWrapperPairing newPairing) throws Exception;

    /* READ */
//    Page<ExternalFileEntityWrapperPairing> getPageAll(Pageable paging) throws Exception;
    List<ExternalFileEntityWrapperPairing> getAll() throws Exception;

    boolean existsById(long pairingId) throws Exception;
    ExternalFileEntityWrapperPairing getById(long pairingId) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperId(long entityWrapperId) throws Exception;
    List<ExternalFileReadDTO> getAllExternalFileDTOByEntityWrapperId(long entityWrapperId) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperIdIn(Collection<Long> entityWrapperIdCollection) throws Exception;
    Map<Long, List<ExternalFileReadDTO>> mapEntityWrapperIdExternalFileDTOListByEntityWrapperIdIn(Collection<Long> entityWrapperIdCollection) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByExternalFileId(long externalFileId) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByExternalFileIdIn(Collection<Long> externalFileIdCollection) throws Exception;
    Map<Long, List<ExternalFileReadDTO>> mapEntityWrapperIdExternalFileDTOListByExternalFileIdIn(Collection<Long> externalFileIdCollection) throws Exception;

    /* UPDATE */
    ExternalFileEntityWrapperPairing updatePairing(ExternalFileEntityWrapperPairing updatedPairing) throws Exception;

    /* DELETE */
    boolean deletePairing(long pairingId) throws Exception;

    boolean deleteAllByEntityWrapperId(long entityWrapperId) throws Exception;
    boolean deleteAllByEntityWrapperIdIn(Collection<Long> entityWrapperIdCollection) throws Exception;

    boolean deleteAllByExternalFileId(long fileId) throws Exception;
    boolean deleteAllByExternalFileIdIn(Collection<Long> fileIdCollection) throws Exception;
}
