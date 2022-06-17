package com.ntv.ntvcons_backend.services.task;

import com.ntv.ntvcons_backend.constants.SearchOption;
import com.ntv.ntvcons_backend.dtos.task.TaskCreateDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskUpdateDTO;
import com.ntv.ntvcons_backend.entities.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TaskService {
    /* CREATE */
    Task createTask(Task newTask) throws Exception;
    TaskReadDTO createTaskByDTO(TaskCreateDTO newTaskDTO) throws Exception;

    /* READ */
    List<Task> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<TaskReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    boolean existsById(long taskId) throws Exception;
    Task getById(long taskId) throws Exception;
    TaskReadDTO getDTOById(long taskId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> taskIdCollection) throws Exception;
    List<Task> getAllByIdIn(Collection<Long> taskIdCollection) throws Exception;
    List<TaskReadDTO> getAllDTOByIdIn(Collection<Long> taskIdCollection) throws Exception;
    Map<Long, Task> mapTaskIdTaskByIdIn(Collection<Long> taskIdCollection) throws Exception;
    Map<Long, TaskReadDTO> mapTaskIdTaskDTOByIdIn(Collection<Long> taskIdCollection) throws Exception;

    List<Task> getAllByProjectId(long projectId) throws Exception;
    List<TaskReadDTO> getAllDTOByProjectId(long projectId) throws Exception;

    List<Task> getAllByTaskNameContains(String taskName) throws Exception;
    List<TaskReadDTO> getAllDTOByTaskNameContains(String taskName) throws Exception;

    List<Task> getAllByPlanStartDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByPlanStartDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    List<Task> getAllByPlanEndDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByPlanEndDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    List<Task> getAllByPlanStartDateAfterAndPlanEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByPlanStartDateAfterAndPlanEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    List<Task> getAllByActualStartDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByActualStartDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    List<Task> getAllByActualEndDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByActualEndDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    List<Task> getAllByActualStartDateAfterAndActualEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception;
    List<TaskReadDTO> getAllDTOByActualStartDateAfterAndActualEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception;

    /* UPDATE */
    Task updateTask(Task updatedTask) throws Exception;
    TaskReadDTO updateTaskByDTO(TaskUpdateDTO updatedTaskDTO) throws Exception;

    /* DELETE */
    boolean deleteTask(long taskId) throws Exception;
}
