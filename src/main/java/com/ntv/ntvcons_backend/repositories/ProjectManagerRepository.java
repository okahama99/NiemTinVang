package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
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
    Page<ProjectManager> findAllByStatusNotIn
            (Collection<Status> statusCollection, Pageable paging);


    /* Id */
    Optional<ProjectManager> findByProjectManagerIdAndStatusNotIn
            (long projectManagerId, Collection<Status> statusCollection);
    List<ProjectManager> findAllByProjectManagerIdInAndStatusNotIn
            (Collection<Long> projectManagerIdCollection, Collection<Status> statusCollection);
    /* Id & projectId & managerId */
    /** Check duplicate for update */
    boolean existsByProjectIdAndManagerIdAndProjectManagerIdIsNotAndStatusNotIn
            (long projectId, long managerId, long projectManagerId, Collection<Status> statusCollection);


    /* projectId */
    List<ProjectManager> findAllByProjectIdAndStatusNotIn
            (long projectId, Collection<Status> statusCollection);
    Page<ProjectManager> findAllByProjectIdAndStatusNotIn
            (long projectId, Collection<Status> statusCollection, Pageable paging);
    List<ProjectManager> findAllByProjectIdInAndStatusNotIn
            (Collection<Long> projectIdCollection, Collection<Status> statusCollection);
    Page<ProjectManager> findAllByProjectIdInAndStatusNotIn
            (Collection<Long> projectIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* managerId */
    List<ProjectManager> findAllByManagerIdAndStatusNotIn
            (long managerId, Collection<Status> statusCollection);
    Page<ProjectManager> findAllByManagerIdAndStatusNotIn
            (long managerId, Collection<Status> statusCollection, Pageable paging);
    List<ProjectManager> findAllByManagerIdInAndStatusNotIn
            (Collection<Long> managerIdCollection, Collection<Status> statusCollection);
    Page<ProjectManager> findAllByManagerIdInAndStatusNotIn
            (Collection<Long> managerIdCollection, Collection<Status> statusCollection, Pageable paging);
    /* projectId & managerId */
    /** Check duplicate for Create */
    boolean existsByProjectIdAndManagerIdAndStatusNotIn
            (long projectId, long managerId, Collection<Status> statusCollection);
}
