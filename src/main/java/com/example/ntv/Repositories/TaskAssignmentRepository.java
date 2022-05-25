package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssignmentRepository   extends JpaRepository<TaskAssignment, Integer> {

}
