package com.ntv.ntvcons_backend.services.task;

import com.ntv.ntvcons_backend.constants.SearchOption;
import com.ntv.ntvcons_backend.dtos.task.TaskCreateDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskReadDTO;
import com.ntv.ntvcons_backend.dtos.task.TaskUpdateDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentUpdateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportReadDTO;
import com.ntv.ntvcons_backend.entities.Task;
import com.ntv.ntvcons_backend.repositories.TaskRepository;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import com.ntv.ntvcons_backend.services.taskAssignment.TaskAssignmentService;
import com.ntv.ntvcons_backend.services.taskReport.TaskReportService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ProjectService projectService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Autowired
    private TaskReportService taskReportService;
    @Autowired
    private TaskAssignmentService taskAssignmentService;

    /* CREATE */
    @Override
    public Task createTask(Task newTask) throws Exception {
        /* TODO: also create EntityWrapper for task */

        String errorMsg = "";

        /* Check FK */
        if (!projectService.existsById(newTask.getProjectId())) {
            errorMsg += "No Project found with Id: '" + newTask.getProjectId()
                    + "'. Which violate constraint: FK_Task_Project. ";
        }
        if (!userService.existsById(newTask.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newTask.getCreatedBy()
                    + "'. Which violate constraint: FK_Task_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (taskRepository
                .existsByProjectIdAndTaskNameAndIsDeletedIsFalse(
                        newTask.getProjectId(),
                        newTask.getTaskName())) {
            errorMsg += "Already exists another Task with projectId: '" + newTask.getProjectId()
                    + "', taskName: '" + newTask.getTaskName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return taskRepository.saveAndFlush(newTask);
    }
    @Override
    public TaskReadDTO createTaskByDTO(TaskCreateDTO newTaskDTO) throws Exception {
        modelMapper.typeMap(TaskCreateDTO.class, Task.class)
                .addMappings(mapper -> {
                    mapper.skip(Task::setPlanStartDate);
                    mapper.skip(Task::setPlanEndDate);});

        Task newTask = modelMapper.map(newTaskDTO, Task.class);

        /* Already check NOT NULL */
        newTask.setPlanStartDate(
                LocalDateTime.parse(newTaskDTO.getPlanStartDate(), dateTimeFormatter));

        if (newTaskDTO.getPlanEndDate() != null) {
            newTask.setPlanEndDate(
                    LocalDateTime.parse(newTaskDTO.getPlanEndDate(), dateTimeFormatter));

            if (newTask.getPlanEndDate().isBefore(newTask.getPlanStartDate())) {
                throw new IllegalArgumentException("planEndDate is before planStartDate");
            }
        }

        newTask = createTask(newTask);

        /* Create associated TaskAssignment (if present) */
        if (newTaskDTO.getTaskAssignment() != null) {
            taskAssignmentService.createTaskAssignmentByDTO(newTaskDTO.getTaskAssignment());
        }

        return fillDTO(newTask);
    }

    /* READ */
    /* TODO: add get externalFile via pairing */
    @Override
    public Page<Task> getPageAll(Pageable paging) throws Exception {
        Page<Task> taskPage = taskRepository.findAllByIsDeletedIsFalse(paging);

        if (taskPage.isEmpty())
            return null;

        return taskPage;
    }
    @Override
    public List<TaskReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<Task> taskPage = getPageAll(paging);

        if (taskPage == null)
            return null;

        List<Task> taskList = taskPage.getContent();

        if (taskList.isEmpty())
            return null;

        return fillAllDTO(taskList, taskPage.getTotalPages());
    }

    @Override
    public boolean existsById(long taskId) throws Exception {
        return taskRepository.existsByTaskIdAndIsDeletedIsFalse(taskId);
    }
    @Override
    public Task getById(long taskId) throws Exception {
        return taskRepository
                .findByTaskIdAndIsDeletedIsFalse(taskId)
                .orElse(null);
    }
    @Override
    public TaskReadDTO getDTOById(long taskId) throws Exception {
        Task task = getById(taskId);

        if (task == null) 
            return null;

        return fillDTO(task);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> taskIdCollection) throws Exception {
        return taskRepository.existsAllByTaskIdInAndIsDeletedIsFalse(taskIdCollection);
    }
    @Override
    public List<Task> getAllByIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<Task> taskList = taskRepository.findAllByTaskIdInAndIsDeletedIsFalse(taskIdCollection);

        if (taskList.isEmpty()) 
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<Task> taskList = getAllByIdIn(taskIdCollection);

        if (taskList == null) 
            return null;

        return fillAllDTO(taskList, null);
    }
    @Override
    public Map<Long, TaskReadDTO> mapTaskIdTaskDTOByIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<TaskReadDTO> taskDTOList = getAllDTOByIdIn(taskIdCollection);

        if (taskDTOList == null) 
            return new HashMap<>();

        return taskDTOList.stream().collect(Collectors.toMap(TaskReadDTO::getTaskId, Function.identity()));
    }

    @Override
    public List<Task> getAllByProjectId(long projectId) throws Exception {
        List<Task> taskList = taskRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId);

        if (taskList.isEmpty()) 
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByProjectId(long projectId) throws Exception {
        List<Task> taskList = getAllByProjectId(projectId);

        if (taskList == null) 
            return null;

        return fillAllDTO(taskList, null);
    }
    @Override
    public Page<Task> getPageAllByProjectId(Pageable paging, long projectId) throws Exception {
        Page<Task> taskPage =
                taskRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId, paging);

        if (taskPage.isEmpty())
            return null;

        return taskPage;
    }
    @Override
    public List<TaskReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception {
        Page<Task> taskPage = getPageAllByProjectId(paging, projectId);

        if (taskPage == null)
            return null;

        List<Task> taskList = taskPage.getContent();

        if (taskList.isEmpty())
            return null;

        return fillAllDTO(taskList, taskPage.getTotalPages());
    }

    @Override
    public List<Task> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Task> taskList = taskRepository.findAllByProjectIdInAndIsDeletedIsFalse(projectIdCollection);

        if (taskList.isEmpty()) 
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Task> taskList = getAllByProjectIdIn(projectIdCollection);

        if (taskList == null) 
            return null;

        return fillAllDTO(taskList, null);
    }
    @Override
    public Map<Long, List<TaskReadDTO>> mapProjectIdTaskDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<TaskReadDTO> taskDTOList = getAllDTOByProjectIdIn(projectIdCollection);

        if (taskDTOList == null) 
            return new HashMap<>();

        Map<Long, List<TaskReadDTO>> projectIdTaskDTOListMap = new HashMap<>();

        long tmpProjectId;
        List<TaskReadDTO> tmpTaskDTOList;

        for (TaskReadDTO taskDTO : taskDTOList) {
            tmpProjectId = taskDTO.getProjectId();
            tmpTaskDTOList = projectIdTaskDTOListMap.get(tmpProjectId);

            if (tmpTaskDTOList == null) {
                projectIdTaskDTOListMap.put(tmpProjectId, new ArrayList<>(Collections.singletonList(taskDTO)));
            } else {
                tmpTaskDTOList.add(taskDTO);

                projectIdTaskDTOListMap.put(tmpProjectId, tmpTaskDTOList);
            }
        }

        return projectIdTaskDTOListMap;
    }
    @Override
    public Page<Task> getPageAllByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception {
        Page<Task> taskPage =
                taskRepository.findAllByProjectIdInAndIsDeletedIsFalse(projectIdCollection, paging);

        if (taskPage.isEmpty())
            return null;

        return taskPage;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByInPagingProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception {
        Page<Task> taskPage = getPageAllByProjectIdIn(paging, projectIdCollection);

        if (taskPage == null)
            return null;

        List<Task> taskList = taskPage.getContent();

        if (taskList.isEmpty())
            return null;

        return fillAllDTO(taskList, taskPage.getTotalPages());
    }

    @Override
    public List<Task> getAllByTaskName(String taskName) throws Exception {
        List<Task> taskList = taskRepository.findAllByTaskNameAndIsDeletedIsFalse(taskName);

        if (taskList.isEmpty())
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByTaskName(String taskName) throws Exception {
        List<Task> taskList = getAllByTaskName(taskName);

        if (taskList == null)
            return null;

        return fillAllDTO(taskList, null);
    }
    @Override
    public Page<Task> getPageAllByTaskName(Pageable paging, String taskName) throws Exception {
        Page<Task> taskPage =
                taskRepository.findAllByTaskNameAndIsDeletedIsFalse(taskName, paging);

        if (taskPage.isEmpty())
            return null;

        return taskPage;
    }
    @Override
    public List<TaskReadDTO> getAllDTOInPagingByTaskName(Pageable paging, String taskName) throws Exception {
        Page<Task> taskPage = getPageAllByTaskName(paging, taskName);

        if (taskPage == null)
            return null;

        List<Task> taskList = taskPage.getContent();

        if (taskList.isEmpty())
            return null;

        return fillAllDTO(taskList, taskPage.getTotalPages());
    }

    @Override
    public List<Task> getAllByTaskNameContains(String taskName) throws Exception {
        List<Task> taskList = taskRepository.findAllByTaskNameContainsAndIsDeletedIsFalse(taskName);

        if (taskList.isEmpty()) 
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByTaskNameContains(String taskName) throws Exception {
        List<Task> taskList = getAllByTaskNameContains(taskName);

        if (taskList == null) 
            return null;

        return fillAllDTO(taskList, null);
    }
    @Override
    public Page<Task> getPageAllByTaskNameContains(Pageable paging, String taskName) throws Exception {
        Page<Task> taskPage =
                taskRepository.findAllByTaskNameContainsAndIsDeletedIsFalse(taskName, paging);

        if (taskPage.isEmpty())
            return null;

        return taskPage;
    }
    @Override
    public List<TaskReadDTO> getAllDTOInPagingByTaskNameContains(Pageable paging, String taskName) throws Exception {
        Page<Task> taskPage = getPageAllByTaskNameContains(paging, taskName);

        if (taskPage == null)
            return null;

        List<Task> taskList = taskPage.getContent();

        if (taskList.isEmpty())
            return null;

        return fillAllDTO(taskList, taskPage.getTotalPages());
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

        if (taskList.isEmpty()) 
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByPlanStartDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByPlanStartDate(searchOption, fromDate, toDate);

        if (taskList == null) 
            return null;

        return fillAllDTO(taskList, null);
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

        if (taskList.isEmpty()) 
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByPlanEndDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByPlanEndDate(searchOption, fromDate, toDate);

        if (taskList == null) 
            return null;

        return fillAllDTO(taskList, null);
    }

    @Override
    public List<Task> getAllByPlanStartDateAfterAndPlanEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList =
                taskRepository.findAllByPlanStartDateAfterAndPlanEndDateBeforeAndIsDeletedIsFalse(fromDate, toDate);

        if (taskList.isEmpty()) 
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByPlanStartDateAfterAndPlanEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByPlanStartDateAfterAndPlanEndDateBefore(fromDate, toDate);

        if (taskList == null) 
            return null;

        return fillAllDTO(taskList, null);
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

        if (taskList.isEmpty()) 
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByActualStartDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByActualStartDate(searchOption, fromDate, toDate);

        if (taskList == null) 
            return null;

        return fillAllDTO(taskList, null);
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

        if (taskList.isEmpty()) 
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByActualEndDate(SearchOption searchOption, LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByActualEndDate(searchOption, fromDate, toDate);

        if (taskList == null) 
            return null;

        return fillAllDTO(taskList, null);
    }

    @Override
    public List<Task> getAllByActualStartDateAfterAndActualEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList =
                taskRepository.findAllByActualStartDateAfterAndActualEndDateBeforeAndIsDeletedIsFalse(fromDate, toDate);

        if (taskList.isEmpty()) 
            return null;

        return taskList;
    }
    @Override
    public List<TaskReadDTO> getAllDTOByActualStartDateAfterAndActualEndDateBefore(LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<Task> taskList = getAllByActualStartDateAfterAndActualEndDateBefore(fromDate, toDate);

        if (taskList == null) 
            return null;

        return taskList.stream()
                .map(task -> modelMapper.map(task, TaskReadDTO.class))
                .collect(Collectors.toList());
    }

    /* UPDATE */
    @Override
    public Task updateTask(Task updatedTask) throws Exception {
        Task oldTask = getById(updatedTask.getTaskId());

        if (oldTask == null)
            return null; /* Not found by Id, return null */

        String errorMsg = "";

        /* Check FK */
        if (!oldTask.getProjectId().equals(updatedTask.getProjectId())) {
            if (!projectService.existsById(updatedTask.getProjectId())) {
                errorMsg += "No Project found with Id: '" + updatedTask.getProjectId()
                        + "'. Which violate constraint: FK_Task_Project. ";
            }
        }
        if (oldTask.getUpdatedBy() != null) {
            if (!oldTask.getUpdatedBy().equals(updatedTask.getUpdatedBy())) {
                if (!userService.existsById(updatedTask.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedTask.getUpdatedBy()
                            + "'. Which violate constraint: FK_Task_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedTask.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedTask.getUpdatedBy()
                        + "'. Which violate constraint: FK_Task_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (taskRepository
                .existsByProjectIdAndTaskNameAndTaskIdIsNotAndIsDeletedIsFalse(
                        updatedTask.getProjectId(),
                        updatedTask.getTaskName(),
                        updatedTask.getTaskId())) {
            errorMsg += "Already exists another Task with projectId: '" + updatedTask.getProjectId()
                    + "', taskName: '" + updatedTask.getTaskName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        updatedTask.setCreatedAt(oldTask.getCreatedAt());
        updatedTask.setCreatedBy(oldTask.getCreatedBy());

        return taskRepository.saveAndFlush(updatedTask);
    }
    @Override
    public TaskReadDTO updateTaskByDTO(TaskUpdateDTO updatedTaskDTO) throws Exception {
        modelMapper.typeMap(TaskUpdateDTO.class, Task.class)
                .addMappings(mapper -> {
                    mapper.skip(Task::setPlanStartDate);
                    mapper.skip(Task::setPlanEndDate);
                    mapper.skip(Task::setActualStartDate);
                    mapper.skip(Task::setActualEndDate);});

        Task updatedTask = modelMapper.map(updatedTaskDTO, Task.class);

        /* Already check NOT NULL */
        updatedTask.setPlanStartDate(
                LocalDateTime.parse(updatedTaskDTO.getPlanStartDate(), dateTimeFormatter));

        if (updatedTaskDTO.getPlanEndDate() != null) {
            updatedTask.setPlanEndDate(
                    LocalDateTime.parse(updatedTaskDTO.getPlanEndDate(), dateTimeFormatter));

            if (updatedTask.getPlanEndDate().isBefore(updatedTask.getPlanStartDate()))
                throw new IllegalArgumentException("planEndDate is before planStartDate");
        }

        boolean hasActualStartDate = false;
        if (updatedTaskDTO.getActualStartDate() != null) {
            hasActualStartDate = true;

            updatedTask.setActualStartDate(
                    LocalDateTime.parse(updatedTaskDTO.getActualStartDate(), dateTimeFormatter));

            /* Nếu đã thực tế thì làm sao tương lai, tương lai thì nó là kế hoạch rồi */
            if (updatedTask.getActualStartDate().isAfter(LocalDateTime.now()))
                throw new IllegalArgumentException("actualStartDate can't be in the future");
        }

        if (updatedTaskDTO.getActualEndDate() != null) {
            updatedTask.setActualEndDate(
                    LocalDateTime.parse(updatedTaskDTO.getActualEndDate(), dateTimeFormatter));

            /* Nếu đã thực tế thì làm sao tương lai, tương lai thì nó là kế hoạch rồi */
            if (updatedTask.getActualEndDate().isAfter(LocalDateTime.now()))
                throw new IllegalArgumentException("actualEndDate can't be in the future");

            if (hasActualStartDate)
                if (updatedTask.getActualEndDate().isBefore(updatedTask.getActualStartDate()))
                    throw new IllegalArgumentException("actualEndDate is before actualStartDate");
        }

        updatedTask = updateTask(updatedTask);

        if (updatedTask == null) 
            return null;

        long taskId = updatedTask.getTaskId();

        /* Update TaskAssignment (if present) */
        TaskAssignmentUpdateDTO taskAssignmentDTO = updatedTaskDTO.getTaskAssignment();
        if (taskAssignmentDTO != null) {
            /* Reset FK just in case */
            taskAssignmentDTO.setTaskId(taskId);

            if (taskAssignmentService.updateTaskAssignmentByDTO(taskAssignmentDTO) == null)
                throw new IllegalArgumentException("No TaskAssignment found with taskId: '" + taskId + "' to update");
        }

        return fillDTO(updatedTask);
    }

    /* DELETE */
    @Override
    public boolean deleteTask(long taskId) throws Exception {
        Task task = getById(taskId);

        if (task == null) {
            return false;
            /* Not found with Id */
        }

        /* TODO: also delete EntityWrapper for task */

        /* Delete associated taskAssignment */
        taskAssignmentService.deleteByTaskId(taskId);

        /* Delete all associated taskReport */
        taskReportService.deleteAllByTaskId(taskId);

        task.setIsDeleted(true);
        taskRepository.saveAndFlush(task);

        return true;
    }

    /* Utils */
    private TaskReadDTO fillDTO(Task task) throws Exception {
        long taskId = task.getTaskId();

        TaskReadDTO taskDTO = modelMapper.map(task, TaskReadDTO.class);

        /* Get associated taskAssignment */
        taskDTO.setTaskAssignment(
                taskAssignmentService.getDTOByTaskId(taskId));

        /* Get associated taskReport */
        taskDTO.setTaskReportList(
                taskReportService.getAllDTOByTaskId(taskId));

        return taskDTO;
    }

    private List<TaskReadDTO> fillAllDTO(List<Task> taskList, Integer totalPage) throws Exception {
        Set<Long> taskIdSet = new HashSet<>();

        for (Task task : taskList) {
            taskIdSet.add(task.getTaskId());
        }

        /* Get associated TaskAssignment */
        Map<Long, TaskAssignmentReadDTO> taskIdTaskAssignmentDTOMap =
                taskAssignmentService.mapTaskIdTaskAssignmentDTOByTaskIdIn(taskIdSet);

        /* Get associated TaskReport */
        Map<Long, List<TaskReportReadDTO>> taskIdTaskReportDTOListMap =
                taskReportService.mapTaskIdTaskReportDTOListByTaskIdIn(taskIdSet);

        return taskList.stream()
                .map(task -> {
                    TaskReadDTO taskReadDTO =
                            modelMapper.map(task, TaskReadDTO.class);

                    taskReadDTO.setTaskAssignment(taskIdTaskAssignmentDTOMap.get(task.getTaskId()));

                    taskReadDTO.setTaskReportList(taskIdTaskReportDTOListMap.get(task.getTaskId()));

                    taskReadDTO.setTotalPage(totalPage);

                    return taskReadDTO;})
                .collect(Collectors.toList());
    }
}
