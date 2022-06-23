package com.ntv.ntvcons_backend.services.taskAssignment;

import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentReadDTO;
import com.ntv.ntvcons_backend.dtos.taskAssignment.TaskAssignmentUpdateDTO;
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
            errorMsg += "No Task found with Id: " + newTaskAssignment.getTaskId()
                    + ". Which violate constraint: FK_TaskAssignment_Task. ";
        }

        if (!userService.existsById(newTaskAssignment.getAssignerId())) {
            errorMsg += "No User (Assigner) found with Id: " + newTaskAssignment.getAssignerId()
                    + ". Which violate constraint: FK_TaskAssignment_User_AssignerId. ";
        }

        if (!userService.existsById(newTaskAssignment.getAssigneeId())) {
            errorMsg += "No User (Assignee) found with Id: " + newTaskAssignment.getAssigneeId()
                    + ". Which violate constraint: FK_TaskAssignment_User_AssigneeId. ";
        }

        /* Check duplicate */
        if (taskAssignmentRepository
                .existsByTaskIdAndAssignerIdAndAssigneeIdAndIsDeletedIsFalse(
                        newTaskAssignment.getTaskId(),
                        newTaskAssignment.getAssignerId(),
                        newTaskAssignment.getAssigneeId())) {
            errorMsg += "Already exists another TaskAssignment relationship between with Task with Id: "
                    + newTaskAssignment.getTaskId()
                    + " and User (Assigner) with Id: "
                    + newTaskAssignment.getAssignerId()
                    + " and User (Assignee) with Id: "
                    + newTaskAssignment.getAssigneeId() + ". ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return taskAssignmentRepository.saveAndFlush(newTaskAssignment);
    }

    @Override
    public List<TaskAssignment> createBulkTaskAssignment(List<TaskAssignment> newTaskAssignmentList) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        Map<Long, Map<Long, List<Long>>> taskIdAssignerIdAssigneeIdListMapMap = new HashMap<>();
        Set<Long> taskIdSet = new HashSet<>();
        Set<Long> assignerIdSet = new HashSet<>();
        Set<Long> assigneeIdSet = new HashSet<>();
        Map<Long, List<Long>> tmpAssignerIdAssigneeIdListMap;
        List<Long> tmpAssigneeIdList;
        boolean isDuplicated = false;

        for (TaskAssignment newTaskAssignment : newTaskAssignmentList) {
            taskIdSet.add(newTaskAssignment.getTaskId());
            assignerIdSet.add(newTaskAssignment.getAssignerId());
            assigneeIdSet.add(newTaskAssignment.getAssigneeId());

            /* Check duplicate 1 (within input) */
            tmpAssignerIdAssigneeIdListMap =
                    taskIdAssignerIdAssigneeIdListMapMap.get(newTaskAssignment.getTaskId());

            if (tmpAssignerIdAssigneeIdListMap == null) {
                tmpAssignerIdAssigneeIdListMap = new HashMap<>();

                tmpAssignerIdAssigneeIdListMap.put(
                        newTaskAssignment.getAssignerId(),
                        new ArrayList<>(Collections.singletonList(newTaskAssignment.getAssigneeId())));

                taskIdAssignerIdAssigneeIdListMapMap.put(
                        newTaskAssignment.getTaskId(),
                        tmpAssignerIdAssigneeIdListMap);
            } else {
                tmpAssigneeIdList =
                        tmpAssignerIdAssigneeIdListMap.get(newTaskAssignment.getAssignerId());

                if (tmpAssigneeIdList.contains(newTaskAssignment.getAssigneeId())) {
                    isDuplicated = true;

                    errorMsg.append("Duplicate TaskAssignment relationship between with Task with Id: ")
                            .append(newTaskAssignment.getTaskId())
                            .append(" and User (Assigner) with Id: ")
                            .append(newTaskAssignment.getAssignerId())
                            .append(" and User (Assignee) with Id: ")
                            .append(newTaskAssignment.getAssigneeId()).append(". ");
                } else {
                    tmpAssigneeIdList.add(newTaskAssignment.getAssigneeId());

                    tmpAssignerIdAssigneeIdListMap.put(
                            newTaskAssignment.getAssignerId(),
                            tmpAssigneeIdList);

                    taskIdAssignerIdAssigneeIdListMapMap.put(
                            newTaskAssignment.getTaskId(),
                            tmpAssignerIdAssigneeIdListMap);
                }
            }
        }

        /* Check FK */
        if (!taskService.existsAllByIdIn(taskIdSet)) {
            errorMsg.append("1 or more Task not found with Id. Which violate constraint: FK_TaskAssignment_Task. ");
        }

        if (!userService.existsAllByIdIn(assignerIdSet)) {
            errorMsg.append("1 or more User (Assigner) not found with Id. Which violate constraint: FK_TaskAssignment_User_AssignerId. ");
        }

        if (!userService.existsAllByIdIn(assigneeIdSet)) {
            errorMsg.append("1 or more User (Assignee) not found with Id. Which violate constraint: FK_TaskAssignment_User_AssigneeId. ");
        }

        /* Already has duplicated within input, no need to check with DB */
        if (!isDuplicated) {
            /* Check duplicate 2 (in DB) */
            for (TaskAssignment newTaskAssignment : newTaskAssignmentList) {
                /* TODO: bulk check instead of loop */
                if (taskAssignmentRepository
                        .existsByTaskIdAndAssignerIdAndAssigneeIdAndIsDeletedIsFalse(
                                newTaskAssignment.getTaskId(),
                                newTaskAssignment.getAssignerId(),
                                newTaskAssignment.getAssigneeId())) {
                    errorMsg.append("Already exists another TaskAssignment relationship between with Task with Id: ")
                            .append(newTaskAssignment.getTaskId())
                            .append(" and User (Assigner) with Id: ")
                            .append(newTaskAssignment.getAssignerId())
                            .append(" and User (Assignee) with Id: ")
                            .append(newTaskAssignment.getAssigneeId()).append(". ");
                }
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        return taskAssignmentRepository.saveAllAndFlush(newTaskAssignmentList);
    }

    /* READ */
    @Override
    public List<TaskAssignment> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
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

        return taskAssignmentPage.getContent();
    }

    @Override
    public TaskAssignment getById(long taskAssignmentId) throws Exception {
        return taskAssignmentRepository
                .findByAssignmentIdAndIsDeletedIsFalse(taskAssignmentId)
                .orElse(null);
    }

    @Override
    public List<TaskAssignment> getAllByIdIn(Collection<Long> taskAssignmentIdCollection) throws Exception {
        List<TaskAssignment> taskAssignmentList =
                taskAssignmentRepository.findAllByAssignmentIdInAndIsDeletedIsFalse(taskAssignmentIdCollection);

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
    public List<TaskAssignment> getAllByAssigneeId(long assigneeId) throws Exception {
        List<TaskAssignment> taskAssignmentList =
                taskAssignmentRepository.findAllByAssigneeIdAndIsDeletedIsFalse(assigneeId);

        if (!taskAssignmentList.isEmpty()) {
            return null;
        }

        return taskAssignmentList;
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
                errorMsg += "No Task found with Id: " + updatedTaskAssignment.getTaskId()
                        + ". Which violate constraint: FK_TaskAssignment_Task. ";
            }
        }

        if (!oldTaskAssignment.getAssignerId().equals(updatedTaskAssignment.getAssignerId())) {
            if (!userService.existsById(updatedTaskAssignment.getAssignerId())) {
                errorMsg += "No User (Assigner) found with Id: " + updatedTaskAssignment.getAssignerId()
                        + ". Which violate constraint: FK_TaskAssignment_User_AssignerId. ";
            }
        }

        if (!oldTaskAssignment.getAssigneeId().equals(updatedTaskAssignment.getAssigneeId())) {
            if (!userService.existsById(updatedTaskAssignment.getAssigneeId())) {
                errorMsg += "No User (Assignee) found with Id: " + updatedTaskAssignment.getAssigneeId()
                        + ". Which violate constraint: FK_TaskAssignment_User_AssigneeId. ";
            }
        }

        /* Check duplicate */
        if (taskAssignmentRepository
                .existsByTaskIdAndAssignerIdAndAssigneeIdAndAssignmentIdIsNotAndIsDeletedIsFalse(
                        updatedTaskAssignment.getTaskId(),
                        updatedTaskAssignment.getAssignerId(),
                        updatedTaskAssignment.getAssigneeId(),
                        updatedTaskAssignment.getAssignmentId())) {
            errorMsg += "Already exists another TaskAssignment relationship between with Task with Id: "
                    + updatedTaskAssignment.getTaskId()
                    + " and User (Assigner) with Id: "
                    + updatedTaskAssignment.getAssignerId()
                    + " and User (Assignee) with Id: "
                    + updatedTaskAssignment.getAssigneeId() + ". ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return taskAssignmentRepository.saveAndFlush(updatedTaskAssignment);
    }
    @Override
    public TaskAssignmentReadDTO updateTaskAssignmentByDTO(TaskAssignmentUpdateDTO updatedTaskAssignmentDTO) throws Exception {
        modelMapper.typeMap(TaskAssignmentUpdateDTO.class, TaskAssignment.class)
                .addMappings(taskAssignmentUpdateDTO -> {
                    taskAssignmentUpdateDTO.skip(TaskAssignment::setAssignDate);
                    taskAssignmentUpdateDTO.skip(TaskAssignment::setRemoveDate);});

        TaskAssignment updatedTaskAssignment = modelMapper.map(updatedTaskAssignmentDTO, TaskAssignment.class);

        updatedTaskAssignment.setAssignDate(
                LocalDateTime.parse(updatedTaskAssignmentDTO.getAssignDate(), dateTimeFormatter));
        updatedTaskAssignment.setRemoveDate(
                LocalDateTime.parse(updatedTaskAssignmentDTO.getRemoveDate(), dateTimeFormatter));

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

    @Override
    public List<TaskAssignment> updateBulkTaskAssignment(List<TaskAssignment> updatedTaskAssignmentList) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        Map<Long, Map<Long, List<Long>>> taskIdAssignerIdAssigneeIdListMapMap = new HashMap<>();
        Set<Long> assignmentIdSet = new HashSet<>();
        Set<Long> oldTaskIdSet = new HashSet<>();
        Set<Long> oldAssignerIdSet = new HashSet<>();
        Set<Long> oldAssigneeIdSet = new HashSet<>();
        Set<Long> updatedTaskIdSet = new HashSet<>();
        Set<Long> updatedAssignerIdSet = new HashSet<>();
        Set<Long> updatedAssigneeIdSet = new HashSet<>();
        Map<Long, List<Long>> tmpAssignerIdAssigneeIdListMap;
        List<Long> tmpAssigneeIdList;
        boolean isDuplicated = false;

        for (TaskAssignment updatedTaskAssignment : updatedTaskAssignmentList) {
            assignmentIdSet.add(updatedTaskAssignment.getAssignmentId());

            updatedTaskIdSet.add(updatedTaskAssignment.getTaskId());
            updatedAssignerIdSet.add(updatedTaskAssignment.getAssignerId());
            updatedAssigneeIdSet.add(updatedTaskAssignment.getAssigneeId());

            /* Check duplicate 1 (within input) */
            tmpAssignerIdAssigneeIdListMap =
                    taskIdAssignerIdAssigneeIdListMapMap.get(updatedTaskAssignment.getTaskId());

            if (tmpAssignerIdAssigneeIdListMap == null) {
                tmpAssignerIdAssigneeIdListMap = new HashMap<>();

                tmpAssignerIdAssigneeIdListMap.put(
                        updatedTaskAssignment.getAssignerId(),
                        new ArrayList<>(Collections.singletonList(updatedTaskAssignment.getAssigneeId())));

                taskIdAssignerIdAssigneeIdListMapMap.put(
                        updatedTaskAssignment.getTaskId(),
                        tmpAssignerIdAssigneeIdListMap);
            } else {
                tmpAssigneeIdList =
                        tmpAssignerIdAssigneeIdListMap.get(updatedTaskAssignment.getAssignerId());

                if (tmpAssigneeIdList.contains(updatedTaskAssignment.getAssigneeId())) {
                    isDuplicated = true;

                    errorMsg.append("Duplicate TaskAssignment relationship between with Task with Id: ")
                            .append(updatedTaskAssignment.getTaskId())
                            .append(" and User (Assigner) with Id: ")
                            .append(updatedTaskAssignment.getAssignerId())
                            .append(" and User (Assignee) with Id: ")
                            .append(updatedTaskAssignment.getAssigneeId()).append(". ");
                } else {
                    tmpAssigneeIdList.add(updatedTaskAssignment.getAssigneeId());

                    tmpAssignerIdAssigneeIdListMap.put(
                            updatedTaskAssignment.getAssignerId(),
                            tmpAssigneeIdList);

                    taskIdAssignerIdAssigneeIdListMapMap.put(
                            updatedTaskAssignment.getTaskId(),
                            tmpAssignerIdAssigneeIdListMap);
                }
            }
        }

        List<TaskAssignment> oldTaskAssignmentList = getAllByIdIn(assignmentIdSet);

        if (oldTaskAssignmentList == null) {
            return null;
        }

        for (TaskAssignment oldTaskAssignment : oldTaskAssignmentList) {
            oldTaskIdSet.add(oldTaskAssignment.getTaskId());
            oldAssignerIdSet.add(oldTaskAssignment.getAssignerId());
            oldAssigneeIdSet.add(oldTaskAssignment.getAssigneeId());
        }

        /* Remove all unchanged taskId & assignerId & assigneeId */
        updatedTaskIdSet.removeAll(oldTaskIdSet);
        updatedAssignerIdSet.removeAll(oldAssignerIdSet);
        updatedAssigneeIdSet.removeAll(oldAssigneeIdSet);

        /* Check FK (if changed) */
        /* If there are updated taskId, need to recheck FK */
        if (!updatedTaskIdSet.isEmpty()) {
            if (!taskService.existsAllByIdIn(updatedTaskIdSet)) {
                errorMsg.append("1 or more Task not found with Id. Which violate constraint: FK_TaskAssignment_Task. ");
            }
        }

        /* If there are updated assignerId, need to recheck FK */
        if (!updatedAssignerIdSet.isEmpty()) {
            if (!userService.existsAllByIdIn(updatedAssignerIdSet)) {
                errorMsg.append("1 or more User (Assigner) not found with Id. Which violate constraint: FK_TaskAssignment_User_AssignerId. ");
            }
        }

        /* If there are updated assigneeId, need to recheck FK */
        if (!updatedAssigneeIdSet.isEmpty()) {
            if (!userService.existsAllByIdIn(updatedAssigneeIdSet)) {
                errorMsg.append("1 or more User (Assignee) not found with Id. Which violate constraint: FK_TaskAssignment_User_AssigneeId. ");
            }
        }

        /* Already has duplicated within input, no need to check with DB */
        if (!isDuplicated) {
            /* Check duplicate 2 (in DB) */
            for (TaskAssignment updatedTaskAssignment : updatedTaskAssignmentList) {
                /* TODO: bulk check instead of loop */
                if (taskAssignmentRepository
                        .existsByTaskIdAndAssignerIdAndAssigneeIdAndAssignmentIdIsNotAndIsDeletedIsFalse(
                                updatedTaskAssignment.getTaskId(),
                                updatedTaskAssignment.getAssignerId(),
                                updatedTaskAssignment.getAssigneeId(),
                                updatedTaskAssignment.getAssignmentId())) {
                    errorMsg.append("Already exists another TaskAssignment relationship between with Task with Id: ")
                            .append(updatedTaskAssignment.getTaskId())
                            .append(" and User (Assigner) with Id: ")
                            .append(updatedTaskAssignment.getAssignerId())
                            .append(" and User (Assignee) with Id: ")
                            .append(updatedTaskAssignment.getAssigneeId()).append(". ");
                }
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        return taskAssignmentRepository.saveAllAndFlush(updatedTaskAssignmentList);
    }

    /* DELETE */
    @Override
    public boolean deleteTaskAssignment(long taskAssignmentId) throws Exception {
        TaskAssignment taskAssignment = getById(taskAssignmentId);

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
