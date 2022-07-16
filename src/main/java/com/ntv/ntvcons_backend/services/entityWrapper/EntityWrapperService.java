package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.entities.EntityWrapper;

import java.util.Collection;
import java.util.List;

public interface EntityWrapperService {
    /* CREATE */
    EntityWrapper createEntityWrapper(EntityWrapper entityWrapper, EntityType type) throws Exception;

    /* READ */
    List<EntityWrapper> getAll() throws Exception;

    List<EntityWrapper> getAllByEntityType(EntityType type) throws Exception;

    List<EntityWrapper> getAllByEntityIdInAndEntityType(Collection<Integer> entityIdCollection, EntityType type) throws Exception;

    EntityWrapper getByEntityIdAndEntityType(int entityID, EntityType type) throws Exception;

    EntityWrapper getById(int entityWrapperId) throws Exception;

    /* UPDATE */

    /* DELETE */
    boolean deleteEntityWrapper(int entityWrapperID) throws Exception;
}