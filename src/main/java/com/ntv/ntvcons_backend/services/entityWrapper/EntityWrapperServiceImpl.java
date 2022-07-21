package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.Status;
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

    private final String DELETED = Status.DELETED.getStringValue();

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
            errorMsg += "No " + type.getEntityName() + " found with Id: '" + createdBy
                    + "'. Which violate constraint: "
                    + "FK_EntityWrapper_" + type.getEntityName() +  "_" + type.getEntityIdPCName() + ". ";
        }

        /* Check duplicate */
        boolean isDuplicated = false;
        switch (type) {
            case BLUEPRINT_ENTITY:
                if (entityWrapperRepository.existsByBlueprintIdAndStatusNotContains(entityId, DELETED))
                    isDuplicated = true;
                break;

            case POST_ENTITY:
                if (entityWrapperRepository.existsByPostIdAndStatusNotContains(entityId, DELETED))
                    isDuplicated = true;
                break;

            case PROJECT_ENTITY:
                if (entityWrapperRepository.existsByProjectIdAndStatusNotContains(entityId, DELETED))
                    isDuplicated = true;
                break;

            case REPORT_ENTITY:
                if (entityWrapperRepository.existsByReportIdAndStatusNotContains(entityId, DELETED))
                    isDuplicated = true;
                break;

            case REQUEST_ENTITY:
                if (entityWrapperRepository.existsByRequestIdAndStatusNotContains(entityId, DELETED))
                    isDuplicated = true;
                break;

            case TASK_ENTITY:
                if (entityWrapperRepository.existsByTaskIdAndStatusNotContains(entityId, DELETED))
                    isDuplicated = true;
                break;

            case USER_ENTITY:
                if (entityWrapperRepository.existsByUserIdAndStatusNotContains(entityId, DELETED))
                    isDuplicated = true;
                break;

            case WORKER_ENTITY:
                if (entityWrapperRepository.existsByWorkerIdAndStatusNotContains(entityId, DELETED))
                    isDuplicated = true;
                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        if (isDuplicated)
            errorMsg += "Already exists another EntityWrapper with "
                    + type.getEntityIdCCName() + ": '" + entityId + "'. ";

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
                entityWrapperRepository.findAllByStatusNotContains(DELETED);

        if (entityWrapperList.isEmpty())
            return null;

        return entityWrapperList;
    }

    @Override
    public boolean existsById(long entityWrapperId) throws Exception {
        return entityWrapperRepository
                .existsByEntityWrapperIdAndStatusNotContains(entityWrapperId, DELETED);
    }
    @Override
    public EntityWrapper getById(long entityWrapperId) throws Exception {
        return entityWrapperRepository
                .findByEntityWrapperIdAndStatusNotContains(entityWrapperId, DELETED)
                .orElse(null);
    }

    @Override
    public List<EntityWrapper> getAllByEntityType(EntityType type) throws Exception {
        List<EntityWrapper> entityWrapperList = new ArrayList<>();

        switch (type) {
            case BLUEPRINT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByBlueprintIdNotNullAndStatusNotContains(DELETED);
                break;

            case POST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByPostIdNotNullAndStatusNotContains(DELETED);
                break;

            case PROJECT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByProjectIdNotNullAndStatusNotContains(DELETED);
                break;

            case REPORT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByReportIdNotNullAndStatusNotContains(DELETED);
                break;

            case REQUEST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByRequestIdNotNullAndStatusNotContains(DELETED);
                break;

            case TASK_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByTaskIdNotNullAndStatusNotContains(DELETED);
                break;

            case USER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByUserIdNotNullAndStatusNotContains(DELETED);
                break;

            case WORKER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByWorkerIdNotNullAndStatusNotContains(DELETED);
                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        if (entityWrapperList.isEmpty())
            return null;

        return entityWrapperList;
    }

    @Override
    public boolean existsByEntityIdAndEntityType(long entityID, EntityType type) throws Exception {
        boolean exists = false;

        switch (type) {
            case BLUEPRINT_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByBlueprintIdAndStatusNotContains(entityID, DELETED);
                break;

            case POST_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByPostIdAndStatusNotContains(entityID, DELETED);
                break;

            case PROJECT_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByProjectIdAndStatusNotContains(entityID, DELETED);
                break;

            case REPORT_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByReportIdAndStatusNotContains(entityID, DELETED);
                break;

            case REQUEST_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByRequestIdAndStatusNotContains(entityID, DELETED);
                break;

            case TASK_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByTaskIdAndStatusNotContains(entityID, DELETED);
                break;

            case USER_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByUserIdAndStatusNotContains(entityID, DELETED);
                break;

            case WORKER_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByWorkerIdAndStatusNotContains(entityID, DELETED);
                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        return exists;
    }
    @Override
    public EntityWrapper getByEntityIdAndEntityType(long entityID, EntityType type) throws Exception {
        EntityWrapper entityWrapper = null;

        switch (type) {
            case BLUEPRINT_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByBlueprintIdAndStatusNotContains(entityID, DELETED)
                                .orElse(null);
                break;

            case POST_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByPostIdAndStatusNotContains(entityID, DELETED)
                                .orElse(null);
                break;

            case PROJECT_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByProjectIdAndStatusNotContains(entityID, DELETED)
                                .orElse(null);
                break;

            case REPORT_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByReportIdAndStatusNotContains(entityID, DELETED)
                                .orElse(null);
                break;

            case REQUEST_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByRequestIdAndStatusNotContains(entityID, DELETED)
                                .orElse(null);
                break;

            case TASK_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByTaskIdAndStatusNotContains(entityID, DELETED)
                                .orElse(null);
                break;

            case USER_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByUserIdAndStatusNotContains(entityID, DELETED)
                                .orElse(null);
                break;

            case WORKER_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByWorkerIdAndStatusNotContains(entityID, DELETED)
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
                        entityWrapperRepository.findAllByBlueprintIdInAndStatusNotContains(entityIdCollection, DELETED);
                break;

            case POST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByPostIdInAndStatusNotContains(entityIdCollection, DELETED);
                break;

            case PROJECT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByProjectIdInAndStatusNotContains(entityIdCollection, DELETED);
                break;

            case REPORT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByReportIdInAndStatusNotContains(entityIdCollection, DELETED);
                break;

            case REQUEST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByRequestIdInAndStatusNotContains(entityIdCollection, DELETED);
                break;

            case TASK_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByTaskIdInAndStatusNotContains(entityIdCollection, DELETED);
                break;

            case USER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByUserIdInAndStatusNotContains(entityIdCollection, DELETED);
                break;

            case WORKER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByWorkerIdInAndStatusNotContains(entityIdCollection, DELETED);
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

        entityWrapper.setStatus(Status.DELETED);
        entityWrapperRepository.saveAndFlush(entityWrapper);

        return true;
    }

    @Override
    public boolean deleteByEntityIdAndEntityType(long entityWrapperID, EntityType type) throws Exception {
        EntityWrapper entityWrapper = getByEntityIdAndEntityType(entityWrapperID, type);

        if (entityWrapper == null)
            return false;

        entityWrapper.setStatus(Status.DELETED);
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

            case POST_ENTITY:
                entityService = new PostServiceImpl();
                break;

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
