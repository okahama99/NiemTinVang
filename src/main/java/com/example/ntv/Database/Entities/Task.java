package com.example.ntv.Database.Entities;
import com.example.ntv.Database.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "task")
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TaskId", nullable = false)
    private int taskId;
    @Basic
    @Column(name = "ProjectId", nullable = false)
    private int projectId;
    @Basic
    @Column(name = "TaskName", nullable = false)
    private String taskName;
    @Basic
    @Column(name = "TaskDesc")
    private String taskDesc;
    @Basic
    @Column(name = "PlanStartDate", nullable = false)
    private Date planStartDate;
    @Basic
    @Column(name = "PlanEndDate", nullable = false)
    private Date planEndDate;
    @Basic
    @Column(name = "ActualStartDate")
    private Date actualStartDate;
    @Basic
    @Column(name = "ActualEndDate")
    private Date actualEndDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId && projectId == task.projectId && Objects.equals(taskName, task.taskName) && Objects.equals(taskDesc, task.taskDesc) && Objects.equals(planStartDate, task.planStartDate) && Objects.equals(planEndDate, task.planEndDate) && Objects.equals(actualStartDate, task.actualStartDate) && Objects.equals(actualEndDate, task.actualEndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, projectId, taskName, taskDesc, planStartDate, planEndDate, actualStartDate, actualEndDate);
    }
}