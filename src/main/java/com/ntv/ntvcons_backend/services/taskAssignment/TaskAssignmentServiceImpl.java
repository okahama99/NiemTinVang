package com.ntv.ntvcons_backend.services.taskAssignment;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentUpdateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.entities.TaskAssignment;
import com.ntv.ntvcons_backend.repositories.TaskAssignmentRepository;
import com.ntv.ntvcons_backend.services.task.TaskService;
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
public class TaskAssignmentServiceImpl implements TaskAssignmentService {
    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private TaskService taskService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public TaskAssignment createTaskAssignment(TaskAssignment newTaskAssignment) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!taskService.existsById(newTaskAssignment.getTaskId())) {
            errorMsg += "No Task found with Id: '" + newTaskAssignment.getTaskId()
                    + "'. Which violate constraint: FK_TaskAssignment_Task. ";
        }
        if (!userService.existsById(newTaskAssignment.getAssignerId())) {
            errorMsg += "No User (Assigner) found with Id: '" + newTaskAssignment.getAssignerId()
                    + "'. Which violate constraint: FK_TaskAssignment_User_AssignerId. ";
        }
        if (!userService.existsById(newTaskAssignment.getAssigneeId())) {
            errorMsg += "No User (Assignee) found with Id: '" + newTaskAssignment.getAssigneeId()
                    + "'. Which violate constraint: FK_TaskAssignment_User_AssigneeId. ";
        }
        if (newTaskAssignment.getCreatedBy() != null) {
            if (!userService.existsById(newTaskAssignment.getCreatedBy())) {
                errorMsg += "No User (CreatedBy) found with Id: '" + newTaskAssignment.getAssigneeId()
                        + "'. Which violate constraint: FK_TaskAssignment_User_CreatedBy. ";
            }
        }

        /* Check duplicate */
        if (taskAssignmentRepository
                .existsByTaskIdAndAssignerIdAndAssigneeIdAndStatusNotIn(
                        newTaskAssignment.getTaskId(),
                        newTaskAssignment.getAssignerId(),
                        newTaskAssignment.getAssigneeId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another TaskAssignment relationship between with Task with Id: '"
                    + newTaskAssignment.getTaskId()
                    + "' and User (Assigner) with Id: '"
                    + newTaskAssignment.getAssignerId()
                    + "' and User (Assignee) with Id: '"
                    + newTaskAssignment.getAssigneeId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return taskAssignmentRepository.saveAndFlush(newTaskAssignment);
    }
    @Override
    public TaskAssignmentReadDTO createTaskAssignmentByDTO(TaskAssignmentCreateDTO newTaskAssignmentDTO) throws Exception {
        /* TODO: to use later or skip forever
        modelMapper.typeMap(TaskAssignmentCreateDTO.class, TaskAssignment.class)
                .addMappings(mapper -> mapper.skip(TaskAssignment::setAssignDate));*/

        TaskAssignment newTaskAssignment = modelMapper.map(newTaskAssignmentDTO, TaskAssignment.class);

        newTaskAssignment = createTaskAssignment(newTaskAssignment);

        return fillDTO(newTaskAssignment);
    }

    /* READ */
    @Override
    public Page<TaskAssignment> getPageAll(Pageable paging) throws Exception {
        Page<TaskAssignment> taskAssignmentPage =
                taskAssignmentRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (taskAssignmentPage.isEmpty()) 
            return null;

        return taskAssignmentPage;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<TaskAssignment> taskAssignmentPage = getPageAll(paging);

        if (taskAssignmentPage == null) 
            return null;

        List<TaskAssignment> taskAssignmentList = taskAssignmentPage.getContent();

        if (taskAssignmentList.isEmpty()) 
            return null;

        return fillAllDTO(taskAssignmentList, taskAssignmentPage.getTotalPages());
    }

    @Override
    public TaskAssignment getById(long assignmentId) throws Exception {
        return taskAssignmentRepository
                .findByAssignmentIdAndStatusNotIn(assignmentId, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public TaskAssignmentReadDTO getDTOById(long assignmentId) throws Exception {
        TaskAssignment taskAssignment = getById(assignmentId);

        if (taskAssignment == null) 
            return null;

        return fillDTO(taskAssignment);
    }

    @Override
    public List<TaskAssignment> getAllByIdIn(Collection<Long> assignmentIdCollection) throws Exception {
        List<TaskAssignment> taskAssignmentList =
                taskAssignmentRepository.findAllByAssignmentIdInAndStatusNotIn(assignmentIdCollection, N_D_S_STATUS_LIST);

        if (!taskAssignmentList.isEmpty()) 
            return null;

        return taskAssignmentList;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOByIdIn(Collection<Long> assignmentIdCollection) throws Exception {
        List<TaskAssignment> taskAssignmentList = getAllByIdIn(assignmentIdCollection);

        if (taskAssignmentList == null)
            return null;

        return fillAllDTO(taskAssignmentList, null);
    }

    @Override
    public TaskAssignment getByTaskId(long taskId) throws Exception {
        return taskAssignmentRepository
                .findByTaskIdAndStatusNotIn(taskId, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public TaskAssignmentReadDTO getDTOByTaskId(long taskId) throws Exception {
        TaskAssignment taskAssignment = getByTaskId(taskId);

        if (taskAssignment == null) 
            return null;

        return fillDTO(taskAssignment);
    }

    @Override
    public List<TaskAssignment> getAllByTaskIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<TaskAssignment> taskAssignmentList =
                taskAssignmentRepository.findAllByTaskIdInAndStatusNotIn(taskIdCollection, N_D_S_STATUS_LIST);
        
        if (taskAssignmentList.isEmpty())
            return null;
        
        return taskAssignmentList;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOByTaskIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<TaskAssignment> taskAssignmentList = getAllByTaskIdIn(taskIdCollection);
        
        if (taskAssignmentList == null) 
            return null;
        
        return fillAllDTO(taskAssignmentList, null);
    }
    @Override
    public Map<Long, TaskAssignmentReadDTO> mapTaskIdTaskAssignmentDTOByTaskIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<TaskAssignmentReadDTO> taskAssignmentDTOList = getAllDTOByTaskIdIn(taskIdCollection);
        
        if (taskAssignmentDTOList == null)
            return new HashMap<>();

        return taskAssignmentDTOList.stream()
                .collect(Collectors.toMap(TaskAssignmentReadDTO::getTaskId, Function.identity()));
    }
    @Override
    public Page<TaskAssignment> getPageAllByTaskIdIn(Pageable paging, Collection<Long> taskIdCollection) throws Exception {
        Page<TaskAssignment> taskAssignmentPage =
                taskAssignmentRepository.findAllByTaskIdInAndStatusNotIn(taskIdCollection, N_D_S_STATUS_LIST, paging);

        if (taskAssignmentPage.isEmpty())
            return null;

        return taskAssignmentPage;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOInPagingByTaskIdIn(Pageable paging, Collection<Long> taskIdCollection) throws Exception {
        Page<TaskAssignment> taskAssignmentPage = getPageAllByTaskIdIn(paging, taskIdCollection);

        if (taskAssignmentPage == null)
            return null;

        List<TaskAssignment> taskAssignmentList = taskAssignmentPage.getContent();

        if (taskAssignmentList.isEmpty())
            return null;

        return fillAllDTO(taskAssignmentList, taskAssignmentPage.getTotalPages());
    }

    @Override
    public List<TaskAssignment> getAllByAssignerId(long assignerId) throws Exception {
        List<TaskAssignment> taskAssignmentList =
                taskAssignmentRepository.findAllByAssignerIdAndStatusNotIn(assignerId, N_D_S_STATUS_LIST);

        if (!taskAssignmentList.isEmpty()) 
            return null;

        return taskAssignmentList;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOByAssignerId(long assignerId) throws Exception {
        List<TaskAssignment> taskAssignmentList = getAllByAssignerId(assignerId);

        if (taskAssignmentList == null) 
            return null;

        return fillAllDTO(taskAssignmentList, null);
    }
    @Override
    public Page<TaskAssignment> getPageAllByAssignerId(Pageable paging, long assignerId) throws Exception {
        Page<TaskAssignment> taskAssignmentPage =
                taskAssignmentRepository.findAllByAssignerIdAndStatusNotIn(assignerId, N_D_S_STATUS_LIST, paging);

        if (taskAssignmentPage.isEmpty())
            return null;

        return taskAssignmentPage;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOInPagingByAssignerId(Pageable paging, long assignerId) throws Exception {
        Page<TaskAssignment> taskAssignmentPage = getPageAllByAssignerId(paging, assignerId);

        if (taskAssignmentPage == null)
            return null;

        List<TaskAssignment> taskAssignmentList = taskAssignmentPage.getContent();

        if (taskAssignmentList.isEmpty())
            return null;

        return fillAllDTO(taskAssignmentList, taskAssignmentPage.getTotalPages());
    }

    @Override
    public List<TaskAssignment> getAllByAssigneeId(long assigneeId) throws Exception {
        List<TaskAssignment> taskAssignmentList =
                taskAssignmentRepository.findAllByAssigneeIdAndStatusNotIn(assigneeId, N_D_S_STATUS_LIST);

        if (!taskAssignmentList.isEmpty()) 
            return null;

        return taskAssignmentList;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOByAssigneeId(long assigneeId) throws Exception {
        List<TaskAssignment> taskAssignmentList = getAllByAssigneeId(assigneeId);

        if (taskAssignmentList == null) 
            return null;

        return fillAllDTO(taskAssignmentList, null);
    }
    @Override
    public Page<TaskAssignment> getPageAllByAssigneeId(Pageable paging, long assigneeId) throws Exception {
        Page<TaskAssignment> taskAssignmentPage =
                taskAssignmentRepository.findAllByAssigneeIdAndStatusNotIn(assigneeId, N_D_S_STATUS_LIST, paging);

        if (taskAssignmentPage.isEmpty())
            return null;

        return taskAssignmentPage;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOInPagingByAssigneeId(Pageable paging, long assigneeId) throws Exception {
        Page<TaskAssignment> taskAssignmentPage = getPageAllByAssigneeId(paging, assigneeId);

        if (taskAssignmentPage == null)
            return null;

        List<TaskAssignment> taskAssignmentList = taskAssignmentPage.getContent();

        if (taskAssignmentList.isEmpty())
            return null;

        return fillAllDTO(taskAssignmentList, taskAssignmentPage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public TaskAssignment updateTaskAssignment(TaskAssignment updatedTaskAssignment) throws Exception {
        TaskAssignment oldTaskAssignment = getById(updatedTaskAssignment.getAssignmentId());

        if (oldTaskAssignment == null) 
            return null;

        String errorMsg = "";

        /* Check input */
        if (updatedTaskAssignment.getRemoveDate() != null) {
            if (updatedTaskAssignment.getRemoveDate().isBefore(oldTaskAssignment.getAssignDate())) {
                errorMsg += "Invalid Input: removeDate is before assignDate. ";
            }
        }

        /* Check FK (if changed) */
        if (!oldTaskAssignment.getTaskId().equals(updatedTaskAssignment.getTaskId())) {
            if (!taskService.existsById(updatedTaskAssignment.getTaskId())) {
                errorMsg += "No Task found with Id: '" + updatedTaskAssignment.getTaskId()
                        + "'. Which violate constraint: FK_TaskAssignment_Task. ";
            }
        }
        if (!oldTaskAssignment.getAssignerId().equals(updatedTaskAssignment.getAssignerId())) {
            if (!userService.existsById(updatedTaskAssignment.getAssignerId())) {
                errorMsg += "No User (Assigner) found with Id: '" + updatedTaskAssignment.getAssignerId()
                        + "'. Which violate constraint: FK_TaskAssignment_User_AssignerId. ";
            }
        }
        if (!oldTaskAssignment.getAssigneeId().equals(updatedTaskAssignment.getAssigneeId())) {
            if (!userService.existsById(updatedTaskAssignment.getAssigneeId())) {
                errorMsg += "No User (Assignee) found with Id: '" + updatedTaskAssignment.getAssigneeId()
                        + "'. Which violate constraint: FK_TaskAssignment_User_AssigneeId. ";
            }
        }
        if (updatedTaskAssignment.getUpdatedBy() != null) {
            if (oldTaskAssignment.getUpdatedBy() != null) {
                if (!oldTaskAssignment.getUpdatedBy().equals(updatedTaskAssignment.getUpdatedBy())) {
                    if (!userService.existsById(updatedTaskAssignment.getUpdatedBy())) {
                        errorMsg += "No User (UpdatedBy) found with Id: '" + updatedTaskAssignment.getUpdatedBy()
                                + "'. Which violate constraint: FK_TaskAssignment_User_UpdatedBy. ";
                    }
                }
            } else {
                if (!userService.existsById(updatedTaskAssignment.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedTaskAssignment.getUpdatedBy()
                            + "'. Which violate constraint: FK_TaskAssignment_User_UpdatedBy. ";
                }
            }
        }

        /* Check duplicate */
        if (taskAssignmentRepository
                .existsByTaskIdAndAssignerIdAndAssigneeIdAndAssignmentIdIsNotAndStatusNotIn(
                        updatedTaskAssignment.getTaskId(),
                        updatedTaskAssignment.getAssignerId(),
                        updatedTaskAssignment.getAssigneeId(),
                        updatedTaskAssignment.getAssignmentId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another TaskAssignment relationship between with Task with Id: '"
                    + updatedTaskAssignment.getTaskId()
                    + "' and User (Assigner) with Id: '"
                    + updatedTaskAssignment.getAssignerId()
                    + "' and User (Assignee) with Id: '"
                    + updatedTaskAssignment.getAssigneeId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedTaskAssignment.setCreatedAt(oldTaskAssignment.getCreatedAt());
        updatedTaskAssignment.setCreatedBy(oldTaskAssignment.getCreatedBy());

        return taskAssignmentRepository.saveAndFlush(updatedTaskAssignment);
    }
    @Override
    public TaskAssignmentReadDTO updateTaskAssignmentByDTO(TaskAssignmentUpdateDTO updatedTaskAssignmentDTO) throws Exception {
        modelMapper.typeMap(TaskAssignmentUpdateDTO.class, TaskAssignment.class)
                .addMappings(mapper -> {
                    mapper.skip(TaskAssignment::setAssignDate);
                    mapper.skip(TaskAssignment::setRemoveDate);});

        TaskAssignment updatedTaskAssignment = modelMapper.map(updatedTaskAssignmentDTO, TaskAssignment.class);

        boolean updateAssignDate = false;
        if (updatedTaskAssignmentDTO.getAssignDate() != null) {
            updateAssignDate = true;

            updatedTaskAssignment.setAssignDate(
                    LocalDateTime.parse(updatedTaskAssignmentDTO.getAssignDate(), dateTimeFormatter));
        }

        if (updatedTaskAssignmentDTO.getRemoveDate() != null) {
            updatedTaskAssignment.setRemoveDate(
                    LocalDateTime.parse(updatedTaskAssignmentDTO.getRemoveDate(), dateTimeFormatter));

            if (updateAssignDate)
                if (updatedTaskAssignment.getRemoveDate().isBefore(updatedTaskAssignment.getAssignDate()))
                    throw new IllegalArgumentException("removeDate is before assignDate");
        }

        updatedTaskAssignment = updateTaskAssignment(updatedTaskAssignment);

        if (updatedTaskAssignment == null) 
            return null;

        return fillDTO(updatedTaskAssignment);
    }

    /* DELETE */
    @Override
    public boolean removeTaskAssignment(long assignmentId) throws Exception {
        TaskAssignment taskAssignment = getById(assignmentId);

        if (taskAssignment == null)
            return false;

        taskAssignment.setStatus(Status.REMOVED);
        taskAssignmentRepository.saveAndFlush(taskAssignment);

        return true;
    }

    @Override
    public boolean removeAllByUserId(long userId) throws Exception {
        List<TaskAssignment> taskAssignmentList = new ArrayList<>();

        List<TaskAssignment> taskAssignmentListByAssigner = getAllByAssignerId(userId);
        List<TaskAssignment> taskAssignmentListByAssignee = getAllByAssigneeId(userId);

        if (taskAssignmentListByAssigner != null)
            taskAssignmentList.addAll(taskAssignmentListByAssigner);
        if (taskAssignmentListByAssignee != null)
            taskAssignmentList.addAll(taskAssignmentListByAssignee);

        if (taskAssignmentList.isEmpty())
            return false;

        taskAssignmentList =
                taskAssignmentList.stream()
                        .peek(taskAssignment -> taskAssignment.setStatus(Status.REMOVED))
                        .collect(Collectors.toList());

        taskAssignmentRepository.saveAllAndFlush(taskAssignmentList);

        return true;
    }

    @Override
    public boolean removeByTaskId(long taskId) throws Exception {
        TaskAssignment taskAssignment = getByTaskId(taskId);

        if (taskAssignment == null)
            return false;

        taskAssignment.setStatus(Status.REMOVED);
        taskAssignmentRepository.saveAndFlush(taskAssignment);

        return true;
    }

    @Override
    public boolean deleteTaskAssignment(long assignmentId) throws Exception {
        TaskAssignment taskAssignment = getById(assignmentId);

        if (taskAssignment == null)
            return false;

        taskAssignment.setStatus(Status.DELETED);
        taskAssignmentRepository.saveAndFlush(taskAssignment);

        return true;
    }

    @Override
    public boolean deleteAllByUserId(long userId) throws Exception {
        List<TaskAssignment> taskAssignmentList = new ArrayList<>();

        List<TaskAssignment> taskAssignmentListByAssigner = getAllByAssignerId(userId);
        List<TaskAssignment> taskAssignmentListByAssignee = getAllByAssigneeId(userId);

        if (taskAssignmentListByAssigner != null)
            taskAssignmentList.addAll(taskAssignmentListByAssigner);
        if (taskAssignmentListByAssignee != null)
            taskAssignmentList.addAll(taskAssignmentListByAssignee);

        if (taskAssignmentList.isEmpty())
            return false;

        taskAssignmentList =
                taskAssignmentList.stream()
                        .peek(taskAssignment -> taskAssignment.setStatus(Status.DELETED))
                        .collect(Collectors.toList());

        taskAssignmentRepository.saveAllAndFlush(taskAssignmentList);

        return true;
    }

    @Override
    public boolean deleteByTaskId(long taskId) throws Exception {
        TaskAssignment taskAssignment = getByTaskId(taskId);

        if (taskAssignment == null)
            return false;

        taskAssignment.setStatus(Status.DELETED);
        taskAssignmentRepository.saveAndFlush(taskAssignment);

        return true;
    }
    @Override
    public boolean deleteAllByTaskIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<TaskAssignment> taskAssignmentList = getAllByTaskIdIn(taskIdCollection);

        if (taskAssignmentList == null)
            return false;

        taskAssignmentList =
                taskAssignmentList.stream()
                        .peek(taskAssignment -> taskAssignment.setStatus(Status.DELETED))
                        .collect(Collectors.toList());

        taskAssignmentRepository.saveAllAndFlush(taskAssignmentList);

        return true;
    }
    
    /* Utils */
    private TaskAssignmentReadDTO fillDTO(TaskAssignment taskAssignment) throws Exception {
        modelMapper.typeMap(TaskAssignment.class, TaskAssignmentReadDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(TaskAssignmentReadDTO::setAssignDate);
                    mapper.skip(TaskAssignmentReadDTO::setRemoveDate);
                    mapper.skip(TaskAssignmentReadDTO::setCreatedAt);
                    mapper.skip(TaskAssignmentReadDTO::setUpdatedAt);});

        TaskAssignmentReadDTO taskAssignmentDTO =
                modelMapper.map(taskAssignment, TaskAssignmentReadDTO.class);

        if (taskAssignment.getAssignDate() != null)
            taskAssignmentDTO.setAssignDate(taskAssignment.getAssignDate().format(dateTimeFormatter));
        if (taskAssignment.getRemoveDate() != null)
            taskAssignmentDTO.setRemoveDate(taskAssignment.getRemoveDate().format(dateTimeFormatter));
        if (taskAssignment.getCreatedAt() != null)
            taskAssignmentDTO.setCreatedAt(taskAssignment.getCreatedAt().format(dateTimeFormatter));
        if (taskAssignment.getUpdatedAt() != null)
            taskAssignmentDTO.setUpdatedAt(taskAssignment.getUpdatedAt().format(dateTimeFormatter));

        Set<Long> userIdSet = new HashSet<>();

        userIdSet.add(taskAssignment.getAssignerId());
        userIdSet.add(taskAssignment.getAssigneeId());

        /* Get all associated User (Assigner & Assignee) */
        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(userIdSet);
        
        /* NOT NULL */
        /* Set associated User (Assigner) */
        taskAssignmentDTO.setAssigner(
                userIdUserDTOMap.get(taskAssignment.getAssignerId()));
        /* Set associated User (Assignee) */
        taskAssignmentDTO.setAssignee(
                userIdUserDTOMap.get(taskAssignment.getAssigneeId()));

        return taskAssignmentDTO;
    }
    
    private List<TaskAssignmentReadDTO> fillAllDTO(Collection<TaskAssignment> taskAssignmentCollection, Integer totalPage) throws Exception {
        modelMapper.typeMap(TaskAssignment.class, TaskAssignmentReadDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(TaskAssignmentReadDTO::setAssignDate);
                    mapper.skip(TaskAssignmentReadDTO::setRemoveDate);
                    mapper.skip(TaskAssignmentReadDTO::setCreatedAt);
                    mapper.skip(TaskAssignmentReadDTO::setUpdatedAt);});

        Set<Long> userIdSet = new HashSet<>();

        for (TaskAssignment taskAssignment : taskAssignmentCollection) {
            userIdSet.add(taskAssignment.getAssignerId());
            userIdSet.add(taskAssignment.getAssigneeId());
        }

        /* Get all associated User (Assigner & Assignee) */
        Map<Long, UserReadDTO> userIdUserDTOMap = 
                userService.mapUserIdUserDTOByIdIn(userIdSet);

        return taskAssignmentCollection.stream()
                .map(taskAssignment -> {
                    TaskAssignmentReadDTO taskAssignmentDTO =
                            modelMapper.map(taskAssignment, TaskAssignmentReadDTO.class);

                    if (taskAssignment.getAssignDate() != null)
                        taskAssignmentDTO.setAssignDate(taskAssignment.getAssignDate().format(dateTimeFormatter));
                    if (taskAssignment.getRemoveDate() != null)
                        taskAssignmentDTO.setRemoveDate(taskAssignment.getRemoveDate().format(dateTimeFormatter));
                    if (taskAssignment.getCreatedAt() != null)
                        taskAssignmentDTO.setCreatedAt(taskAssignment.getCreatedAt().format(dateTimeFormatter));
                    if (taskAssignment.getUpdatedAt() != null)
                        taskAssignmentDTO.setUpdatedAt(taskAssignment.getUpdatedAt().format(dateTimeFormatter));

                    taskAssignmentDTO.setAssigner(userIdUserDTOMap.get(taskAssignment.getAssignerId()));
                    taskAssignmentDTO.setAssignee(userIdUserDTOMap.get(taskAssignment.getAssigneeId()));
                    
                    taskAssignmentDTO.setTotalPage(totalPage);

                    return taskAssignmentDTO;})
                .collect(Collectors.toList());
    }
}
