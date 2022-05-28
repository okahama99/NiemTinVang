package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.repositories.EntityWrapperRepository;
import com.ntv.ntvcons_backend.repositories.ExternalFileEntityWrapperPairingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntityWrapperServiceImpl implements EntityWrapperService {
    @Autowired
    EntityWrapperRepository entityWrapperRepository;


}
