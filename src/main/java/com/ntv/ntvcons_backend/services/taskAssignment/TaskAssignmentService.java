package com.ntv.ntvcons_backend.services.taskAssignment;

import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentUpdateDTO;
import com.ntv.ntvcons_backend.entities.TaskAssignment;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface TaskAssignmentService {
    /* CREATE */
    TaskAssignment createTaskAssignment(TaskAssignment newTaskAssignment) throws Exception;
    TaskAssignmentReadDTO createTaskAssignmentByDTO(TaskAssignmentCreateDTO newTaskAssignmentDTO) throws Exception;

    /* READ */
    Page<TaskAssignment> getPageAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOInPaging(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    TaskAssignment getById(long assignmentId) throws Exception;
    TaskAssignmentReadDTO getDTOById(long assignmentId) throws Exception;

    List<TaskAssignment> getAllByIdIn(Collection<Long> assignmentIdCollection) throws Exception;

    /** Task 1 - 1 TaskAssignment */
    TaskAssignment getByTaskId(long taskId) throws Exception;
    TaskAssignmentReadDTO getDTOByTaskId(long taskId) throws Exception;

    List<TaskAssignment> getAllByAssignerId(long assignerId) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOByAssignerId(long assignerId) throws Exception;

    List<TaskAssignment> getAllByAssigneeId(long assigneeId) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOByAssigneeId(long assigneeId) throws Exception;

    /* UPDATE */
    TaskAssignment updateTaskAssignment(TaskAssignment updatedTaskAssignment) throws Exception;
    TaskAssignmentReadDTO updateTaskAssignmentByDTO(TaskAssignmentUpdateDTO updatedTaskAssignmentDTO) throws Exception;

    /* DELETE */
    boolean deleteTaskAssignment(long assignmentId) throws Exception;

    /** Cascade when delete User */
    boolean deleteAllByUserId(long userId) throws Exception;

    /** Cascade when delete Task */
    boolean deleteByTaskId(long taskId) throws Exception;
}
