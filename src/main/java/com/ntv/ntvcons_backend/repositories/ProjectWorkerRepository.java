package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ProjectWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectWorkerRepository extends JpaRepository<ProjectWorker, Integer> {
    List<ProjectWorker> findAllByIsDeletedFalse();


    /* Id */
    Optional<ProjectWorker> findByProjectWorkerIdAndIsDeletedIsFalse(int projectWorkerId);
    List<ProjectWorker> findAllByProjectWorkerIdInAndIsDeletedIsFalse(Collection<Integer> projectWorkerIdCollection);


    /* projectId */
    List<ProjectWorker> findAllByProjectIdAndIsDeletedIsFalse(int projectId);
    List<ProjectWorker> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Integer> projectIdCollection);


    /* managerId */
    List<ProjectWorker> findAllByWorkerIdAndIsDeletedIsFalse(int workerId);
    List<ProjectWorker> findAllByWorkerIdInAndIsDeletedIsFalse(Collection<Integer> workerIdCollection);
}
