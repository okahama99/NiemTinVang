package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TaskRepository  extends JpaRepository<Task, Integer> {
    Task findByTaskNameLike (String name);

    Task findByPlanStartDateAndPlanEndDate (Date start, Date end);

    Task findByActualStartDateAndActualEndDate (Date start, Date end);

    Task findByActualStartDateOrActualEndDate(Date start, Date end);

    Task findByPlanStartDateOrPlanEndDate(Date start, Date end);
}
