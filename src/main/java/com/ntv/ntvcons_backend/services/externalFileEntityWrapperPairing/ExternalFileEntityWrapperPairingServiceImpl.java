package com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing;

import com.ntv.ntvcons_backend.entities.ExternalFile;
import com.ntv.ntvcons_backend.entities.ExternalFileEntityWrapperPairing;
import com.ntv.ntvcons_backend.repositories.EntityWrapperRepository;
import com.ntv.ntvcons_backend.repositories.ExternalFileEntityWrapperPairingRepository;
import com.ntv.ntvcons_backend.services.entityWrapper.EntityWrapperService;
import com.ntv.ntvcons_backend.services.externalFile.ExternalFileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ExternalFileEntityWrapperPairingServiceImpl implements ExternalFileEntityWrapperPairingService {
    @Autowired
    private ExternalFileEntityWrapperPairingRepository eFEWPRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private EntityWrapperService entityWrapperService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ExternalFileService externalFileService;

    /* CREATE */
    @Override
    public ExternalFileEntityWrapperPairing createPairing(long externalFileId, long entityWrapperId) throws Exception {
        return null;
    }

    /* READ */
    @Override
    public Page<ExternalFileEntityWrapperPairing> getPageAll(Pageable paging) throws Exception {
        return null;
    }

    @Override
    public ExternalFileEntityWrapperPairing getById(long pairingId) throws Exception {
        return null;
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperId(long entityWrapperId) throws Exception {
        return null;
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAllByEntityWrapperIdIn(Collection<Long> entityWrapperIdCollection) throws Exception {
        return null;
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAllByExternalFileId(long externalFileId) throws Exception {
        return null;
    }

    @Override
    public List<ExternalFileEntityWrapperPairing> getAllByExternalFileIdIn(Collection<Long> externalFileIdCollection) throws Exception {
        return null;
    }

    /* UPDATE */
    @Override
    public ExternalFileEntityWrapperPairing updatePairing(ExternalFileEntityWrapperPairing updatedPairing) throws Exception {
        return null;
    }

    /* DELETE */
    @Override
    public boolean deletePairing(long pairingId) throws Exception {
        return false;
    }

}
