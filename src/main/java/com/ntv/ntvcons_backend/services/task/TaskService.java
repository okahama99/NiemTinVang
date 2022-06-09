package com.ntv.ntvcons_backend.services.task;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.task.TaskDTO;
import com.ntv.ntvcons_backend.entities.Task;

import java.time.Instant;
import java.util.List;

public interface TaskService {
    /* CREATE */
    Task createTask(Task newTask) throws Exception;
    TaskDTO createTaskByDTO(TaskDTO newTaskDTO) throws Exception;

    /* READ */
    List<Task> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<TaskDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    Task getById(long taskId) throws Exception;
    TaskDTO getDTOById(long taskId) throws Exception;

    List<Task> getAllByProjectId(long projectId) throws Exception;
    List<TaskDTO> getAllDTOByProjectId(long projectId) throws Exception;

    List<Task> getAllByTaskNameLike(String taskName) throws Exception;
    List<TaskDTO> getAllDTOByTaskNameLike(long taskId) throws Exception;

    List<Task> getAllByPlanStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;
    List<TaskDTO> getAllDTOByPlanStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;

    List<Task> getAllByPlanEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;
    List<TaskDTO> getAllDTOByPlanEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;

    List<Task> getAllByPlanStartDateAfterAndPlanEndDateBefore(Instant fromDate, Instant toDate) throws Exception;
    List<TaskDTO> getAllDTOByPlanStartDateAfterAndPlanEndDateBefore(Instant fromDate, Instant toDate) throws Exception;

    List<Task> getAllByActualStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;
    List<TaskDTO> getAllDTOByActualStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;

    List<Task> getAllByActualEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;
    List<TaskDTO> getAllDTOByActualEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception;

    List<Task> getAllByActualStartDateAfterAndActualEndDateBefore(Instant fromDate, Instant toDate) throws Exception;
    List<TaskDTO> getAllDTOByActualStartDateAfterAndActualEndDateBefore(Instant fromDate, Instant toDate) throws Exception;

    /* UPDATE */
    Task updateTask(Task updatedTask) throws Exception;
    TaskDTO updateTaskByDTO(TaskDTO updatedTaskDTO) throws Exception;

    /* DELETE */
    boolean deleteTask(long taskId) throws Exception;
}
