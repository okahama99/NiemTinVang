package com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing;

import com.ntv.ntvcons_backend.entities.ExternalFileEntityWrapperPairing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface ExternalFileEntityWrapperPairingService {
    /* CREATE */
    ExternalFileEntityWrapperPairing createPairing(long externalFileId, long entityWrapperId) throws Exception;

    /* READ */
    Page<ExternalFileEntityWrapperPairing> getPageAll(Pageable paging) throws Exception;
    
    ExternalFileEntityWrapperPairing getById(long pairingId) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperId(long entityWrapperId) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperIdIn(Collection<Long> entityWrapperIdCollection) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByExternalFileId(long externalFileId) throws Exception;

    List<ExternalFileEntityWrapperPairing> getAllByExternalFileIdIn(Collection<Long> externalFileIdCollection) throws Exception;

    /* UPDATE */
    ExternalFileEntityWrapperPairing updatePairing(ExternalFileEntityWrapperPairing updatedPairing) throws Exception;

    /* DELETE */
    boolean deletePairing(long pairingId) throws Exception;
}
