package com.ntv.ntvcons_backend.repositories;

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
    Page<ProjectWorker> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<ProjectWorker> findByProjectWorkerIdAndIsDeletedIsFalse(long projectWorkerId);
    List<ProjectWorker> findAllByProjectWorkerIdInAndIsDeletedIsFalse(Collection<Long> projectWorkerIdCollection);


    /* projectId */
    List<ProjectWorker> findAllByProjectIdAndIsDeletedIsFalse(long projectId);
    List<ProjectWorker> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection);


    /* managerId */
    List<ProjectWorker> findAllByWorkerIdAndIsDeletedIsFalse(long workerId);
    List<ProjectWorker> findAllByWorkerIdInAndIsDeletedIsFalse(Collection<Long> workerIdCollection);
}
