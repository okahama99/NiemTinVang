package com.example.ntv.entities;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "taskassignment")
public class TaskAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AssignmentId", nullable = false)
    private int assignmentId;
    @Basic
    @Column(name = "TaskId", nullable = false)
    private int taskId;
    @Basic
    @Column(name = "AssignerId", nullable = false)
    private int assignerId;
    @Basic
    @Column(name = "AssigneeId", nullable = false)
    private int assigneeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskAssignment that = (TaskAssignment) o;
        return assignmentId == that.assignmentId && taskId == that.taskId && assignerId == that.assignerId && assigneeId == that.assigneeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentId, taskId, assignerId, assigneeId);
    }
}