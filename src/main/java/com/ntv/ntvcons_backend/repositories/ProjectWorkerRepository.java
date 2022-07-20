package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ProjectWorker;
import com.ntv.ntvcons_backend.entities.ProjectWorker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectWorkerRepository extends JpaRepository<ProjectWorker, Long> {
    Page<ProjectWorker> findAllByIsDeletedIsFalse(Pageable paging);


    /* Id */
    Optional<ProjectWorker> findByProjectWorkerIdAndIsDeletedIsFalse(long projectWorkerId);
    List<ProjectWorker> findAllByProjectWorkerIdInAndIsDeletedIsFalse(Collection<Long> projectWorkerIdCollection);
    /* Id & projectId & workerId */
    boolean existsByProjectIdAndWorkerIdAndProjectWorkerIdIsNotAndIsDeletedIsFalse
            (long projectId, long workerId, long projectWorkerId);


    /* projectId */
    List<ProjectWorker> findAllByProjectIdAndIsDeletedIsFalse(long projectId);
    Page<ProjectWorker> findAllByProjectIdAndIsDeletedIsFalse(long projectId, Pageable paging);
    List<ProjectWorker> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection);
    Page<ProjectWorker> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection, Pageable paging);


    /* workerId */
    List<ProjectWorker> findAllByWorkerIdAndIsDeletedIsFalse(long workerId);
    Page<ProjectWorker> findAllByWorkerIdAndIsDeletedIsFalse(long workerId, Pageable paging);
    List<ProjectWorker> findAllByWorkerIdInAndIsDeletedIsFalse(Collection<Long> workerIdCollection);
    Page<ProjectWorker> findAllByWorkerIdInAndIsDeletedIsFalse(Collection<Long> workerIdCollection, Pageable paging);
    /* projectId & workerId */
    boolean existsByProjectIdAndWorkerIdAndIsDeletedIsFalse(long projectId, long workerId);
}
