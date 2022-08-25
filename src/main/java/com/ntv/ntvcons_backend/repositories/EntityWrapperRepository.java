package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.EntityWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntityWrapperRepository extends JpaRepository<EntityWrapper, Long> {
    List<EntityWrapper> findAllByStatusNotIn(
            Collection<Status> statusCollection);
    Page<EntityWrapper> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByEntityWrapperIdAndStatusNotIn(
            long entityWrapperId, Collection<Status> statusCollection);
    Optional<EntityWrapper> findByEntityWrapperIdAndStatusNotIn(
            long entityWrapperId, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByEntityWrapperIdInAndStatusNotIn(
            Collection<Long> entityWrapperIdCollection, Collection<Status> statusCollection);


    /* blueprintId */
    /** Check duplicate for Create */
    boolean existsByBlueprintIdAndStatusNotIn(
            long blueprintId, Collection<Status> statusCollection);
    Optional<EntityWrapper> findByBlueprintIdAndStatusNotIn(
            long blueprintId, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByBlueprintIdInAndStatusNotIn(
            Collection<Long> blueprintIdCollection, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByBlueprintIdNotNullAndStatusNotIn(
            Collection<Status> statusCollection);


    /* postId */
    /** Check duplicate for Create */
    boolean existsByPostIdAndStatusNotIn(
            long postId, Collection<Status> statusCollection);
    Optional<EntityWrapper> findByPostIdAndStatusNotIn(
            long postId, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByPostIdInAndStatusNotIn(
            Collection<Long> postIdCollection, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByPostIdNotNullAndStatusNotIn(
            Collection<Status> statusCollection);


    /* projectId */
    /** Check duplicate for Create */
    boolean existsByProjectIdAndStatusNotIn(
            long projectId, Collection<Status> statusCollection);
    Optional<EntityWrapper> findByProjectIdAndStatusNotIn(
            long projectId, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByProjectIdInAndStatusNotIn(
            Collection<Long> projectIDCollection, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByProjectIdNotNullAndStatusNotIn(
            Collection<Status> statusCollection);


    /* reportId */
    /** Check duplicate for Create */
    boolean existsByReportIdAndStatusNotIn(
            long reportId, Collection<Status> statusCollection);
    Optional<EntityWrapper> findByReportIdAndStatusNotIn(
            long reportId, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByReportIdInAndStatusNotIn(
            Collection<Long> reportIdCollection, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByReportIdNotNullAndStatusNotIn(
            Collection<Status> statusCollection);


    /* requestId */
    /** Check duplicate for Create */
    boolean existsByRequestIdAndStatusNotIn(
            long requestId, Collection<Status> statusCollection);
    Optional<EntityWrapper> findByRequestIdAndStatusNotIn(
            long requestId, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByRequestIdInAndStatusNotIn(
            Collection<Long> requestIdCollection, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByRequestIdNotNullAndStatusNotIn(
            Collection<Status> statusCollection);


    /* taskId */
    /** Check duplicate for Create */
    boolean existsByTaskIdAndStatusNotIn(
            long taskId, Collection<Status> statusCollection);
    Optional<EntityWrapper> findByTaskIdAndStatusNotIn(
            long taskId, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByTaskIdInAndStatusNotIn(
            Collection<Long> taskIdCollection, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByTaskIdNotNullAndStatusNotIn(
            Collection<Status> statusCollection);


    /* userId */
    /** Check duplicate for Create */
    boolean existsByUserIdAndStatusNotIn(
            long userId, Collection<Status> statusCollection);
    Optional<EntityWrapper> findByUserIdAndStatusNotIn(
            long userId, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByUserIdInAndStatusNotIn(
            Collection<Long> userIdCollection, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByUserIdNotNullAndStatusNotIn(
            Collection<Status> statusCollection);


    /* workerId */
    /** Check duplicate for Create */
    boolean existsByWorkerIdAndStatusNotIn(
            long workerId, Collection<Status> statusCollection);
    Optional<EntityWrapper> findByWorkerIdAndStatusNotIn(
            long workerId, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByWorkerIdInAndStatusNotIn(
            Collection<Long> workerIdCollection, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByWorkerIdNotNullAndStatusNotIn(
            Collection<Status> statusCollection);


    /* messageId */
    /** Check duplicate for Create */
    boolean existsByMessageIdAndStatusNotIn(
            long messageId, Collection<Status> statusCollection);
    Optional<EntityWrapper> findByMessageIdAndStatusNotIn(
            long messageId, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByMessageIdInAndStatusNotIn(
            Collection<Long> messageIdCollection, Collection<Status> statusCollection);
    List<EntityWrapper> findAllByMessageIdNotNullAndStatusNotIn(
            Collection<Status> statusCollection);

}
