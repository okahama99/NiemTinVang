package com.ntv.ntvcons_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    Task findByTaskNameLike (String name);

    Task findByPlanStartDateAndPlanEndDate (Date start, Date end);

    Task findByActualStartDateAndActualEndDate (Date start, Date end);

    Task findByActualStartDateOrActualEndDate(Date start, Date end);

    Task findByPlanStartDateOrPlanEndDate(Date start, Date end);
}
