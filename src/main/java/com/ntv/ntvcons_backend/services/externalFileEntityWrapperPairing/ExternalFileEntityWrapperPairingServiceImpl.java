package com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing;


import com.ntv.ntvcons_backend.entities.ExternalFileEntityWrapperPairing;
import com.ntv.ntvcons_backend.repositories.EntityWrapperRepository;
import com.ntv.ntvcons_backend.repositories.ExternalFileEntityWrapperPairingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalFileEntityWrapperPairingServiceImpl implements ExternalFileEntityWrapperPairingService {
    @Autowired
    private ExternalFileEntityWrapperPairingRepository eFEWPRepository;
    @Autowired
    private EntityWrapperRepository entityWrapperRepository;


    @Override
    public ExternalFileEntityWrapperPairing createPairing(int externalFileId, int entityWrapperId) {
        return null;
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAll() {
        return null;
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperId(int entityWrapperId) {
        return null;
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAllByExternalFileId(int externalFileId) {
        return null;
    }

    @Override
    public ExternalFileEntityWrapperPairing getById(int pairingId) {
        return null;
    }

    @Override
    public boolean deletePairing(int pairingId) {
        return false;
    }
}
