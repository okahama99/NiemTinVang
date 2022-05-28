package com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing;

import com.ntv.ntvcons_backend.entities.ExternalFile;
import com.ntv.ntvcons_backend.entities.externalFileModel.ShowExternalFileModel;
import com.ntv.ntvcons_backend.repositories.ExternalFileEntityWrapperPairingRepository;
import com.ntv.ntvcons_backend.repositories.ExternalFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalFileEntityWrapperPairingServiceImpl implements ExternalFileEntityWrapperPairingService {
    @Autowired
    ExternalFileEntityWrapperPairingRepository eFEWPRepository;


}
