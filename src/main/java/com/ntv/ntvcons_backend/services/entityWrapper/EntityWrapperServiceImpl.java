package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.EntityWrapper;
import com.ntv.ntvcons_backend.repositories.EntityWrapperRepository;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import com.ntv.ntvcons_backend.services.report.ReportService;
import com.ntv.ntvcons_backend.services.request.RequestService;
import com.ntv.ntvcons_backend.services.task.TaskService;
import com.ntv.ntvcons_backend.services.user.UserService;
import com.ntv.ntvcons_backend.services.worker.WorkerService;
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
    private BlueprintService blueprintService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ProjectService projectService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ReportService reportService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private RequestService requestService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private TaskService taskService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private WorkerService workerService;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;

    private final Status DELETED = Status.DELETED;

    /* CREATE */
    @Override
    public EntityWrapper createEntityWrapper(long entityId, EntityType type, long createdBy) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!userService.existsById(createdBy)) {
            errorMsg += "No User (CreatedBy) found with Id: '" + createdBy
                    + "'. Which violate constraint: FK_EntityWrapper_User_CreatedBy. ";
        }
        boolean isNotExists = false;
        switch (type) {
            case BLUEPRINT_ENTITY:
                if (!blueprintService.existsById(entityId))
                    isNotExists = true;
                break;

//            case POST_ENTITY:
//            if (!postService.existsById(entityId))
//                isNotExists = true;
//                break;

            case PROJECT_ENTITY:
                if (!projectService.existsById(entityId))
                    isNotExists = true;
                break;

            case REPORT_ENTITY:
                if (!reportService.existsById(entityId))
                    isNotExists = true;
                break;

            case REQUEST_ENTITY:
                if (!requestService.existsById(entityId))
                    isNotExists = true;
                break;

            case TASK_ENTITY:
                if (!taskService.existsById(entityId))
                    isNotExists = true;
                break;

            case USER_ENTITY:
                if (!userService.existsById(entityId))
                    isNotExists = true;
                break;

            case WORKER_ENTITY:
                if (!workerService.existsById(entityId))
                    isNotExists = true;
                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }
        if (isNotExists) {
            errorMsg += "No " + type.getEntityName() + " found with Id: '" + createdBy
                    + "'. Which violate constraint: "
                    + "FK_EntityWrapper_" + type.getEntityName() +  "_" + type.getEntityIdPCName() + ". ";
        }

        /* Check duplicate */
        boolean isDuplicated = false;
        switch (type) {
            case BLUEPRINT_ENTITY:
                if (entityWrapperRepository.existsByBlueprintIdAndStatusNot(entityId, DELETED))
                    isDuplicated = true;
                break;

            case POST_ENTITY:
                if (entityWrapperRepository.existsByPostIdAndStatusNot(entityId, DELETED))
                    isDuplicated = true;
                break;

            case PROJECT_ENTITY:
                if (entityWrapperRepository.existsByProjectIdAndStatusNot(entityId, DELETED))
                    isDuplicated = true;
                break;

            case REPORT_ENTITY:
                if (entityWrapperRepository.existsByReportIdAndStatusNot(entityId, DELETED))
                    isDuplicated = true;
                break;

            case REQUEST_ENTITY:
                if (entityWrapperRepository.existsByRequestIdAndStatusNot(entityId, DELETED))
                    isDuplicated = true;
                break;

            case TASK_ENTITY:
                if (entityWrapperRepository.existsByTaskIdAndStatusNot(entityId, DELETED))
                    isDuplicated = true;
                break;

            case USER_ENTITY:
                if (entityWrapperRepository.existsByUserIdAndStatusNot(entityId, DELETED))
                    isDuplicated = true;
                break;

            case WORKER_ENTITY:
                if (entityWrapperRepository.existsByWorkerIdAndStatusNot(entityId, DELETED))
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
                entityWrapperRepository.findAllByStatusNot(DELETED);

        if (entityWrapperList.isEmpty())
            return null;

        return entityWrapperList;
    }

    @Override
    public boolean existsById(long entityWrapperId) throws Exception {
        return entityWrapperRepository
                .existsByEntityWrapperIdAndStatusNot(entityWrapperId, DELETED);
    }
    @Override
    public EntityWrapper getById(long entityWrapperId) throws Exception {
        return entityWrapperRepository
                .findByEntityWrapperIdAndStatusNot(entityWrapperId, DELETED)
                .orElse(null);
    }

    @Override
    public List<EntityWrapper> getAllByEntityType(EntityType type) throws Exception {
        List<EntityWrapper> entityWrapperList = new ArrayList<>();

        switch (type) {
            case BLUEPRINT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByBlueprintIdNotNullAndStatusNot(DELETED);
                break;

            case POST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByPostIdNotNullAndStatusNot(DELETED);
                break;

            case PROJECT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByProjectIdNotNullAndStatusNot(DELETED);
                break;

            case REPORT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByReportIdNotNullAndStatusNot(DELETED);
                break;

            case REQUEST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByRequestIdNotNullAndStatusNot(DELETED);
                break;

            case TASK_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByTaskIdNotNullAndStatusNot(DELETED);
                break;

            case USER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByUserIdNotNullAndStatusNot(DELETED);
                break;

            case WORKER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByWorkerIdNotNullAndStatusNot(DELETED);
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
                                .existsByBlueprintIdAndStatusNot(entityID, DELETED);
                break;

            case POST_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByPostIdAndStatusNot(entityID, DELETED);
                break;

            case PROJECT_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByProjectIdAndStatusNot(entityID, DELETED);
                break;

            case REPORT_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByReportIdAndStatusNot(entityID, DELETED);
                break;

            case REQUEST_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByRequestIdAndStatusNot(entityID, DELETED);
                break;

            case TASK_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByTaskIdAndStatusNot(entityID, DELETED);
                break;

            case USER_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByUserIdAndStatusNot(entityID, DELETED);
                break;

            case WORKER_ENTITY:
                exists =
                        entityWrapperRepository
                                .existsByWorkerIdAndStatusNot(entityID, DELETED);
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
                                .findByBlueprintIdAndStatusNot(entityID, DELETED)
                                .orElse(null);
                break;

            case POST_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByPostIdAndStatusNot(entityID, DELETED)
                                .orElse(null);
                break;

            case PROJECT_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByProjectIdAndStatusNot(entityID, DELETED)
                                .orElse(null);
                break;

            case REPORT_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByReportIdAndStatusNot(entityID, DELETED)
                                .orElse(null);
                break;

            case REQUEST_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByRequestIdAndStatusNot(entityID, DELETED)
                                .orElse(null);
                break;

            case TASK_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByTaskIdAndStatusNot(entityID, DELETED)
                                .orElse(null);
                break;

            case USER_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByUserIdAndStatusNot(entityID, DELETED)
                                .orElse(null);
                break;

            case WORKER_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByWorkerIdAndStatusNot(entityID, DELETED)
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
                        entityWrapperRepository.findAllByBlueprintIdInAndStatusNot(entityIdCollection, DELETED);
                break;

            case POST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByPostIdInAndStatusNot(entityIdCollection, DELETED);
                break;

            case PROJECT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByProjectIdInAndStatusNot(entityIdCollection, DELETED);
                break;

            case REPORT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByReportIdInAndStatusNot(entityIdCollection, DELETED);
                break;

            case REQUEST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByRequestIdInAndStatusNot(entityIdCollection, DELETED);
                break;

            case TASK_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByTaskIdInAndStatusNot(entityIdCollection, DELETED);
                break;

            case USER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByUserIdInAndStatusNot(entityIdCollection, DELETED);
                break;

            case WORKER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByWorkerIdInAndStatusNot(entityIdCollection, DELETED);
                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        if (entityWrapperList.isEmpty())
            return null;

        return entityWrapperList;
    }

    @Override
    public Map<Long, Long> mapEntityWrapperIdEntityIdByEntityIdInAndEntityType(Collection<Long> entityIdCollection, EntityType type) throws Exception {
        List<EntityWrapper> entityWrapperList = getAllByEntityIdInAndEntityType(entityIdCollection, type);

        if (entityWrapperList == null)
            return new HashMap<>();

        Map<Long, Long> entityWrapperIdEntityIdMap = new HashMap<>();

        switch (type) {
            case BLUEPRINT_ENTITY:
                entityWrapperIdEntityIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(
                                        EntityWrapper::getEntityWrapperId,
                                        EntityWrapper::getBlueprintId));
                break;

            case POST_ENTITY:
                entityWrapperIdEntityIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(
                                        EntityWrapper::getEntityWrapperId,
                                        EntityWrapper::getPostId));
                break;

            case PROJECT_ENTITY:
                entityWrapperIdEntityIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(
                                        EntityWrapper::getEntityWrapperId,
                                        EntityWrapper::getProjectId));
                break;

            case REPORT_ENTITY:
                entityWrapperIdEntityIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(
                                        EntityWrapper::getEntityWrapperId,
                                        EntityWrapper::getReportId));
                break;

            case REQUEST_ENTITY:
                entityWrapperIdEntityIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(
                                        EntityWrapper::getEntityWrapperId,
                                        EntityWrapper::getRequestId));
                break;

            case TASK_ENTITY:
                entityWrapperIdEntityIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(
                                        EntityWrapper::getEntityWrapperId,
                                        EntityWrapper::getTaskId));

                break;

            case USER_ENTITY:
                entityWrapperIdEntityIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(
                                        EntityWrapper::getEntityWrapperId,
                                        EntityWrapper::getUserId));

                break;

            case WORKER_ENTITY:
                entityWrapperIdEntityIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(
                                        EntityWrapper::getEntityWrapperId,
                                        EntityWrapper::getWorkerId));

                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        return entityWrapperIdEntityIdMap;
    }

    /* UPDATE */

    /* DELETE */
    @Override
    public boolean deleteEntityWrapper(long entityWrapperID) throws Exception {
        EntityWrapper entityWrapper = getById(entityWrapperID);

        if (entityWrapper == null)
            return false;

        /* Delete all associated EFEWPairing */
        eFEWPairingService.deleteAllByEntityWrapperId(entityWrapper.getEntityWrapperId());

        entityWrapper.setStatus(Status.DELETED);
        entityWrapperRepository.saveAndFlush(entityWrapper);

        return true;
    }

    @Override
    public boolean deleteByEntityIdAndEntityType(long entityID, EntityType type) throws Exception {
        EntityWrapper entityWrapper = getByEntityIdAndEntityType(entityID, type);

        if (entityWrapper == null)
            return false;

        /* Delete all associated EFEWPairing */
        eFEWPairingService.deleteAllByEntityWrapperId(entityWrapper.getEntityWrapperId());

        entityWrapper.setStatus(Status.DELETED);
        entityWrapperRepository.saveAndFlush(entityWrapper);

        return true;
    }
    @Override
    public boolean deleteAllByEntityIdInAndEntityType(Collection<Long> entityIDCollection, EntityType type) throws Exception {
        List<EntityWrapper> entityWrapperList =
                getAllByEntityIdInAndEntityType(entityIDCollection, type);

        if (entityWrapperList == null)
            return false;

        Set<Long> entityWrapperIdSet = new HashSet<>();

        entityWrapperList = entityWrapperList.stream()
                .peek(entityWrapper -> {
                    entityWrapperIdSet.add(entityWrapper.getEntityWrapperId());

                    entityWrapper.setStatus(Status.DELETED);})
                .collect(Collectors.toList());

        /* Delete all associated EFEWPairing */
        eFEWPairingService.deleteAllByEntityWrapperIdIn(entityWrapperIdSet);

        entityWrapperRepository.saveAllAndFlush(entityWrapperList);

        return true;
    }
}
