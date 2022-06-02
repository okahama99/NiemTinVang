package com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing;

import java.util.List;

public interface ExternalFileEntityWrapperPairingService {
    /* CREATE */
    ExternalFileEntityWrapperPairing createPairing(int externalFileId, int entityWrapperId);

    /* READ */
    List<ExternalFileEntityWrapperPairing> getAll();

    List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperId(int entityWrapperId);

    List<ExternalFileEntityWrapperPairing> getAllByExternalFileId(int externalFileId);

    ExternalFileEntityWrapperPairing getById(int pairingId);

    /* UPDATE */
    /* DELETE */
    boolean deletePairing(int pairingId);

}
