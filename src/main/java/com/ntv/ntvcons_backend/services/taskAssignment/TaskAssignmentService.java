package com.ntv.ntvcons_backend.services.taskAssignment;

import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentUpdateDTO;
import com.ntv.ntvcons_backend.entities.TaskAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TaskAssignmentService {
    /* CREATE */
    TaskAssignment createTaskAssignment(TaskAssignment newTaskAssignment) throws Exception;
    TaskAssignmentReadDTO createTaskAssignmentByDTO(TaskAssignmentCreateDTO newTaskAssignmentDTO) throws Exception;

    /* READ */
    Page<TaskAssignment> getPageAll(Pageable paging) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    TaskAssignment getById(long assignmentId) throws Exception;
    TaskAssignmentReadDTO getDTOById(long assignmentId) throws Exception;

    List<TaskAssignment> getAllByIdIn(Collection<Long> assignmentIdCollection) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOByIdIn(Collection<Long> assignmentIdCollection) throws Exception;

    /** Task 1 - 1 TaskAssignment */
    TaskAssignment getByTaskId(long taskId) throws Exception;
    TaskAssignmentReadDTO getDTOByTaskId(long taskId) throws Exception;

    List<TaskAssignment> getAllByTaskIdIn(Collection<Long> taskIdCollection) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOByTaskIdIn(Collection<Long> taskIdCollection) throws Exception;
    Map<Long, TaskAssignmentReadDTO> mapTaskIdTaskAssignmentDTOByTaskIdIn(Collection<Long> taskIdCollection) throws Exception;
    Page<TaskAssignment> getPageAllByTaskIdIn(Pageable paging, Collection<Long> taskIdCollection) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOInPagingByTaskIdIn(Pageable paging, Collection<Long> taskIdCollection) throws Exception;

    List<TaskAssignment> getAllByAssignerId(long assignerId) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOByAssignerId(long assignerId) throws Exception;
    Page<TaskAssignment> getPageAllByAssignerId(Pageable paging, long assignerId) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOInPagingByAssignerId(Pageable paging, long assignerId) throws Exception;

    List<TaskAssignment> getAllByAssigneeId(long assigneeId) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOByAssigneeId(long assigneeId) throws Exception;
    Page<TaskAssignment> getPageAllByAssigneeId(Pageable paging, long assigneeId) throws Exception;
    List<TaskAssignmentReadDTO> getAllDTOInPagingByAssigneeId(Pageable paging, long assigneeId) throws Exception;

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
