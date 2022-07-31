package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
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
    Page<ProjectWorker> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    Optional<ProjectWorker> findByProjectWorkerIdAndStatusNotIn(
            long projectWorkerId, Collection<Status> statusCollection);
    List<ProjectWorker> findAllByProjectWorkerIdInAndStatusNotIn(
            Collection<Long> projectWorkerIdCollection, Collection<Status> statusCollection);
    /* Id & projectId & workerId */
    /** Check duplicate for Update */
    boolean existsByProjectIdAndWorkerIdAndProjectWorkerIdIsNotAndStatusNotIn(
            long projectId, long workerId, long projectWorkerId, Collection<Status> statusCollection);


    /* projectId */
    List<ProjectWorker> findAllByProjectIdAndStatusNotIn(
            long projectId, Collection<Status> statusCollection);
    Page<ProjectWorker> findAllByProjectIdAndStatusNotIn(
            long projectId, Collection<Status> statusCollection, Pageable paging);
    List<ProjectWorker> findAllByProjectIdInAndStatusNotIn(
            Collection<Long> projectIdCollection, Collection<Status> statusCollection);
    Page<ProjectWorker> findAllByProjectIdInAndStatusNotIn(
            Collection<Long> projectIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* workerId */
    List<ProjectWorker> findAllByWorkerIdAndStatusNotIn(
            long workerId, Collection<Status> statusCollection);
    Page<ProjectWorker> findAllByWorkerIdAndStatusNotIn(
            long workerId, Collection<Status> statusCollection, Pageable paging);
    List<ProjectWorker> findAllByWorkerIdInAndStatusNotIn(
            Collection<Long> workerIdCollection, Collection<Status> statusCollection);
    Page<ProjectWorker> findAllByWorkerIdInAndStatusNotIn(
            Collection<Long> workerIdCollection, Collection<Status> statusCollection, Pageable paging);
    /* projectId & workerId */
    /** Check duplicate for Create */
    boolean existsByProjectIdAndWorkerIdAndStatusNotIn(
            long projectId, long workerId, Collection<Status> statusCollection);
}
