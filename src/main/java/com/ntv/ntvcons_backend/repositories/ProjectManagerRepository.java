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


    /* projectId */
    List<ProjectManager> findAllByProjectIdAndIsDeletedIsFalse(long projectId);
    List<ProjectManager> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection);


    /* managerId */
    List<ProjectManager> findAllByManagerIdAndIsDeletedIsFalse(long managerId);
    List<ProjectManager> findAllByManagerIdInAndIsDeletedIsFalse(Collection<Long> managerIdCollection);
}
