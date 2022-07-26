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
    List<EntityWrapper> findAllByStatusNot(Status status);
    Page<EntityWrapper> findAllByStatusNot(Status status, Pageable paging);


    /* Id */
    boolean existsByEntityWrapperIdAndStatusNot(long entityWrapperId, Status status);
    Optional<EntityWrapper> findByEntityWrapperIdAndStatusNot(long entityWrapperId, Status status);
    List<EntityWrapper> findAllByEntityWrapperIdInAndStatusNot(Collection<Long> entityWrapperIdCollection, Status status);


    /* blueprintId */
    boolean existsByBlueprintIdAndStatusNot(long blueprintId, Status status);
    Optional<EntityWrapper> findByBlueprintIdAndStatusNot(long blueprintId, Status status);
    List<EntityWrapper> findAllByBlueprintIdInAndStatusNot(Collection<Long> blueprintIdCollection, Status status);
    List<EntityWrapper> findAllByBlueprintIdNotNullAndStatusNot(Status status);


    /* postId */
    boolean existsByPostIdAndStatusNot(long postId, Status status);
    Optional<EntityWrapper> findByPostIdAndStatusNot(long postId, Status status);
    List<EntityWrapper> findAllByPostIdInAndStatusNot(Collection<Long> postIdCollection, Status status);
    List<EntityWrapper> findAllByPostIdNotNullAndStatusNot(Status status);


    /* projectId */
    boolean existsByProjectIdAndStatusNot(long projectId, Status status);
    Optional<EntityWrapper> findByProjectIdAndStatusNot(long projectId, Status status);
    List<EntityWrapper> findAllByProjectIdInAndStatusNot(Collection<Long> projectIDCollection, Status status);
    List<EntityWrapper> findAllByProjectIdNotNullAndStatusNot(Status status);


    /* reportId */
    boolean existsByReportIdAndStatusNot(long reportId, Status status);
    Optional<EntityWrapper> findByReportIdAndStatusNot(long reportId, Status status);
    List<EntityWrapper> findAllByReportIdInAndStatusNot(Collection<Long> reportIdCollection, Status status);
    List<EntityWrapper> findAllByReportIdNotNullAndStatusNot(Status status);


    /* requestId */
    boolean existsByRequestIdAndStatusNot(long requestId, Status status);
    Optional<EntityWrapper> findByRequestIdAndStatusNot(long requestId, Status status);
    List<EntityWrapper> findAllByRequestIdInAndStatusNot(Collection<Long> requestIdCollection, Status status);
    List<EntityWrapper> findAllByRequestIdNotNullAndStatusNot(Status status);


    /* taskId */
    boolean existsByTaskIdAndStatusNot(long taskId, Status status);
    Optional<EntityWrapper> findByTaskIdAndStatusNot(long taskId, Status status);
    List<EntityWrapper> findAllByTaskIdInAndStatusNot(Collection<Long> taskIdCollection, Status status);
    List<EntityWrapper> findAllByTaskIdNotNullAndStatusNot(Status status);


    /* userId */
    boolean existsByUserIdAndStatusNot(long userId, Status status);
    Optional<EntityWrapper> findByUserIdAndStatusNot(long userId, Status status);
    List<EntityWrapper> findAllByUserIdInAndStatusNot(Collection<Long> userIdCollection, Status status);
    List<EntityWrapper> findAllByUserIdNotNullAndStatusNot(Status status);


    /* workerId */
    boolean existsByWorkerIdAndStatusNot(long workerId, Status status);
    Optional<EntityWrapper> findByWorkerIdAndStatusNot(long workerId, Status status);
    List<EntityWrapper> findAllByWorkerIdInAndStatusNot(Collection<Long> workerIdCollection, Status status);
    List<EntityWrapper> findAllByWorkerIdNotNullAndStatusNot(Status status);
}
