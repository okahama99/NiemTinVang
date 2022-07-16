package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.entities.EntityWrapper;
import com.ntv.ntvcons_backend.repositories.EntityWrapperRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class EntityWrapperServiceImpl implements EntityWrapperService {
    @Autowired
    private EntityWrapperRepository entityWrapperRepository;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    /* CREATE */
    @Override
    public EntityWrapper createEntityWrapper(EntityWrapper newEntityWrapper, EntityType type) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (newEntityWrapper.getCreatedBy() != null) {
            if (!userService.existsById(newEntityWrapper.getCreatedBy())) {
                errorMsg += "No User (CreatedBy) found with Id: '" + newEntityWrapper.getCreatedBy()
                        + "'. Which violate constraint: FK_EntityWrapper_User_CreatedBy. ";
            }
        }

        /* Check duplicate */



        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);


        return entityWrapperRepository.saveAndFlush(newEntityWrapper);
    }

    /* READ */
    @Override
    public List<EntityWrapper> getAll() throws Exception {
        return null;
    }

    @Override
    public List<EntityWrapper> getAllByEntityType(EntityType type) throws Exception {
        return null;
    }

    @Override
    public List<EntityWrapper> getAllByEntityIdInAndEntityType(Collection<Integer> entityIdCollection, EntityType type) throws Exception {
        return null;
    }

    @Override
    public EntityWrapper getByEntityIdAndEntityType(int entityID, EntityType type) throws Exception {
        return null;
    }

    @Override
    public EntityWrapper getById(int entityWrapperId) throws Exception {
        return null;
    }

    /* UPDATE */

    /* DELETE */
    @Override
    public boolean deleteEntityWrapper(int entityWrapperID) throws Exception {
        return false;
    }

    /* Utils */
}
