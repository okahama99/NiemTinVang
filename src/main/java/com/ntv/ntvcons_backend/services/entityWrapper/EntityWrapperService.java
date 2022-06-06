package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.entities.EntityWrapper;

import java.util.Collection;
import java.util.List;

public interface EntityWrapperService {
    /* CREATE */
    EntityWrapper createEntityWrapper(int entityID, EntityType type);

    /* READ */
    List<EntityWrapper> getAll();

    List<EntityWrapper> getAllByEntityType(EntityType type);

    List<EntityWrapper> getAllByEntityIdInAndEntityType(Collection<Integer> entityIdCollection, EntityType type);

    EntityWrapper getByEntityIdAndEntityType(int entityID, EntityType type);

    EntityWrapper getById(int entityWrapperId);

    /* UPDATE */

    /* DELETE */
    boolean deleteEntityWrapper(int entityWrapperID);
}