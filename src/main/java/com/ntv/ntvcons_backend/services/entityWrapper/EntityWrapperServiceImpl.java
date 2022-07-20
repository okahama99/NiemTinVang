package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.entities.EntityWrapper;
import com.ntv.ntvcons_backend.repositories.EntityWrapperRepository;
import com.ntv.ntvcons_backend.services.BaseService;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintServiceImpl;
import com.ntv.ntvcons_backend.services.post.PostServiceImpl;
import com.ntv.ntvcons_backend.services.project.ProjectServiceImpl;
import com.ntv.ntvcons_backend.services.report.ReportServiceImpl;
import com.ntv.ntvcons_backend.services.request.RequestServiceImpl;
import com.ntv.ntvcons_backend.services.task.TaskServiceImpl;
import com.ntv.ntvcons_backend.services.user.UserService;
import com.ntv.ntvcons_backend.services.user.UserServiceImpl;
import com.ntv.ntvcons_backend.services.worker.WorkerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EntityWrapperServiceImpl implements EntityWrapperService {
    @Autowired
    private EntityWrapperRepository entityWrapperRepository;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    /* CREATE */
    @Override
    public EntityWrapper createEntityWrapper(long entityId, EntityType type, long createdBy) throws Exception {
        String errorMsg = "";

        BaseService entityService = getServiceByEntityType(type);
        if (entityService == null)
            throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");

        /* Check FK */
        if (!userService.existsById(createdBy)) {
            errorMsg += "No User (CreatedBy) found with Id: '" + createdBy
                    + "'. Which violate constraint: FK_EntityWrapper_User_CreatedBy. ";
        }
        if (!entityService.existsById(entityId)) {
            errorMsg += "No " + type.EntityName + " found with Id: '" + createdBy
                    + "'. Which violate constraint: "
                    + "FK_EntityWrapper_" + type.EntityName +  "_" + type.EntityIdPCName + ". ";
        }

        /* Check duplicate */
        boolean isDuplicated = false;
        switch (type) {
            case BLUEPRINT_ENTITY:
                if (entityWrapperRepository.existsByBlueprintIdAndIsDeletedIsFalse(entityId))
                    isDuplicated = true;
                break;

            case POST_ENTITY:
                if (entityWrapperRepository.existsByPostIdAndIsDeletedIsFalse(entityId))
                    isDuplicated = true;
                break;

            case PROJECT_ENTITY:
                if (entityWrapperRepository.existsByProjectIdAndIsDeletedIsFalse(entityId))
                    isDuplicated = true;
                break;

            case REPORT_ENTITY:
                if (entityWrapperRepository.existsByReportIdAndIsDeletedIsFalse(entityId))
                    isDuplicated = true;
                break;

            case REQUEST_ENTITY:
                if (entityWrapperRepository.existsByRequestIdAndIsDeletedIsFalse(entityId))
                    isDuplicated = true;
                break;

            case TASK_ENTITY:
                if (entityWrapperRepository.existsByTaskIdAndIsDeletedIsFalse(entityId))
                    isDuplicated = true;
                break;

            case USER_ENTITY:
                if (entityWrapperRepository.existsByUserIdAndIsDeletedIsFalse(entityId))
                    isDuplicated = true;
                break;

            case WORKER_ENTITY:
                if (entityWrapperRepository.existsByWorkerIdAndIsDeletedIsFalse(entityId))
                    isDuplicated = true;
                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        if (isDuplicated)
            errorMsg += "Already exists another EntityWrapper with "
                    + type.EntityIdCCName + ": '" + entityId + "'. ";

        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        EntityWrapper newEntityWrapper = new EntityWrapper(entityId, type);
        newEntityWrapper.setCreatedBy(createdBy);
        newEntityWrapper.setCreatedAt(LocalDateTime.now());

        return entityWrapperRepository.saveAndFlush(newEntityWrapper);
    }

    /* READ */
    @Override
    public List<EntityWrapper> getAll() throws Exception {
        List<EntityWrapper> entityWrapperList =
                entityWrapperRepository.findAllByIsDeletedIsFalse();

        if (entityWrapperList.isEmpty())
            return null;

        return entityWrapperList;
    }

    @Override
    public EntityWrapper getById(long entityWrapperId) throws Exception {
        return entityWrapperRepository
                .findByEntityWrapperIdAndIsDeletedIsFalse(entityWrapperId)
                .orElse(null);
    }

    @Override
    public List<EntityWrapper> getAllByEntityType(EntityType type) throws Exception {
        List<EntityWrapper> entityWrapperList = new ArrayList<>();

        switch (type) {
            case BLUEPRINT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByBlueprintIdNotNullAndIsDeletedIsFalse();
                break;

            case POST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByPostIdNotNullAndIsDeletedIsFalse();
                break;

            case PROJECT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByProjectIdNotNullAndIsDeletedIsFalse();
                break;

            case REPORT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByReportIdNotNullAndIsDeletedIsFalse();
                break;

            case REQUEST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByRequestIdNotNullAndIsDeletedIsFalse();
                break;

            case TASK_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByTaskIdNotNullAndIsDeletedIsFalse();
                break;

            case USER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByUserIdNotNullAndIsDeletedIsFalse();
                break;

            case WORKER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByWorkerIdNotNullAndIsDeletedIsFalse();
                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        if (entityWrapperList.isEmpty())
            return null;

        return entityWrapperList;
    }

    @Override
    public EntityWrapper getByEntityIdAndEntityType(long entityID, EntityType type) throws Exception {
        EntityWrapper entityWrapper = null;

        switch (type) {
            case BLUEPRINT_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByBlueprintIdAndIsDeletedIsFalse(entityID)
                                .orElse(null);
                break;

            case POST_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByPostIdAndIsDeletedIsFalse(entityID)
                                .orElse(null);
                break;

            case PROJECT_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByProjectIdAndIsDeletedIsFalse(entityID)
                                .orElse(null);
                break;

            case REPORT_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByReportIdAndIsDeletedIsFalse(entityID)
                                .orElse(null);
                break;

            case REQUEST_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByRequestIdAndIsDeletedIsFalse(entityID)
                                .orElse(null);
                break;

            case TASK_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByTaskIdAndIsDeletedIsFalse(entityID)
                                .orElse(null);
                break;

            case USER_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByUserIdAndIsDeletedIsFalse(entityID)
                                .orElse(null);
                break;

            case WORKER_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByWorkerIdAndIsDeletedIsFalse(entityID)
                                .orElse(null);
                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        return entityWrapper;
    }

    @Override
    public List<EntityWrapper> getAllByEntityIdInAndEntityType(Collection<Long> entityIdCollection, EntityType type) throws Exception {
        List<EntityWrapper> entityWrapperList = new ArrayList<>();

        switch (type) {
            case BLUEPRINT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByBlueprintIdInAndIsDeletedIsFalse(entityIdCollection);
                break;

            case POST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByPostIdInAndIsDeletedIsFalse(entityIdCollection);
                break;

            case PROJECT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByProjectIdInAndIsDeletedIsFalse(entityIdCollection);
                break;

            case REPORT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByReportIdInAndIsDeletedIsFalse(entityIdCollection);
                break;

            case REQUEST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByRequestIdInAndIsDeletedIsFalse(entityIdCollection);
                break;

            case TASK_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByTaskIdInAndIsDeletedIsFalse(entityIdCollection);
                break;

            case USER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByUserIdInAndIsDeletedIsFalse(entityIdCollection);
                break;

            case WORKER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByWorkerIdInAndIsDeletedIsFalse(entityIdCollection);
                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        if (entityWrapperList.isEmpty())
            return null;

        return entityWrapperList;
    }

    @Override
    public Map<Long, Long> mapEntityIdEntityWrapperIdByEntityIdInAndEntityType(Collection<Long> entityIdCollection, EntityType type) throws Exception {
        List<EntityWrapper> entityWrapperList = getAllByEntityIdInAndEntityType(entityIdCollection, type);

        if (entityWrapperList == null)
            return new HashMap<>();

        Map<Long, Long> entityIdEntityWrapperIdMap = new HashMap<>();

        switch (type) {
            case BLUEPRINT_ENTITY:
                entityIdEntityWrapperIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(EntityWrapper::getBlueprintId, EntityWrapper::getEntityWrapperId));
                break;

            case POST_ENTITY:
                entityIdEntityWrapperIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(EntityWrapper::getPostId, EntityWrapper::getEntityWrapperId));
                break;

            case PROJECT_ENTITY:
                entityIdEntityWrapperIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(EntityWrapper::getProjectId, EntityWrapper::getEntityWrapperId));
                break;

            case REPORT_ENTITY:
                entityIdEntityWrapperIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(EntityWrapper::getReportId, EntityWrapper::getEntityWrapperId));
                break;

            case REQUEST_ENTITY:
                entityIdEntityWrapperIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(EntityWrapper::getRequestId, EntityWrapper::getEntityWrapperId));
                break;

            case TASK_ENTITY:
                entityIdEntityWrapperIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(EntityWrapper::getTaskId, EntityWrapper::getEntityWrapperId));

                break;

            case USER_ENTITY:
                entityIdEntityWrapperIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(EntityWrapper::getUserId, EntityWrapper::getEntityWrapperId));

                break;

            case WORKER_ENTITY:
                entityIdEntityWrapperIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(EntityWrapper::getWorkerId, EntityWrapper::getEntityWrapperId));

                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        return entityIdEntityWrapperIdMap;
    }

    /* UPDATE */

    /* DELETE */
    @Override
    public boolean deleteEntityWrapper(long entityWrapperID) throws Exception {
        EntityWrapper entityWrapper = getById(entityWrapperID);

        if (entityWrapper == null)
            return false;

        entityWrapper.setIsDeleted(true);
        entityWrapperRepository.saveAndFlush(entityWrapper);

        return true;
    }

    @Override
    public boolean deleteByEntityIdAndEntityType(long entityWrapperID, EntityType type) throws Exception {
        EntityWrapper entityWrapper = getByEntityIdAndEntityType(entityWrapperID, type);

        if (entityWrapper == null)
            return false;

        entityWrapper.setIsDeleted(true);
        entityWrapperRepository.saveAndFlush(entityWrapper);

        return true;
    }

    /* Utils */
    private BaseService getServiceByEntityType(EntityType type) throws Exception {
        BaseService entityService = null;

        switch (type) {
            case BLUEPRINT_ENTITY:
                entityService = new BlueprintServiceImpl();
                break;

//            case POST_ENTITY:
//                entityService = new PostServiceImpl();
//                break;

            case PROJECT_ENTITY:
                entityService = new ProjectServiceImpl();
                break;

            case REPORT_ENTITY:
                entityService = new ReportServiceImpl();
                break;

            case REQUEST_ENTITY:
                entityService = new RequestServiceImpl();
                break;

            case TASK_ENTITY:
                entityService = new TaskServiceImpl();
                break;

            case USER_ENTITY:
                entityService = new UserServiceImpl();
                break;

            case WORKER_ENTITY:
                entityService = new WorkerServiceImpl();
                break;


            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        return entityService;
    }
}
