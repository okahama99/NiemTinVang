package com.ntv.ntvcons_backend.services.task;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.task.TaskDTO;
import com.ntv.ntvcons_backend.entities.Task;

import java.time.LocalDateTime;
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

    List<Task> getAllByPlanStartDate(SearchType searchType, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskDTO> getAllDTOByPlanStartDate(SearchType searchType, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    List<Task> getAllByPlanEndDate(SearchType searchType, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskDTO> getAllDTOByPlanEndDate(SearchType searchType, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    List<Task> getAllByPlanStartDateAfterAndPlanEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskDTO> getAllDTOByPlanStartDateAfterAndPlanEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    List<Task> getAllByActualStartDate(SearchType searchType, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskDTO> getAllDTOByActualStartDate(SearchType searchType, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    List<Task> getAllByActualEndDate(SearchType searchType, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskDTO> getAllDTOByActualEndDate(SearchType searchType, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    List<Task> getAllByActualStartDateAfterAndActualEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskDTO> getAllDTOByActualStartDateAfterAndActualEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    /* UPDATE */
    Task updateTask(Task updatedTask) throws Exception;
    TaskDTO updateTaskByDTO(TaskDTO updatedTaskDTO) throws Exception;

    /* DELETE */
    boolean deleteTask(long taskId) throws Exception;
}
