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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskAssignmentServiceImpl implements TaskAssignmentService{
    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;
    @Autowired
    private ModelMapper modelMapper;
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
                    + "Which violate constraint: FK_TaskAssignment_Task. ";
        }

        if (!userService.existsById(newTaskAssignment.getAssignerId())) {
            errorMsg += "No User (Assigner) found with Id: " + newTaskAssignment.getAssignerId()
                    + "Which violate constraint: FK_TaskAssignment_User_AssignerId. ";
        }

        if (!userService.existsById(newTaskAssignment.getAssigneeId())) {
            errorMsg += "No User (Assignee) found with Id: " + newTaskAssignment.getAssigneeId()
                    + "Which violate constraint: FK_TaskAssignment_User_AssigneeId. ";
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

        Map<Long, Map<Long, Long>> taskIdAssignerIdAssigneeIdMapMap = new HashMap<>();
        Set<Long> taskIdSet = new HashSet<>();
        Set<Long> assignerIdSet = new HashSet<>();
        Set<Long> assigneeIdSet = new HashSet<>();
        Map<Long, Long> tmpAssignerIdAssigneeIdMap;
        boolean isDuplicated = false;

        for (TaskAssignment newTaskAssignment : newTaskAssignmentList) {
            taskIdSet.add(newTaskAssignment.getTaskId());
            assignerIdSet.add(newTaskAssignment.getAssignerId());
            assigneeIdSet.add(newTaskAssignment.getAssigneeId());

            /* Check duplicate 1 (within input) */
            tmpAssignerIdAssigneeIdMap = taskIdAssignerIdAssigneeIdMapMap.get(newTaskAssignment.getTaskId());
            if (tmpAssignerIdAssigneeIdMap == null) {
                tmpAssignerIdAssigneeIdMap = new HashMap<>();
                tmpAssignerIdAssigneeIdMap.put(newTaskAssignment.getAssignerId(), newTaskAssignment.getAssigneeId());

                taskIdAssignerIdAssigneeIdMapMap.put(newTaskAssignment.getTaskId(), tmpAssignerIdAssigneeIdMap;
            } else {
                if (tmpAssignerIdAssigneeIdMap.get(newTaskAssignment.getManagerId())) {
                    isDuplicated = true;
                    errorMsg.append("Duplicate TaskAssignment relationship between with Project with Id: ")
                            .append(newTaskAssignment.getProjectId())
                            .append(" and User with Id: ")
                            .append(newTaskAssignment.getManagerId()).append(". ");
                } else {
                    tmpAssignerIdAssigneeIdMap.add(newTaskAssignment.getManagerId());
                    taskIdAssignerIdAssigneeIdMapMap.put(newTaskAssignment.getProjectId(), tmpAssignerIdAssigneeIdMap);
                }
            }
        }


        /* Check FK */
        if (!projectService.existsAllByIdIn(taskIdSet)) {
            errorMsg.append("1 or more Project not found with Id. Which violate constraint: FK_TaskAssignment_Project. ");
        }

        if (!userService.existsAllByIdIn(assignerIdSet)) {
            errorMsg.append("1 or more User not found with Id. Which violate constraint: FK_TaskAssignment_User. ");
        }

        /* Already has duplicated within input, no need to check with DB */
        if (!isDuplicated) {
            /* Check duplicate 2 (in DB) */
            for (TaskAssignment newTaskAssignment : newTaskAssignmentList) {
                /* TODO: bulk check instead of loop */
                if (taskAssignmentRepository
                        .existsByProjectIdAndManagerIdAndIsDeletedIsFalse(
                                newTaskAssignment.getProjectId(),
                                newTaskAssignment.getManagerId())) {
                    errorMsg.append("Already exists another TaskAssignment relationship between with Project with Id: ")
                            .append(newTaskAssignment.getProjectId())
                            .append(" and User with Id: ")
                            .append(newTaskAssignment.getManagerId()).append(". ");
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
    public List<TaskAssignment> getAllByTaskId(long taskId) throws Exception {
        List<TaskAssignment> taskAssignmentList =
                taskAssignmentRepository.findAllByTaskIdAndIsDeletedIsFalse(taskId);

        if (!taskAssignmentList.isEmpty()) {
            return null;
        }

        return taskAssignmentList;
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
        return null;
    }

    @Override
    public TaskAssignmentReadDTO updateTaskAssignmentByDTO(TaskAssignmentUpdateDTO updatedTaskAssignmentDTO) throws Exception {
        return null;
    }

    @Override
    public List<TaskAssignment> updateBulkTaskAssignment(List<TaskAssignment> updatedTaskAssignmentList) throws Exception {
        return null;
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
    public boolean deleteAllByTaskId(long taskId) throws Exception {
        List<TaskAssignment> taskAssignmentList = getAllByTaskId(taskId);

        if (taskAssignmentList == null) {
            return false;
        }

        taskAssignmentList =
                taskAssignmentList.stream()
                        .peek(taskAssignment -> taskAssignment.setIsDeleted(true))
                        .collect(Collectors.toList());

        taskAssignmentRepository.saveAllAndFlush(taskAssignmentList);

        return true;
    }
}
