package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TaskId", nullable = false)
    private int taskId;
    @Basic
    @Column(name = "ProjectId", nullable = false)
    private int projectId;
    @Basic
    @Column(name = "TaskName", nullable = false, length = 255)
    private String taskName;
    @Basic
    @Column(name = "TaskDesc", nullable = true, length = 500)
    private String taskDesc;
    @Basic
    @Column(name = "PlanStartDate", nullable = false)
    private Date planStartDate;
    @Basic
    @Column(name = "PlanEndDate", nullable = false)
    private Date planEndDate;
    @Basic
    @Column(name = "ActualStartDate", nullable = true)
    private Date actualStartDate;
    @Basic
    @Column(name = "ActualEndDate", nullable = true)
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
