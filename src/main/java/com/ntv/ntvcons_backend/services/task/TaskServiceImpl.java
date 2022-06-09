package com.ntv.ntvcons_backend.services.task;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.task.TaskDTO;
import com.ntv.ntvcons_backend.entities.Task;
import com.ntv.ntvcons_backend.repositories.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ModelMapper modelMapper;

    /* CREATE */
    @Override
    public Task createTask(Task newTask) throws Exception {
        return taskRepository.save(newTask);
    }
    @Override
    public TaskDTO createTaskByDTO(TaskDTO newTaskDTO) throws Exception {
        Task newTask = modelMapper.map(newTaskDTO, Task.class);

        newTask = createTask(newTask);

        return modelMapper.map(newTask, TaskDTO.class);
    }

    /* READ */
    @Override
    public List<Task> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        return null;
    }
    @Override
    public List<TaskDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        return null;
    }

    @Override
    public Task getById(long taskId) throws Exception {
        return null;
    }
    @Override
    public TaskDTO getDTOById(long taskId) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByProjectId(long projectId) throws Exception {
        return null;
    }
    @Override
    public List<TaskDTO> getAllDTOByProjectId(long projectId) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByTaskNameLike(String taskName) throws Exception {
        return null;
    }
    @Override
    public List<TaskDTO> getAllDTOByTaskNameLike(long taskId) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByPlanStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskDTO> getAllDTOByPlanStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByPlanEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskDTO> getAllDTOByPlanEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByPlanStartDateAfterAndPlanEndDateBefore(Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskDTO> getAllDTOByPlanStartDateAfterAndPlanEndDateBefore(Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByActualStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskDTO> getAllDTOByActualStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByActualEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskDTO> getAllDTOByActualEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByActualStartDateAfterAndActualEndDateBefore(Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskDTO> getAllDTOByActualStartDateAfterAndActualEndDateBefore(Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    /* UPDATE */
    @Override
    public Task updateTask(Task updatedTask) throws Exception {
        Optional<Task> task = taskRepository.findByTaskIdAndIsDeletedIsFalse(updatedTask.getTaskId());

        if (!task.isPresent()) {
            return null;
            /* Not found by Id, return null */
        }

        return taskRepository.save(updatedTask);
    }
    @Override
    public TaskDTO updateTaskByDTO(TaskDTO updatedTaskDTO) throws Exception {
        Task updatedTask = modelMapper.map(updatedTaskDTO, Task.class);

        updatedTask = updateTask(updatedTask);

        if (updatedTask == null) {
            return null;
        }

        return modelMapper.map(updatedTask, TaskDTO.class);
    }

    /* DELETE */
    @Override
    public boolean deleteTask(long taskId) throws Exception {
        Optional<Task> task = taskRepository.findByTaskIdAndIsDeletedIsFalse(taskId);

        if (!task.isPresent()) {
            return false;
            /* Not found with Id */
        }

        task.get().setIsDeleted(true);

        taskRepository.save(task.get());

        return true;
    }
}
