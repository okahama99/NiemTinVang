package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.entities.EntityWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface EntityWrapperService {
    /* CREATE */
    EntityWrapper createEntityWrapper(long entityId, EntityType type, long createdBy) throws Exception;

    /* READ */
    List<EntityWrapper> getAll() throws Exception;

    EntityWrapper getById(long entityWrapperId) throws Exception;

    List<EntityWrapper> getAllByEntityType(EntityType type) throws Exception;

    EntityWrapper getByEntityIdAndEntityType(long entityID, EntityType type) throws Exception;

    List<EntityWrapper> getAllByEntityIdInAndEntityType(Collection<Long> entityIdCollection, EntityType type) throws Exception;
    Map<Long, Long> mapEntityIdEntityWrapperIdByEntityIdInAndEntityType(Collection<Long> entityIdCollection, EntityType type) throws Exception;

    /* UPDATE */

    /* DELETE */
    boolean deleteEntityWrapper(long entityWrapperID) throws Exception;
    boolean deleteByEntityIdAndEntityType(long entityWrapperID, EntityType type) throws Exception;
}