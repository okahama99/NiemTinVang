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
    List<EntityWrapper> findAllByStatusNotContains(String status);
    Page<EntityWrapper> findAllByStatusNotContains(String status, Pageable paging);


    /* Id */
    boolean existsByEntityWrapperIdAndStatusNotContains(long entityWrapperId, String status);
    Optional<EntityWrapper> findByEntityWrapperIdAndStatusNotContains(long entityWrapperId, String status);
    List<EntityWrapper> findAllByEntityWrapperIdInAndStatusNotContains(Collection<Long> entityWrapperIdCollection, String status);


    /* blueprintId */
    boolean existsByBlueprintIdAndStatusNotContains(long blueprintId, String status);
    Optional<EntityWrapper> findByBlueprintIdAndStatusNotContains(long blueprintId, String status);
    List<EntityWrapper> findAllByBlueprintIdInAndStatusNotContains(Collection<Long> blueprintIdCollection, String status);
    List<EntityWrapper> findAllByBlueprintIdNotNullAndStatusNotContains(String status);


    /* postId */
    boolean existsByPostIdAndStatusNotContains(long postId, String status);
    Optional<EntityWrapper> findByPostIdAndStatusNotContains(long postId, String status);
    List<EntityWrapper> findAllByPostIdInAndStatusNotContains(Collection<Long> postIdCollection, String status);
    List<EntityWrapper> findAllByPostIdNotNullAndStatusNotContains(String status);


    /* projectId */
    boolean existsByProjectIdAndStatusNotContains(long projectId, String status);
    Optional<EntityWrapper> findByProjectIdAndStatusNotContains(long projectId, String status);
    List<EntityWrapper> findAllByProjectIdInAndStatusNotContains(Collection<Long> projectIDCollection, String status);
    List<EntityWrapper> findAllByProjectIdNotNullAndStatusNotContains(String status);


    /* reportId */
    boolean existsByReportIdAndStatusNotContains(long reportId, String status);
    Optional<EntityWrapper> findByReportIdAndStatusNotContains(long reportId, String status);
    List<EntityWrapper> findAllByReportIdInAndStatusNotContains(Collection<Long> reportIdCollection, String status);
    List<EntityWrapper> findAllByReportIdNotNullAndStatusNotContains(String status);


    /* requestId */
    boolean existsByRequestIdAndStatusNotContains(long requestId, String status);
    Optional<EntityWrapper> findByRequestIdAndStatusNotContains(long requestId, String status);
    List<EntityWrapper> findAllByRequestIdInAndStatusNotContains(Collection<Long> requestIdCollection, String status);
    List<EntityWrapper> findAllByRequestIdNotNullAndStatusNotContains(String status);


    /* taskId */
    boolean existsByTaskIdAndStatusNotContains(long taskId, String status);
    Optional<EntityWrapper> findByTaskIdAndStatusNotContains(long taskId, String status);
    List<EntityWrapper> findAllByTaskIdInAndStatusNotContains(Collection<Long> taskIdCollection, String status);
    List<EntityWrapper> findAllByTaskIdNotNullAndStatusNotContains(String status);


    /* userId */
    boolean existsByUserIdAndStatusNotContains(long userId, String status);
    Optional<EntityWrapper> findByUserIdAndStatusNotContains(long userId, String status);
    List<EntityWrapper> findAllByUserIdInAndStatusNotContains(Collection<Long> userIdCollection, String status);
    List<EntityWrapper> findAllByUserIdNotNullAndStatusNotContains(String status);


    /* workerId */
    boolean existsByWorkerIdAndStatusNotContains(long workerId, String status);
    Optional<EntityWrapper> findByWorkerIdAndStatusNotContains(long workerId, String status);
    List<EntityWrapper> findAllByWorkerIdInAndStatusNotContains(Collection<Long> workerIdCollection, String status);
    List<EntityWrapper> findAllByWorkerIdNotNullAndStatusNotContains(String status);
}
