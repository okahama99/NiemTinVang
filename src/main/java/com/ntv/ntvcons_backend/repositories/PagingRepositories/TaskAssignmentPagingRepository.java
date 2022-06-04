package com.ntv.ntvcons_backend.repositories.PagingRepositories;

import com.ntv.ntvcons_backend.entities.TaskAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssignmentPagingRepository extends PagingAndSortingRepository<TaskAssignment, Integer> {
    Page<TaskAssignment> findAll(Pageable pageable);
}
