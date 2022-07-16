package com.ntv.ntvcons_backend.repositories;

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
    Page<EntityWrapper> findAllByIsDeletedIsFalse(Pageable paging);

    /* Id */
    boolean existsByEntityWrapperIdAndIsDeletedIsFalse(long entityWrapperId);
    Optional<EntityWrapper> findByEntityWrapperIdAndIsDeletedIsFalse(long entityWrapperId);
    List<EntityWrapper> findAllByEntityWrapperIdInAndIsDeletedIsFalse(Collection<Long> entityWrapperIdCollection);


    /* blueprintId */
    boolean existsByBlueprintIdAndIsDeletedIsFalse(long blueprintId);
    Optional<EntityWrapper> findByBlueprintIdAndIsDeletedIsFalse(long blueprintId);
    List<EntityWrapper> findAllByBlueprintIdInAndIsDeletedIsFalse(Collection<Long> blueprintIdCollection);


    /* postId */
    boolean existsByPostIdAndIsDeletedIsFalse(long postId);
    Optional<EntityWrapper> findByPostIdAndIsDeletedIsFalse(long postId);
    List<EntityWrapper> findAllByPostIdInAndIsDeletedIsFalse(Collection<Long> postIdCollection);


    /* projectId */
    boolean existsByProjectIdAndIsDeletedIsFalse(long projectId);
    Optional<EntityWrapper> findByProjectIdAndIsDeletedIsFalse(long projectId);
    List<EntityWrapper> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIDCollection);


    /* reportId */
    boolean existsByReportIdAndIsDeletedIsFalse(long reportId);
    Optional<EntityWrapper> findByReportIdAndIsDeletedIsFalse(long reportId);
    List<EntityWrapper> findAllByReportIdInAndIsDeletedIsFalse(Collection<Long> reportIdCollection);


    /* requestId */
    boolean existsByRequestIdAndIsDeletedIsFalse(long requestId);
    Optional<EntityWrapper> findByRequestIdAndIsDeletedIsFalse(long requestId);
    List<EntityWrapper> findAllByRequestIdInAndIsDeletedIsFalse(Collection<Long> requestIdCollection);


    /* taskId */
    boolean existsByTaskIdAndIsDeletedIsFalse(long taskId);
    Optional<EntityWrapper> findByTaskIdAndIsDeletedIsFalse(long taskId);
    List<EntityWrapper> findAllByTaskIdInAndIsDeletedIsFalse(Collection<Long> taskIdCollection);


    /* userId */
    boolean existsByUserIdAndIsDeletedIsFalse(long userId);
    Optional<EntityWrapper> findByUserIdAndIsDeletedIsFalse(long userId);
    List<EntityWrapper> findAllByUserIdInAndIsDeletedIsFalse(Collection<Long> userIdCollection);


    /* workerId */
    boolean existsByWorkerIdAndIsDeletedIsFalse(long workerId);
    Optional<EntityWrapper> findByWorkerIdAndIsDeletedIsFalse(long workerId);
    List<EntityWrapper> findAllByWorkerIdInAndIsDeletedIsFalse(Collection<Long> workerIdCollection);
}
