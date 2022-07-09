package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ProjectManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectManagerRepository extends JpaRepository<ProjectManager, Long> {
    Page<ProjectManager> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<ProjectManager> findByProjectManagerIdAndIsDeletedIsFalse(long projectManagerId);
    List<ProjectManager> findAllByProjectManagerIdInAndIsDeletedIsFalse(Collection<Long> projectManagerIdCollection);
    /* Id & projectId & managerId */
    /** Check duplicate projectId & managerId for update */
    boolean existsByProjectIdAndManagerIdAndProjectManagerIdIsNotAndIsDeletedIsFalse
            (long projectId, long managerId, long projectManagerId);


    /* projectId */
    List<ProjectManager> findAllByProjectIdAndIsDeletedIsFalse(long projectId);
    Page<ProjectManager> findAllByProjectIdAndIsDeletedIsFalse(long projectId, Pageable paging);
    List<ProjectManager> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection);
    Page<ProjectManager> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection, Pageable paging);


    /* managerId */
    List<ProjectManager> findAllByManagerIdAndIsDeletedIsFalse(long managerId);
    Page<ProjectManager> findAllByManagerIdAndIsDeletedIsFalse(long managerId, Pageable paging);
    List<ProjectManager> findAllByManagerIdInAndIsDeletedIsFalse(Collection<Long> managerIdCollection);
    Page<ProjectManager> findAllByManagerIdInAndIsDeletedIsFalse(Collection<Long> managerIdCollection, Pageable paging);
    /* projectId & managerId */
    boolean existsByProjectIdAndManagerIdAndIsDeletedIsFalse(long projectId, long managerId);
}
