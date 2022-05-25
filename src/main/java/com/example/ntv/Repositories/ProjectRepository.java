package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository  extends JpaRepository<Project, Integer> {
    Project findByProjectNameLike (String projectName);
}
