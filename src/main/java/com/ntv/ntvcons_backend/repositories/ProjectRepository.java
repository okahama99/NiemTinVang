package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Integer> {
    Project findByProjectNameLike (String projectName);
}
