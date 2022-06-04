package com.ntv.ntvcons_backend.repositories.PagingRepositories;

import com.ntv.ntvcons_backend.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPagingRepository extends PagingAndSortingRepository<Task, Integer> {
    Page<Task> findAll(Pageable pageable);
}
