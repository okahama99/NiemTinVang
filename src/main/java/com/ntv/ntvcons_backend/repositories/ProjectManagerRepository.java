package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ProjectBlueprint;
import com.ntv.ntvcons_backend.entities.ProjectManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectManagerRepository extends JpaRepository<ProjectManager, Integer> {
    List<ProjectManager> findAllByIsDeletedFalse();


    /* Id */
    Optional<ProjectManager> findByProjectManagerIdAndIsDeletedIsFalse(int projectManagerId);
    List<ProjectManager> findAllByProjectManagerIdInAndIsDeletedIsFalse(Collection<Integer> projectManagerIdCollection);


    /* projectId */
    List<ProjectManager> findAllByProjectIdAndIsDeletedIsFalse(int projectId);
    List<ProjectManager> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Integer> projectIdCollection);


    /* managerId */
    List<ProjectManager> findAllByManagerIdAndIsDeletedIsFalse(int managerId);
    List<ProjectManager> findAllByManagerIdInAndIsDeletedIsFalse(Collection<Integer> managerIdCollection);
}
