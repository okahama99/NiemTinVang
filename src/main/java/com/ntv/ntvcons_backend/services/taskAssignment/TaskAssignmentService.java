package com.ntv.ntvcons_backend.services.taskAssignment;

import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentUpdateDTO;
import com.ntv.ntvcons_backend.entities.TaskAssignment;

import java.util.Collection;
import java.util.List;

public interface TaskAssignmentService {
    /* CREATE */
    TaskAssignment createTaskAssignment(TaskAssignment newTaskAssignment) throws Exception;

    List<TaskAssignment> createBulkTaskAssignment(List<TaskAssignment> newTaskAssignmentList) throws Exception;

    /* READ */
    List<TaskAssignment> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    TaskAssignment getById(long taskAssignmentId) throws Exception;

    List<TaskAssignment> getAllByIdIn(Collection<Long> taskAssignmentIdCollection) throws Exception;

    /** Task 1 - 1 TaskAssignment */
    TaskAssignment getByTaskId(long taskId) throws Exception;
    TaskAssignmentReadDTO getDTOByTaskId(long taskId) throws Exception;

    List<TaskAssignment> getAllByAssignerId(long assignerId) throws Exception;

    List<TaskAssignment> getAllByAssigneeId(long assigneeId) throws Exception;

    /* UPDATE */
    TaskAssignment updateTaskAssignment(TaskAssignment updatedTaskAssignment) throws Exception;
    TaskAssignmentReadDTO updateTaskAssignmentByDTO(TaskAssignmentUpdateDTO updatedTaskAssignmentDTO) throws Exception;

    List<TaskAssignment> updateBulkTaskAssignment(List<TaskAssignment> updatedTaskAssignmentList) throws Exception;

    /* DELETE */
    boolean deleteTaskAssignment(long taskAssignmentId) throws Exception;

    /** Cascade when delete User */
    boolean deleteAllByUserId(long userId) throws Exception;

    /** Cascade when delete Task */
    boolean deleteByTaskId(long taskId) throws Exception;
}
