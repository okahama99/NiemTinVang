package com.ntv.ntvcons_backend.services.task;

import com.ntv.ntvcons_backend.constants.SearchOption;
import com.ntv.ntvcons_backend.dtos.task.TaskCreateDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskUpdateDTO;
import com.ntv.ntvcons_backend.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TaskService {
    /* CREATE */
    Task createTask(Task newTask) throws Exception;
    TaskReadDTO createTaskByDTO(TaskCreateDTO newTaskDTO) throws Exception;

    /* READ */
    Page<Task> getPageAll(Pageable paging) throws Exception;
    List<TaskReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    boolean existsById(long taskId) throws Exception;
    Task getById(long taskId) throws Exception;
    TaskReadDTO getDTOById(long taskId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> taskIdCollection) throws Exception;
    List<Task> getAllByIdIn(Collection<Long> taskIdCollection) throws Exception;
    List<TaskReadDTO> getAllDTOByIdIn(Collection<Long> taskIdCollection) throws Exception;
    Map<Long, TaskReadDTO> mapTaskIdTaskDTOByIdIn(Collection<Long> taskIdCollection) throws Exception;

    List<Task> getAllByProjectId(long projectId) throws Exception;
    List<TaskReadDTO> getAllDTOByProjectId(long projectId) throws Exception;
    Page<Task> getPageAllByProjectId(Pageable paging, long projectId) throws Exception;
    List<TaskReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception;

    List<Task> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    List<TaskReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    Map<Long, List<TaskReadDTO>> mapProjectIdTaskDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    Page<Task> getPageAllByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception;
    List<TaskReadDTO> getAllDTOByInPagingProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception;

    List<Task> getAllByTaskName(String taskName) throws Exception;
    List<TaskReadDTO> getAllDTOByTaskName(String taskName) throws Exception;
    Page<Task> getPageAllByTaskName(Pageable paging, String taskName) throws Exception;
    List<TaskReadDTO> getAllDTOInPagingByTaskName(Pageable paging, String taskName) throws Exception;

    List<Task> getAllByTaskNameContains(String taskName) throws Exception;
    List<TaskReadDTO> getAllDTOByTaskNameContains(String taskName) throws Exception;
    Page<Task> getPageAllByTaskNameContains(Pageable paging, String taskName) throws Exception;
    List<TaskReadDTO> getAllDTOInPagingByTaskNameContains(Pageable paging, String taskName) throws Exception;

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
