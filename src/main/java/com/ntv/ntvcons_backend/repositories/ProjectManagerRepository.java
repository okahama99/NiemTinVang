package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ProjectManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectManagerRepository extends CrudRepository<ProjectManager, Integer> {

}
