package com.ntv.ntvcons_backend.services.entityWrapper;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.EntityWrapper;
import com.ntv.ntvcons_backend.repositories.EntityWrapperRepository;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.message.MessageService;
import com.ntv.ntvcons_backend.services.post.PostService;
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
    private PostService postService;
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
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private MessageService messageService;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public EntityWrapper createEntityWrapper(long entityId, EntityType type, Long createdBy) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (createdBy != null) {
            if (!userService.existsById(createdBy)) {
                errorMsg += "No User (CreatedBy) found with Id: '" + createdBy
                        + "'. Which violate constraint: FK_EntityWrapper_User_CreatedBy. ";
            }
        }

        boolean isNotExists = false;
        /* Check duplicate */
        boolean isDuplicated = false;

        switch (type) {
            case BLUEPRINT_ENTITY:
                if (!blueprintService.existsById(entityId))
                    isNotExists = true;
                if (entityWrapperRepository.existsByBlueprintIdAndStatusNotIn(entityId, N_D_S_STATUS_LIST))
                    isDuplicated = true;
                break;

            case POST_ENTITY:
            if (!postService.existsById(entityId))
                isNotExists = true;
            if (entityWrapperRepository.existsByPostIdAndStatusNotIn(entityId, N_D_S_STATUS_LIST))
                isDuplicated = true;
                break;

            case PROJECT_ENTITY:
                if (!projectService.existsById(entityId))
                    isNotExists = true;
                if (entityWrapperRepository.existsByProjectIdAndStatusNotIn(entityId, N_D_S_STATUS_LIST))
                    isDuplicated = true;
                break;

            case REPORT_ENTITY:
                if (!reportService.existsById(entityId))
                    isNotExists = true;
                if (entityWrapperRepository.existsByReportIdAndStatusNotIn(entityId, N_D_S_STATUS_LIST))
                    isDuplicated = true;
                break;

            case REQUEST_ENTITY:
                if (!requestService.existsById(entityId))
                    isNotExists = true;
                if (entityWrapperRepository.existsByRequestIdAndStatusNotIn(entityId, N_D_S_STATUS_LIST))
                    isDuplicated = true;
                break;

            case TASK_ENTITY:
                if (!taskService.existsById(entityId))
                    isNotExists = true;
                if (entityWrapperRepository.existsByTaskIdAndStatusNotIn(entityId, N_D_S_STATUS_LIST))
                    isDuplicated = true;
                break;

            case USER_ENTITY:
                if (!userService.existsById(entityId))
                    isNotExists = true;
                if (entityWrapperRepository.existsByUserIdAndStatusNotIn(entityId, N_D_S_STATUS_LIST))
                    isDuplicated = true;
                break;

            case WORKER_ENTITY:
                if (!workerService.existsById(entityId))
                    isNotExists = true;
                if (entityWrapperRepository.existsByWorkerIdAndStatusNotIn(entityId, N_D_S_STATUS_LIST))
                    isDuplicated = true;
                break;

            case MESSAGE_ENTITY:
                if (!messageService.existsById(entityId))
                    isNotExists = true;
                if (entityWrapperRepository.existsByMessageIdAndStatusNotIn(entityId, N_D_S_STATUS_LIST))
                    isDuplicated = true;
                break;

            default:
                throw new IllegalArgumentException("Invalid EntityType used, no such type exists!");
        }

        if (isNotExists) {
            errorMsg += "No " + type.getEntityName() + " found with Id: '" + createdBy
                    + "'. Which violate constraint: "
                    + "FK_EntityWrapper_" + type.getEntityName() +  "_" + type.getEntityIdPCName() + ". ";
        }

        if (isDuplicated)
            errorMsg += "Already exists another EntityWrapper with "
                    + type.getEntityIdCCName() + ": '" + entityId + "'. ";

        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        EntityWrapper newEntityWrapper = new EntityWrapper(entityId, type);
        newEntityWrapper.setStatus(Status.ACTIVE);
        newEntityWrapper.setCreatedBy(createdBy);
        newEntityWrapper.setCreatedAt(LocalDateTime.now());

        return entityWrapperRepository.saveAndFlush(newEntityWrapper);
    }

    /* READ */
    @Override
    public List<EntityWrapper> getAll() throws Exception {
        List<EntityWrapper> entityWrapperList =
                entityWrapperRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST);

        if (entityWrapperList.isEmpty())
            return null;

        return entityWrapperList;
    }

    @Override
    public boolean existsById(long entityWrapperId) throws Exception {
        return entityWrapperRepository
                .existsByEntityWrapperIdAndStatusNotIn(entityWrapperId, N_D_S_STATUS_LIST);
    }
    @Override
    public EntityWrapper getById(long entityWrapperId) throws Exception {
        return entityWrapperRepository
                .findByEntityWrapperIdAndStatusNotIn(entityWrapperId, N_D_S_STATUS_LIST)
                .orElse(null);
    }

    @Override
    public List<EntityWrapper> getAllByEntityType(EntityType type) throws Exception {
        List<EntityWrapper> entityWrapperList = new ArrayList<>();

        switch (type) {
            case BLUEPRINT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByBlueprintIdNotNullAndStatusNotIn(N_D_S_STATUS_LIST);
                break;

            case POST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByPostIdNotNullAndStatusNotIn(N_D_S_STATUS_LIST);
                break;

            case PROJECT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByProjectIdNotNullAndStatusNotIn(N_D_S_STATUS_LIST);
                break;

            case REPORT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByReportIdNotNullAndStatusNotIn(N_D_S_STATUS_LIST);
                break;

            case REQUEST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByRequestIdNotNullAndStatusNotIn(N_D_S_STATUS_LIST);
                break;

            case TASK_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByTaskIdNotNullAndStatusNotIn(N_D_S_STATUS_LIST);
                break;

            case USER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByUserIdNotNullAndStatusNotIn(N_D_S_STATUS_LIST);
                break;

            case WORKER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByWorkerIdNotNullAndStatusNotIn(N_D_S_STATUS_LIST);
                break;

            case MESSAGE_ENTITY:
                entityWrapperList =
                        entityWrapperRepository.findAllByMessageIdNotNullAndStatusNotIn(N_D_S_STATUS_LIST);
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
                exists = entityWrapperRepository
                        .existsByBlueprintIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST);
                break;

            case POST_ENTITY:
                exists = entityWrapperRepository
                        .existsByPostIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST);
                break;

            case PROJECT_ENTITY:
                exists = entityWrapperRepository
                        .existsByProjectIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST);
                break;

            case REPORT_ENTITY:
                exists = entityWrapperRepository
                        .existsByReportIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST);
                break;

            case REQUEST_ENTITY:
                exists = entityWrapperRepository
                        .existsByRequestIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST);
                break;

            case TASK_ENTITY:
                exists = entityWrapperRepository
                        .existsByTaskIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST);
                break;

            case USER_ENTITY:
                exists = entityWrapperRepository
                        .existsByUserIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST);
                break;

            case WORKER_ENTITY:
                exists = entityWrapperRepository
                        .existsByWorkerIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST);
                break;

            case MESSAGE_ENTITY:
                exists = entityWrapperRepository
                        .existsByMessageIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST);
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
                                .findByBlueprintIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST)
                                .orElse(null);
                break;

            case POST_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByPostIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST)
                                .orElse(null);
                break;

            case PROJECT_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByProjectIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST)
                                .orElse(null);
                break;

            case REPORT_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByReportIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST)
                                .orElse(null);
                break;

            case REQUEST_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByRequestIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST)
                                .orElse(null);
                break;

            case TASK_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByTaskIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST)
                                .orElse(null);
                break;

            case USER_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByUserIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST)
                                .orElse(null);
                break;

            case WORKER_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByWorkerIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST)
                                .orElse(null);
                break;

            case MESSAGE_ENTITY:
                entityWrapper =
                        entityWrapperRepository
                                .findByMessageIdAndStatusNotIn(entityID, N_D_S_STATUS_LIST)
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
                        entityWrapperRepository
                                .findAllByBlueprintIdInAndStatusNotIn(entityIdCollection, N_D_S_STATUS_LIST);
                break;

            case POST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository
                                .findAllByPostIdInAndStatusNotIn(entityIdCollection, N_D_S_STATUS_LIST);
                break;

            case PROJECT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository
                                .findAllByProjectIdInAndStatusNotIn(entityIdCollection, N_D_S_STATUS_LIST);
                break;

            case REPORT_ENTITY:
                entityWrapperList =
                        entityWrapperRepository
                                .findAllByReportIdInAndStatusNotIn(entityIdCollection, N_D_S_STATUS_LIST);
                break;

            case REQUEST_ENTITY:
                entityWrapperList =
                        entityWrapperRepository
                                .findAllByRequestIdInAndStatusNotIn(entityIdCollection, N_D_S_STATUS_LIST);
                break;

            case TASK_ENTITY:
                entityWrapperList =
                        entityWrapperRepository
                                .findAllByTaskIdInAndStatusNotIn(entityIdCollection, N_D_S_STATUS_LIST);
                break;

            case USER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository
                                .findAllByUserIdInAndStatusNotIn(entityIdCollection, N_D_S_STATUS_LIST);
                break;

            case WORKER_ENTITY:
                entityWrapperList =
                        entityWrapperRepository
                                .findAllByWorkerIdInAndStatusNotIn(entityIdCollection, N_D_S_STATUS_LIST);
                break;

            case MESSAGE_ENTITY:
                entityWrapperList =
                        entityWrapperRepository
                                .findAllByMessageIdInAndStatusNotIn(entityIdCollection, N_D_S_STATUS_LIST);
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

            case MESSAGE_ENTITY:
                entityWrapperIdEntityIdMap =
                        entityWrapperList.stream()
                                .collect(Collectors.toMap(
                                        EntityWrapper::getEntityWrapperId,
                                        EntityWrapper::getMessageId));

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
