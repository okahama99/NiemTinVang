package com.ntv.ntvcons_backend.services.task;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.task.*;
import com.ntv.ntvcons_backend.entities.Task;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface TaskService {
    /* CREATE */
    Task createTask(Task newTask) throws Exception;
    TaskReadDTO createTaskByDTO(TaskCreateDTO newTaskDTO) throws Exception;

    /* READ */
    List<Task> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<TaskReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    Task getById(long taskId) throws Exception;
    TaskReadDTO getDTOById(long taskId) throws Exception;
    
    List<Task> getAllByIdIn(Collection<Long> taskIdCollection) throws Exception;
    List<TaskReadDTO> getAllDTOByIdIn(Collection<Long> taskIdCollection) throws Exception;

    List<Task> getAllByProjectId(long projectId) throws Exception;
    List<TaskReadDTO> getAllDTOByProjectId(long projectId) throws Exception;

    List<Task> getAllByTaskNameContains(String taskName) throws Exception;
    List<TaskReadDTO> getAllDTOByTaskNameContains(String taskName) throws Exception;

    List<Task> getAllByPlanStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByPlanStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;

    List<Task> getAllByPlanEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByPlanEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;

    List<Task> getAllByPlanStartDateAfterAndPlanEndDateBefore(Instant fromDate, Instant toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByPlanStartDateAfterAndPlanEndDateBefore(Instant fromDate, Instant toDate) throws Exception;

    List<Task> getAllByActualStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByActualStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;

    List<Task> getAllByActualEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByActualEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;

    List<Task> getAllByActualStartDateAfterAndActualEndDateBefore(Instant fromDate, Instant toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByActualStartDateAfterAndActualEndDateBefore(Instant fromDate, Instant toDate) throws Exception;

    /* UPDATE */
    Task updateTask(Task updatedTask) throws Exception;
    TaskReadDTO updateTaskByDTO(TaskUpdateDTO updatedTaskDTO) throws Exception;

    /* DELETE */
    boolean deleteTask(long taskId) throws Exception;
}
