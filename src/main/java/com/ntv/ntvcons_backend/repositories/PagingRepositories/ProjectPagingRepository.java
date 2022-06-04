package com.ntv.ntvcons_backend.repositories.PagingRepositories;

import com.ntv.ntvcons_backend.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPagingRepository extends PagingAndSortingRepository<Project, Integer> {
    Page<Project> findAll(Pageable pageable);
}
