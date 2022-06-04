package com.ntv.ntvcons_backend.repositories.PagingRepositories;

import com.ntv.ntvcons_backend.entities.ProjectBlueprint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectBlueprintPagingRepository extends PagingAndSortingRepository<ProjectBlueprint, Integer> {
        Page<ProjectBlueprint> findAll(Pageable pageable);
}
