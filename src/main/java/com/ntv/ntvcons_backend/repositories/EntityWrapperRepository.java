package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.EntityWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntityWrapperRepository extends JpaRepository<EntityWrapper, Integer> {
    List<EntityWrapper> findAllByIsDeletedFalse();

    /* Id */
    Optional<EntityWrapper> findByEntityWrapperIdAndIsDeletedIsFalse(int entityWrapperId);
    List<EntityWrapper> findAllByEntityWrapperIdInAndIsDeletedIsFalse
            (Collection<Integer> entityWrapperIdCollection);


    /* projectId */
    Optional<EntityWrapper> findByProjectIdAndIsDeletedIsFalse(int projectId);
    List<EntityWrapper> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Integer> projectIDCollection);


    /* reportId */
    Optional<EntityWrapper> findByReportIdAndIsDeletedIsFalse(int reportId);
    List<EntityWrapper> findAllByReportIdInAndIsDeletedIsFalse(Collection<Integer> reportIdCollection);


    /* requestId */
    Optional<EntityWrapper> findByRequestIdAndIsDeletedIsFalse(int requestId);
    List<EntityWrapper> findAllByRequestIdInAndIsDeletedIsFalse(Collection<Integer> requestIdCollection);


    /* postId */
    Optional<EntityWrapper> findByPostIdAndIsDeletedIsFalse(int postId);
    List<EntityWrapper> findAllByPostIdInAndIsDeletedIsFalse(Collection<Integer> postIdCollection);


    /* userId */
    Optional<EntityWrapper> findByUserIdAndIsDeletedIsFalse(int userId);
    List<EntityWrapper> findAllByUserIdInAndIsDeletedIsFalse(Collection<Integer> userIdCollection);


    /* workerId */
    Optional<EntityWrapper> findByWorkerIdAndIsDeletedIsFalse(int workerId);
    List<EntityWrapper> findAllByWorkerIdInAndIsDeletedIsFalse(Collection<Integer> workerIdCollection);
}
