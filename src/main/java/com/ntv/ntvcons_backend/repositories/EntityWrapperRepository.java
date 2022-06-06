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
    Page<EntityWrapper> findAllByIsDeletedIsFalse(Pageable pageable);

    /* Id */
    Optional<EntityWrapper> findByEntityWrapperIdAndIsDeletedIsFalse(long entityWrapperId);
    List<EntityWrapper> findAllByEntityWrapperIdInAndIsDeletedIsFalse
            (Collection<Long> entityWrapperIdCollection);


    /* projectId */
    Optional<EntityWrapper> findByProjectIdAndIsDeletedIsFalse(long projectId);
    List<EntityWrapper> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIDCollection);


    /* reportId */
    Optional<EntityWrapper> findByReportIdAndIsDeletedIsFalse(long reportId);
    List<EntityWrapper> findAllByReportIdInAndIsDeletedIsFalse(Collection<Long> reportIdCollection);


    /* requestId */
    Optional<EntityWrapper> findByRequestIdAndIsDeletedIsFalse(long requestId);
    List<EntityWrapper> findAllByRequestIdInAndIsDeletedIsFalse(Collection<Long> requestIdCollection);


    /* postId */
    Optional<EntityWrapper> findByPostIdAndIsDeletedIsFalse(long postId);
    List<EntityWrapper> findAllByPostIdInAndIsDeletedIsFalse(Collection<Long> postIdCollection);


    /* userId */
    Optional<EntityWrapper> findByUserIdAndIsDeletedIsFalse(long userId);
    List<EntityWrapper> findAllByUserIdInAndIsDeletedIsFalse(Collection<Long> userIdCollection);


    /* workerId */
    Optional<EntityWrapper> findByWorkerIdAndIsDeletedIsFalse(long workerId);
    List<EntityWrapper> findAllByWorkerIdInAndIsDeletedIsFalse(Collection<Long> workerIdCollection);
}
