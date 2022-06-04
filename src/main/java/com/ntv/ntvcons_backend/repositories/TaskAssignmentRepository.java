package com.example.ntv.Repositories;

import com.ntv.ntvcons_backend.entities.TaskAssignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssignmentRepository extends CrudRepository<TaskAssignment, Integer> {

}
