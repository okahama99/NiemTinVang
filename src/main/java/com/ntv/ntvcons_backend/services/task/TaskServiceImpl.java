package com.ntv.ntvcons_backend.services.task;

import com.ntv.ntvcons_backend.constants.SearchOption;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        /* TODO: also create EntityWrapper for task */

        return taskRepository.saveAndFlush(newTask);
    }
    @Override
    public TaskReadDTO createTaskByDTO(TaskCreateDTO newTaskDTO) throws Exception {
        Task newTask = modelMapper.map(newTaskDTO, Task.class);

        newTask = createTask(newTask);

        return modelMapper.map(newTask, TaskReadDTO.class);
    }

    /* READ */
    /* TODO: add get externalFile via pairing */
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
    public List<Task> getAllByPlanStartDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = new ArrayList<>();

        switch (searchOption) {
            case BEFORE_DATE:
                taskList = taskRepository.findAllByPlanStartDateBeforeAndIsDeletedIsFalse(toDate);
                break;

            case AFTER_DATE:
                taskList = taskRepository.findAllByPlanStartDateAfterAndIsDeletedIsFalse(fromDate);
                break;

            case BETWEEN_DATE:
                taskList = taskRepository.findAllByPlanStartDateBetweenAndIsDeletedIsFalse(fromDate, toDate);
                break;

            default:
                /* All invalid searchOption should've been caught at controller,
                   if reach here then: not normal / slip up */
                throw new IllegalArgumentException("Invalid SearchOption used for entity Task");
        }

        if (taskList.isEmpty()) {
            return null;
        }

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByPlanStartDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByPlanStartDate(searchOption, fromDate, toDate);

        if (taskList == null) {
            return null;
        }

        return taskList.stream()
                .map(task -> modelMapper.map(task, TaskReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllByPlanEndDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = new ArrayList<>();

        switch (searchOption) {
            case BEFORE_DATE:
                taskList = taskRepository.findAllByPlanEndDateBeforeAndIsDeletedIsFalse(toDate);
                break;

            case AFTER_DATE:
                taskList = taskRepository.findAllByPlanEndDateAfterAndIsDeletedIsFalse(fromDate);
                break;

            case BETWEEN_DATE:
                taskList = taskRepository.findAllByPlanEndDateBetweenAndIsDeletedIsFalse(fromDate, toDate);
                break;

            default:
                /* All invalid searchOption should've been caught at controller,
                   if reach here then: not normal / slip up */
                throw new IllegalArgumentException("Invalid SearchOption used for entity Task");
        }

        if (taskList.isEmpty()) {
            return null;
        }

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByPlanEndDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByPlanEndDate(searchOption, fromDate, toDate);

        if (taskList == null) {
            return null;
        }

        return taskList.stream()
                .map(task -> modelMapper.map(task, TaskReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllByPlanStartDateAfterAndPlanEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList =
                taskRepository.findAllByPlanStartDateAfterAndPlanEndDateBeforeAndIsDeletedIsFalse(fromDate, toDate);

        if (taskList.isEmpty()) {
            return null;
        }

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByPlanStartDateAfterAndPlanEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByPlanStartDateAfterAndPlanEndDateBefore(fromDate, toDate);

        if (taskList == null) {
            return null;
        }

        return taskList.stream()
                .map(task -> modelMapper.map(task, TaskReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllByActualStartDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = new ArrayList<>();

        switch (searchOption) {
            case BEFORE_DATE:
                taskList = taskRepository.findAllByActualStartDateBeforeAndIsDeletedIsFalse(toDate);
                break;

            case AFTER_DATE:
                taskList = taskRepository.findAllByActualStartDateAfterAndIsDeletedIsFalse(fromDate);
                break;

            case BETWEEN_DATE:
                taskList = taskRepository.findAllByActualStartDateBetweenAndIsDeletedIsFalse(fromDate, toDate);
                break;

            default:
                /* All invalid searchOption should've been caught at controller,
                   if reach here then: not normal / slip up */
                throw new IllegalArgumentException("Invalid SearchOption used for entity Task");
        }

        if (taskList.isEmpty()) {
            return null;
        }

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByActualStartDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByActualStartDate(searchOption, fromDate, toDate);

        if (taskList == null) {
            return null;
        }

        return taskList.stream()
                .map(task -> modelMapper.map(task, TaskReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllByActualEndDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = new ArrayList<>();

        switch (searchOption) {
            case BEFORE_DATE:
                taskList = taskRepository.findAllByActualEndDateBeforeAndIsDeletedIsFalse(toDate);
                break;

            case AFTER_DATE:
                taskList = taskRepository.findAllByActualEndDateAfterAndIsDeletedIsFalse(fromDate);
                break;

            case BETWEEN_DATE:
                taskList = taskRepository.findAllByActualEndDateBetweenAndIsDeletedIsFalse(fromDate, toDate);
                break;

            default:
                /* All invalid searchOption should've been caught at controller,
                   if reach here then: not normal / slip up */
                throw new IllegalArgumentException("Invalid SearchOption used for entity Task");
        }

        if (taskList.isEmpty()) {
            return null;
        }

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByActualEndDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByActualEndDate(searchOption, fromDate, toDate);

        if (taskList == null) {
            return null;
        }

        return taskList.stream()
                .map(task -> modelMapper.map(task, TaskReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllByActualStartDateAfterAndActualEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList =
                taskRepository.findAllByActualStartDateAfterAndActualEndDateBeforeAndIsDeletedIsFalse(fromDate, toDate);

        if (taskList.isEmpty()) {
            return null;
        }

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByActualStartDateAfterAndActualEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByActualStartDateAfterAndActualEndDateBefore(fromDate, toDate);

        if (taskList == null) {
            return null;
        }

        return taskList.stream()
                .map(task -> modelMapper.map(task, TaskReadDTO.class))
                .collect(Collectors.toList());
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

        /* TODO: also create EntityWrapper for task */

        task.get().setIsDeleted(true);

        taskRepository.saveAndFlush(task.get());

        return true;
    }
}
