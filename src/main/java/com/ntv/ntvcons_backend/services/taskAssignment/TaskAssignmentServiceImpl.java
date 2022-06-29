package com.ntv.ntvcons_backend.services.taskAssignment;

import com.ntv.ntvcons_backend.dtos.role.RoleReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentUpdateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.entities.TaskAssignment;
import com.ntv.ntvcons_backend.repositories.TaskAssignmentRepository;
import com.ntv.ntvcons_backend.services.task.TaskService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskAssignmentServiceImpl implements TaskAssignmentService{
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
        if (!userService.existsById(newTaskAssignment.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newTaskAssignment.getAssigneeId()
                    + "'. Which violate constraint: FK_TaskAssignment_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (taskAssignmentRepository
                .existsByTaskIdAndAssignerIdAndAssigneeIdAndIsDeletedIsFalse(
                        newTaskAssignment.getTaskId(),
                        newTaskAssignment.getAssignerId(),
                        newTaskAssignment.getAssigneeId())) {
            errorMsg += "Already exists another TaskAssignment relationship between with Task with Id: '"
                    + newTaskAssignment.getTaskId()
                    + "' and User (Assigner) with Id: '"
                    + newTaskAssignment.getAssignerId()
                    + "' and User (Assignee) with Id: '"
                    + newTaskAssignment.getAssigneeId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return taskAssignmentRepository.saveAndFlush(newTaskAssignment);
    }
    @Override
    public TaskAssignmentReadDTO createTaskAssignmentByDTO(TaskAssignmentCreateDTO newTaskAssignmentDTO) throws Exception {
        modelMapper.typeMap(TaskAssignmentCreateDTO.class, TaskAssignment.class)
                .addMappings(mapper -> mapper.skip(TaskAssignment::setAssignDate));

        TaskAssignment newTaskAssignment = modelMapper.map(newTaskAssignmentDTO, TaskAssignment.class);

        if (newTaskAssignmentDTO.getAssignDate() != null) {
            newTaskAssignment.setAssignDate(
                    LocalDateTime.parse(newTaskAssignmentDTO.getAssignDate(), dateTimeFormatter));
        } else { /* if null, set current datetime */
            newTaskAssignment.setAssignDate(LocalDateTime.now());
        }

        newTaskAssignment = createTaskAssignment(newTaskAssignment);

        TaskAssignmentReadDTO taskAssignmentDTO =
                modelMapper.map(newTaskAssignment, TaskAssignmentReadDTO.class);

        /* Get associated User (Assigner) */
        taskAssignmentDTO.setAssigner(userService.getDTOById(newTaskAssignment.getAssignerId()));

        /* Get associated User (Assignee) */
        taskAssignmentDTO.setAssignee(userService.getDTOById(newTaskAssignment.getAssigneeId()));

        return taskAssignmentDTO;
    }

    /* READ */
    @Override
    public Page<TaskAssignment> getPageAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<TaskAssignment> taskAssignmentPage = taskAssignmentRepository.findAllByIsDeletedIsFalse(paging);

        if (taskAssignmentPage.isEmpty()) {
            return null;
        }

        return taskAssignmentPage;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOInPaging(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Page<TaskAssignment> taskAssignmentPage = getPageAll(pageNo, pageSize, sortBy, sortType);

        if (taskAssignmentPage == null) {
            return null;
        }

        List<TaskAssignment> taskAssignmentList = taskAssignmentPage.getContent();

        if (taskAssignmentList.isEmpty()) {
            return null;
        }

        int totalPage = taskAssignmentPage.getTotalPages();

        Set<Long> userIdSet = new HashSet<>();

        for (TaskAssignment taskAssignment : taskAssignmentList) {
            userIdSet.add(taskAssignment.getAssignerId());
            userIdSet.add(taskAssignment.getAssigneeId());
        }

        /* Get associated User (Assigner & Assignee) */
        Map<Long, UserReadDTO> userIdUserDTOMap = userService.mapUserIdUserDTOByIdIn(userIdSet);

        return taskAssignmentList.stream()
                .map(taskAssignment -> {
                    TaskAssignmentReadDTO TaskAssignmentDTO =
                            modelMapper.map(taskAssignment, TaskAssignmentReadDTO.class);

                    TaskAssignmentDTO.setAssigner(userIdUserDTOMap.get(taskAssignment.getAssignerId()));
                    TaskAssignmentDTO.setAssignee(userIdUserDTOMap.get(taskAssignment.getAssigneeId()));
                    TaskAssignmentDTO.setTotalPage(totalPage);

                    return TaskAssignmentDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public TaskAssignment getById(long assignmentId) throws Exception {
        return taskAssignmentRepository
                .findByAssignmentIdAndIsDeletedIsFalse(assignmentId)
                .orElse(null);
    }
    @Override
    public TaskAssignmentReadDTO getDTOById(long assignmentId) throws Exception {
        TaskAssignment taskAssignment = getById(assignmentId);

        if (taskAssignment == null) {
            return null;
        }

        TaskAssignmentReadDTO taskAssignmentDTO = modelMapper.map(taskAssignment, TaskAssignmentReadDTO.class);

        /* Get associated User (Assigner) */
        taskAssignmentDTO.setAssigner(userService.getDTOById(taskAssignment.getAssignerId()));

        /* Get associated User (Assignee) */
        taskAssignmentDTO.setAssigner(userService.getDTOById(taskAssignment.getAssignerId()));

        return taskAssignmentDTO;
    }

    @Override
    public List<TaskAssignment> getAllByIdIn(Collection<Long> assignmentIdCollection) throws Exception {
        List<TaskAssignment> taskAssignmentList =
                taskAssignmentRepository.findAllByAssignmentIdInAndIsDeletedIsFalse(assignmentIdCollection);

        if (!taskAssignmentList.isEmpty()) {
            return null;
        }

        return taskAssignmentList;
    }

    @Override
    public TaskAssignment getByTaskId(long taskId) throws Exception {
        return taskAssignmentRepository
                .findByTaskIdAndIsDeletedIsFalse(taskId)
                .orElse(null);
    }
    @Override
    public TaskAssignmentReadDTO getDTOByTaskId(long taskId) throws Exception {
        TaskAssignment taskAssignment = getByTaskId(taskId);

        if (taskAssignment == null) {
            return null;
        }

        TaskAssignmentReadDTO taskAssignmentDTO =
                modelMapper.map(taskAssignment, TaskAssignmentReadDTO.class);

        /* Get associated User (Assigner) */
        taskAssignmentDTO.setAssigner(userService.getDTOById(taskAssignment.getAssignerId()));

        /* Get associated User (Assignee) */
        taskAssignmentDTO.setAssignee(userService.getDTOById(taskAssignment.getAssigneeId()));

        return taskAssignmentDTO;
    }

    @Override
    public List<TaskAssignment> getAllByAssignerId(long assignerId) throws Exception {
        List<TaskAssignment> taskAssignmentList =
                taskAssignmentRepository.findAllByAssignerIdAndIsDeletedIsFalse(assignerId);

        if (!taskAssignmentList.isEmpty()) {
            return null;
        }

        return taskAssignmentList;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOByAssignerId(long assignerId) throws Exception {
        List<TaskAssignment> taskAssignmentList = getAllByAssignerId(assignerId);

        if (taskAssignmentList == null) {
            return null;
        }

        Set<Long> userIdSet = new HashSet<>();

        for (TaskAssignment taskAssignment : taskAssignmentList) {
            userIdSet.add(taskAssignment.getAssignerId());
            userIdSet.add(taskAssignment.getAssigneeId());
        }

        /* Get all associated User (Assigner & Assignee) */
        Map<Long, UserReadDTO> userIdUserDTOMap = userService.mapUserIdUserDTOByIdIn(userIdSet);

        return taskAssignmentList.stream()
                .map(taskAssignment -> {
                    TaskAssignmentReadDTO TaskAssignmentDTO =
                            modelMapper.map(taskAssignment, TaskAssignmentReadDTO.class);

                    TaskAssignmentDTO.setAssigner(userIdUserDTOMap.get(taskAssignment.getAssignerId()));
                    TaskAssignmentDTO.setAssignee(userIdUserDTOMap.get(taskAssignment.getAssigneeId()));

                    return TaskAssignmentDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskAssignment> getAllByAssigneeId(long assigneeId) throws Exception {
        List<TaskAssignment> taskAssignmentList =
                taskAssignmentRepository.findAllByAssigneeIdAndIsDeletedIsFalse(assigneeId);

        if (!taskAssignmentList.isEmpty()) {
            return null;
        }

        return taskAssignmentList;
    }
    @Override
    public List<TaskAssignmentReadDTO> getAllDTOByAssigneeId(long assigneeId) throws Exception {
        List<TaskAssignment> taskAssignmentList = getAllByAssigneeId(assigneeId);

        if (taskAssignmentList == null) {
            return null;
        }

        Set<Long> userIdSet = new HashSet<>();

        for (TaskAssignment taskAssignment : taskAssignmentList) {
            userIdSet.add(taskAssignment.getAssignerId());
            userIdSet.add(taskAssignment.getAssigneeId());
        }

        /* Get all associated User (Assigner & Assignee) */
        Map<Long, UserReadDTO> userIdUserDTOMap = userService.mapUserIdUserDTOByIdIn(userIdSet);

        return taskAssignmentList.stream()
                .map(taskAssignment -> {
                    TaskAssignmentReadDTO TaskAssignmentDTO =
                            modelMapper.map(taskAssignment, TaskAssignmentReadDTO.class);

                    TaskAssignmentDTO.setAssigner(userIdUserDTOMap.get(taskAssignment.getAssignerId()));
                    TaskAssignmentDTO.setAssignee(userIdUserDTOMap.get(taskAssignment.getAssigneeId()));

                    return TaskAssignmentDTO;})
                .collect(Collectors.toList());
    }

    /* UPDATE */
    @Override
    public TaskAssignment updateTaskAssignment(TaskAssignment updatedTaskAssignment) throws Exception {
        TaskAssignment oldTaskAssignment = getById(updatedTaskAssignment.getAssignmentId());

        if (oldTaskAssignment == null) {
            return null;
        }

        String errorMsg = "";

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
        if (!oldTaskAssignment.getUpdatedBy().equals(updatedTaskAssignment.getUpdatedBy())) {
            if (!userService.existsById(updatedTaskAssignment.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedTaskAssignment.getUpdatedBy()
                        + "'. Which violate constraint: FK_TaskAssignment_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (taskAssignmentRepository
                .existsByTaskIdAndAssignerIdAndAssigneeIdAndAssignmentIdIsNotAndIsDeletedIsFalse(
                        updatedTaskAssignment.getTaskId(),
                        updatedTaskAssignment.getAssignerId(),
                        updatedTaskAssignment.getAssigneeId(),
                        updatedTaskAssignment.getAssignmentId())) {
            errorMsg += "Already exists another TaskAssignment relationship between with Task with Id: '"
                    + updatedTaskAssignment.getTaskId()
                    + "' and User (Assigner) with Id: '"
                    + updatedTaskAssignment.getAssignerId()
                    + "' and User (Assignee) with Id: '"
                    + updatedTaskAssignment.getAssigneeId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return taskAssignmentRepository.saveAndFlush(updatedTaskAssignment);
    }
    @Override
    public TaskAssignmentReadDTO updateTaskAssignmentByDTO(TaskAssignmentUpdateDTO updatedTaskAssignmentDTO) throws Exception {
        modelMapper.typeMap(TaskAssignmentUpdateDTO.class, TaskAssignment.class)
                .addMappings(mapper -> {
                    mapper.skip(TaskAssignment::setAssignDate);
                    mapper.skip(TaskAssignment::setRemoveDate);});

        TaskAssignment updatedTaskAssignment = modelMapper.map(updatedTaskAssignmentDTO, TaskAssignment.class);

        if (updatedTaskAssignmentDTO.getAssignDate() != null) {
            updatedTaskAssignment.setAssignDate(
                    LocalDateTime.parse(updatedTaskAssignmentDTO.getAssignDate(), dateTimeFormatter));
        }
        if (updatedTaskAssignmentDTO.getRemoveDate() != null) {
            updatedTaskAssignment.setRemoveDate(
                    LocalDateTime.parse(updatedTaskAssignmentDTO.getRemoveDate(), dateTimeFormatter));
        }

        updatedTaskAssignment = updateTaskAssignment(updatedTaskAssignment);

        if (updatedTaskAssignment == null) {
            return null;
        }

        TaskAssignmentReadDTO taskAssignmentDTO =
                modelMapper.map(updatedTaskAssignment, TaskAssignmentReadDTO.class);

        /* Get associated User (Assigner) */
        taskAssignmentDTO.setAssigner(userService.getDTOById(updatedTaskAssignment.getAssignerId()));

        /* Get associated User (Assignee) */
        taskAssignmentDTO.setAssignee(userService.getDTOById(updatedTaskAssignment.getAssigneeId()));

        return taskAssignmentDTO;
    }

    /* DELETE */
    @Override
    public boolean deleteTaskAssignment(long assignmentId) throws Exception {
        TaskAssignment taskAssignment = getById(assignmentId);

        if (taskAssignment == null) {
            return false;
        }

        taskAssignment.setIsDeleted(true);
        taskAssignmentRepository.saveAndFlush(taskAssignment);

        return true;
    }

    @Override
    public boolean deleteAllByUserId(long userId) throws Exception {
        List<TaskAssignment> taskAssignmentList = getAllByAssignerId(userId);

        if (taskAssignmentList == null) {
            taskAssignmentList = getAllByAssigneeId(userId);

            if (taskAssignmentList == null) {
                return false;
            }
        }

        taskAssignmentList =
                taskAssignmentList.stream()
                        .peek(taskAssignment -> taskAssignment.setIsDeleted(true))
                        .collect(Collectors.toList());

        taskAssignmentRepository.saveAllAndFlush(taskAssignmentList);

        return true;
    }

    @Override
    public boolean deleteByTaskId(long taskId) throws Exception {
        TaskAssignment taskAssignment = getByTaskId(taskId);

        if (taskAssignment == null) {
            return false;
        }

        taskAssignment.setIsDeleted(true);
        taskAssignmentRepository.saveAndFlush(taskAssignment);

        return true;
    }
}
