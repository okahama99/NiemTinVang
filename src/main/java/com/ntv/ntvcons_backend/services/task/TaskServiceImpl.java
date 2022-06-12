package com.ntv.ntvcons_backend.services.task;

import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.task.*;
import com.ntv.ntvcons_backend.entities.Task;
import com.ntv.ntvcons_backend.repositories.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ModelMapper modelMapper;

    /* CREATE */
    @Override
    public Task createTask(Task newTask) throws Exception {
        return taskRepository.saveAndFlush(newTask);
    }
    @Override
    public TaskReadDTO createTaskByDTO(TaskCreateDTO newTaskDTO) throws Exception {
        Task newTask = modelMapper.map(newTaskDTO, Task.class);

        newTask = createTask(newTask);

        return modelMapper.map(newTask, TaskReadDTO.class);
    }

    /* READ */
    @Override
    public List<Task> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Task> taskPage = taskRepository.findAllByIsDeletedIsFalse(paging);

        if (taskPage.isEmpty()) {
            return null;
        }

        return taskPage.getContent();
    }
    @Override
    public List<TaskReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        List<Task> taskList = getAll(pageNo, pageSize, sortBy, sortType);

        if (taskList != null && !taskList.isEmpty()) {
            int totalPage = (int) Math.ceil((double) taskList.size() / pageSize);

            return taskList.stream()
                    .map(task -> {
                        TaskReadDTO taskReadDTO = 
                                modelMapper.map(task, TaskReadDTO.class);
                        taskReadDTO.setTotalPage(totalPage);
                        return taskReadDTO;})
                    .collect(Collectors.toList());

        } else {
            return null;
        }
    }

    @Override
    public Task getById(long taskId) throws Exception {
        Optional<Task> task = taskRepository.findByTaskIdAndIsDeletedIsFalse(taskId);

        return task.orElse(null);
    }
    @Override
    public TaskReadDTO getDTOById(long taskId) throws Exception {
        Task task = getById(taskId);

        if (task == null) {
            return null;
        }

        return modelMapper.map(task, TaskReadDTO.class);
    }

    @Override
    public List<Task> getAllByIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<Task> taskList = taskRepository.findAllByTaskIdInAndIsDeletedIsFalse(taskIdCollection);

        if (taskList.isEmpty()) {
            return null;
        }

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<Task> taskList = getAllByIdIn(taskIdCollection);

        if (taskList == null) {
            return null;
        }

        return taskList.stream().map(task -> modelMapper.map(task, TaskReadDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> getAllByProjectId(long projectId) throws Exception {
        List<Task> taskList = taskRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId);

        if (taskList.isEmpty()) {
            return null;
        }

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByProjectId(long projectId) throws Exception {
        List<Task> taskList = getAllByProjectId(projectId);

        if (taskList == null) {
            return null;
        }

        return taskList.stream()
                .map(task -> modelMapper.map(task, TaskReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllByTaskNameContains(String taskName) throws Exception {
        List<Task> taskList = taskRepository.findAllByTaskNameContainsAndIsDeletedIsFalse(taskName);

        if (taskList.isEmpty()) {
            return null;
        }

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByTaskNameContains(String taskName) throws Exception {
        List<Task> taskList = getAllByTaskNameContains(taskName);

        if (taskList == null) {
            return null;
        }

        return taskList.stream()
                .map(task -> modelMapper.map(task, TaskReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllByPlanStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByPlanStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByPlanEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByPlanEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByPlanStartDateAfterAndPlanEndDateBefore(Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByPlanStartDateAfterAndPlanEndDateBefore(Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByActualStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByActualStartDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByActualEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByActualEndDate(SearchType searchType, Instant fromDate, Instant toDate) throws Exception {
        return null;
    }

    @Override
    public List<Task> getAllByActualStartDateAfterAndActualEndDateBefore(Instant fromDate, Instant toDate) throws Exception {
        return null;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByActualStartDateAfterAndActualEndDateBefore(Instant fromDate, Instant toDate) throws Exception {
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

        return taskRepository.saveAndFlush(updatedTask);
    }
    @Override
    public TaskReadDTO updateTaskByDTO(TaskUpdateDTO updatedTaskDTO) throws Exception {
        Task updatedTask = modelMapper.map(updatedTaskDTO, Task.class);

        updatedTask = updateTask(updatedTask);

        if (updatedTask == null) {
            return null;
        }

        return modelMapper.map(updatedTask, TaskReadDTO.class);
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

        taskRepository.saveAndFlush(task.get());

        return true;
    }
}
