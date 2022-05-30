package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.entities.EntityWrapper;
import com.ntv.ntvcons_backend.repositories.EntityWrapperRepository;
import com.ntv.ntvcons_backend.repositories.ExternalFileEntityWrapperPairingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class EntityWrapperServiceImpl implements EntityWrapperService {
    @Autowired
    private EntityWrapperRepository entityWrapperRepository;


    @Override
    public EntityWrapper createEntityWrapper(int entityID, EntityType type) {
        return null;
    }

    @Override
    public List<EntityWrapper> getAll() {
        return null;
    }

    @Override
    public List<EntityWrapper> getAllByEntityType(EntityType type) {
        return null;
    }

    @Override
    public List<EntityWrapper> getAllByEntityIdInAndEntityType(Collection<Integer> entityIdCollection, EntityType type) {
        return null;
    }

    @Override
    public EntityWrapper getByEntityIdAndEntityType(int entityID, EntityType type) {
        return null;
    }

    @Override
    public EntityWrapper getById(int entityWrapperId) {
        return null;
    }

    @Override
    public boolean deleteEntityWrapper(int entityWrapperID) {
        return false;
    }
}
