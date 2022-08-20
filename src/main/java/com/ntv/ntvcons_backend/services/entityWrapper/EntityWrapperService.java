package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.entities.EntityWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface EntityWrapperService {
    /* CREATE */
    EntityWrapper createEntityWrapper(long entityId, EntityType type, Long createdBy) throws Exception;

    /* READ */
    List<EntityWrapper> getAll() throws Exception;

    boolean existsById(long entityWrapperId) throws Exception;
    EntityWrapper getById(long entityWrapperId) throws Exception;

    List<EntityWrapper> getAllByEntityType(EntityType type) throws Exception;

    boolean existsByEntityIdAndEntityType(long entityID, EntityType type) throws Exception;
    EntityWrapper getByEntityIdAndEntityType(long entityID, EntityType type) throws Exception;

    List<EntityWrapper> getAllByEntityIdInAndEntityType(Collection<Long> entityIdCollection, EntityType type) throws Exception;
    Map<Long, Long> mapEntityWrapperIdEntityIdByEntityIdInAndEntityType(Collection<Long> entityIdCollection, EntityType type) throws Exception;

    /* UPDATE */

    /* DELETE */
    boolean deleteEntityWrapper(long entityWrapperID) throws Exception;
    boolean deleteByEntityIdAndEntityType(long entityID, EntityType type) throws Exception;
    boolean deleteAllByEntityIdInAndEntityType(Collection<Long> entityIDCollection, EntityType type) throws Exception;
}