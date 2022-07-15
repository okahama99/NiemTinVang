package com.ntv.ntvcons_backend.services.taskReport;

import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportReadDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportUpdateDTO;
import com.ntv.ntvcons_backend.entities.TaskReport;
import com.ntv.ntvcons_backend.repositories.TaskReportRepository;
import com.ntv.ntvcons_backend.services.report.ReportService;
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
public class TaskReportServiceImpl implements TaskReportService {
    @Autowired
    private TaskReportRepository taskReportRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ReportService reportService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private TaskService taskService;

    /* CREATE */
    @Override
    public TaskReport createTaskReport(TaskReport newTaskReport) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!reportService.existsById(newTaskReport.getReportId())) {
            errorMsg += "No Report found with Id: '" + newTaskReport.getReportId()
                    + "'. Which violate constraint: FK_TaskReport_Report. ";
        }
        if (!taskService.existsById(newTaskReport.getTaskId())) {
            errorMsg += "No Task found with Id: '" + newTaskReport.getTaskId()
                    + "'. Which violate constraint: FK_TaskReport_Task. ";
        }
        if (!userService.existsById(newTaskReport.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newTaskReport.getCreatedBy()
                    + "'. Which violate constraint: FK_TaskReport_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (taskReportRepository
                .existsByReportIdAndTaskIdAndIsDeletedIsFalse(
                        newTaskReport.getReportId(),
                        newTaskReport.getTaskId())) {
            errorMsg += "Already exists another TaskReport of Report with Id: '" + newTaskReport.getReportId()
                    + "', for Task with Id: '" + newTaskReport.getTaskId()+ "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return taskReportRepository.saveAndFlush(newTaskReport);
    }
    @Override
    public TaskReportReadDTO createTaskReportByDTO(TaskReportCreateDTO newTaskReportDTO) throws Exception {
        TaskReport newTaskReport = modelMapper.map(newTaskReportDTO, TaskReport.class);

        newTaskReport = createTaskReport(newTaskReport);

        return fillDTO(newTaskReport);
    }

    @Override
    public List<TaskReport> createBulkTaskReport(List<TaskReport> newTaskReportList) throws Exception {
        String errorMsg = "";

        Set<Long> reportIdSet = new HashSet<>();
        Set<Long> taskIdSet = new HashSet<>();

        for (TaskReport newTaskReport : newTaskReportList) {
              reportIdSet.add(newTaskReport.getReportId());
              taskIdSet.add(newTaskReport.getTaskId());
        }

        /* CheckFK */
        if (!reportService.existsAllByIdIn(reportIdSet)) {
            errorMsg += "1 or more Report not found with Id: '"
                    + "'. Which violate constraint: FK_TaskReport_Report. ";
        }
        if (!taskService.existsAllByIdIn(taskIdSet)) {
            errorMsg += "1 or more Task not found with Id: '"
                    + "'. Which violate constraint: FK_TaskReport_Task. ";
        }


        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return taskReportRepository.saveAllAndFlush(newTaskReportList);
    }
    @Override
    public List<TaskReportReadDTO> createBulkTaskReportByDTOList(List<TaskReportCreateDTO> newTaskReportDTOList) throws Exception {
        Set<Long> taskIdSet = new HashSet<>();

        List<TaskReport> newTaskReportList = newTaskReportDTOList.stream()
                .map(newTaskReportDTO -> modelMapper.map(newTaskReportDTO, TaskReport.class))
                .collect(Collectors.toList());

        newTaskReportList = createBulkTaskReport(newTaskReportList);

        return newTaskReportList.stream()
                .map(newTaskReport -> modelMapper.map(newTaskReport, TaskReportReadDTO.class))
                .collect(Collectors.toList());
    }

    /* READ */
    @Override
    public Page<TaskReport> getPageAll(Pageable paging) throws Exception {
        Page<TaskReport> taskReportPage = taskReportRepository.findAllByIsDeletedIsFalse(paging);

        if (taskReportPage.isEmpty()) 
            return null;

        return taskReportPage;
    }
    @Override
    public List<TaskReportReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<TaskReport> taskReportPage = getPageAll(paging);

        if (taskReportPage == null)
            return null;

        List<TaskReport> taskReportList = taskReportPage.getContent();

        if (taskReportList.isEmpty())
            return null;

        return fillAllDTO(taskReportList, taskReportPage.getTotalPages());
    }

    @Override
    public boolean existsById(long taskReportId) throws Exception {
        return taskReportRepository.existsByTaskReportIdAndIsDeletedIsFalse(taskReportId);
    }
    @Override
    public TaskReport getById(long taskReportId) throws Exception {
        return taskReportRepository
                .findByTaskReportIdAndIsDeletedIsFalse(taskReportId)
                .orElse(null);
    }
    @Override
    public TaskReportReadDTO getDTOById(long taskReportId) throws Exception {
        TaskReport taskReport = getById(taskReportId);

        if (taskReport == null) 
            return null;

        return fillDTO(taskReport);
    }

    @Override
    public List<TaskReport> getAllByIdIn(Collection<Long> taskReportIdCollection) throws Exception {
        List<TaskReport> taskReportList =
                taskReportRepository.findAllByTaskReportIdInAndIsDeletedIsFalse(taskReportIdCollection);

        if (taskReportList.isEmpty()) 
            return null;

        return taskReportList;
    }
    @Override
    public List<TaskReportReadDTO> getAllDTOByIdIn(Collection<Long> taskReportIdCollection) throws Exception {
        List<TaskReport> taskReportList = getAllByIdIn(taskReportIdCollection);

        if (taskReportList == null) 
            return null;

        return fillAllDTO(taskReportList, null);
    }

    @Override
    public List<TaskReport> getAllByReportId(long reportId) throws Exception {
        List<TaskReport> taskReportList =
                taskReportRepository.findAllByReportIdAndIsDeletedIsFalse(reportId);

        if (taskReportList.isEmpty()) 
            return null;

        return taskReportList;
    }
    @Override
    public List<TaskReportReadDTO> getAllDTOByReportId(long reportId) throws Exception {
        List<TaskReport> taskReportList = getAllByReportId(reportId);

        if (taskReportList == null) 
            return null;

        return fillAllDTO(taskReportList, null);
    }

    @Override
    public List<TaskReport> getAllByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<TaskReport> taskReportList =
                taskReportRepository.findAllByReportIdInAndIsDeletedIsFalse(reportIdCollection);

        if (taskReportList.isEmpty()) 
            return null;

        return taskReportList;
    }
    @Override
    public List<TaskReportReadDTO> getAllDTOByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<TaskReport> taskReportList = getAllByReportIdIn(reportIdCollection);

        if (taskReportList == null) 
            return null;

        return fillAllDTO(taskReportList, null);
    }
    @Override
    public Map<Long, List<TaskReportReadDTO>> mapReportIdTaskReportDTOListByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<TaskReportReadDTO> taskReportDTOList = getAllDTOByReportIdIn(reportIdCollection);

        if (taskReportDTOList == null) 
            return new HashMap<>();

        Map<Long, List<TaskReportReadDTO>> reportIdTaskReportDTOListMap = new HashMap<>();

        long tmpReportId;
        List<TaskReportReadDTO> tmpTaskReportDTOList;

        for (TaskReportReadDTO taskReportDTO : taskReportDTOList) {
            tmpReportId = taskReportDTO.getReportId();
            tmpTaskReportDTOList = reportIdTaskReportDTOListMap.get(tmpReportId);

            if (tmpTaskReportDTOList == null) {
                reportIdTaskReportDTOListMap.put(tmpReportId, new ArrayList<>(Collections.singletonList(taskReportDTO)));
            } else {
                tmpTaskReportDTOList.add(taskReportDTO);

                reportIdTaskReportDTOListMap.put(tmpReportId, tmpTaskReportDTOList);
            }
        }

        return reportIdTaskReportDTOListMap;
    }

    @Override
    public List<TaskReport> getAllByTaskId(long taskId) throws Exception {
        List<TaskReport> taskReportList =
                taskReportRepository.findAllByTaskIdAndIsDeletedIsFalse(taskId);

        if (taskReportList.isEmpty()) 
            return null;

        return taskReportList;
    }
    @Override
    public List<TaskReportReadDTO> getAllDTOByTaskId(long taskId) throws Exception {
        List<TaskReport> taskReportList = getAllByTaskId(taskId);

        if (taskReportList == null) 
            return null;

        return fillAllDTO(taskReportList, null);
    }

    @Override
    public List<TaskReport> getAllByTaskIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<TaskReport> taskReportList =
                taskReportRepository.findAllByTaskIdInAndIsDeletedIsFalse(taskIdCollection);

        if (taskReportList.isEmpty()) 
            return null;

        return taskReportList;
    }
    @Override
    public List<TaskReportReadDTO> getAllDTOByTaskIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<TaskReport> taskReportList = getAllByTaskIdIn(taskIdCollection);

        if (taskReportList == null) 
            return null;

        return fillAllDTO(taskReportList, null);
    }
    @Override
    public Map<Long, List<TaskReportReadDTO>> mapTaskIdTaskReportDTOListByTaskIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<TaskReportReadDTO> taskReportDTOList = getAllDTOByTaskIdIn(reportIdCollection);

        if (taskReportDTOList == null)
            return new HashMap<>();

        Map<Long, List<TaskReportReadDTO>> taskIdTaskReportDTOListMap = new HashMap<>();

        long tmpTaskId;
        List<TaskReportReadDTO> tmpTaskReportDTOList;

        for (TaskReportReadDTO taskReportDTO : taskReportDTOList) {
            tmpTaskId = taskReportDTO.getTaskId();
            tmpTaskReportDTOList = taskIdTaskReportDTOListMap.get(tmpTaskId);

            if (tmpTaskReportDTOList == null) {
                taskIdTaskReportDTOListMap
                        .put(tmpTaskId, new ArrayList<>(Collections.singletonList(taskReportDTO)));
            } else {
                tmpTaskReportDTOList.add(taskReportDTO);

                taskIdTaskReportDTOListMap.put(tmpTaskId, tmpTaskReportDTOList);
            }
        }

        return taskIdTaskReportDTOListMap;
    }

    /* UPDATE */
    @Override
    public TaskReport updateTaskReport(TaskReport updatedTaskReport) throws Exception {
        TaskReport oldTaskReport = getById(updatedTaskReport.getTaskReportId());

        if (oldTaskReport == null) 
            return null; /* Not found by Id, return null */

        String errorMsg = "";

        /* Check FK (if changed) */
        if (!oldTaskReport.getReportId().equals(updatedTaskReport.getReportId())) {
            if (!reportService.existsById(updatedTaskReport.getReportId())) {
                errorMsg += "No Report found with Id: '" + updatedTaskReport.getReportId()
                        + "'. Which violate constraint: FK_TaskReport_Report. ";
            }
        }
        if (!oldTaskReport.getTaskId().equals(updatedTaskReport.getTaskId())) {
            if (!taskService.existsById(updatedTaskReport.getTaskId())) {
                errorMsg += "No Task found with Id: '" + updatedTaskReport.getTaskId()
                        + "'. Which violate constraint: FK_TaskReport_Task. ";
            }
        }
        if (oldTaskReport.getUpdatedBy() != null) {
            if (!oldTaskReport.getUpdatedBy().equals(updatedTaskReport.getUpdatedBy())) {
                if (!taskService.existsById(updatedTaskReport.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedTaskReport.getUpdatedBy()
                            + "'. Which violate constraint: FK_TaskReport_User_UpdateBy. ";
                }
            }
        } else {
            if (!taskService.existsById(updatedTaskReport.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedTaskReport.getUpdatedBy()
                        + "'. Which violate constraint: FK_TaskReport_User_UpdateBy. ";
            }
        }

        /* Check duplicate */
        if (taskReportRepository
                .existsByReportIdAndTaskIdAndTaskReportIdIsNotAndIsDeletedIsFalse(
                        updatedTaskReport.getReportId(),
                        updatedTaskReport.getTaskId(),
                        updatedTaskReport.getTaskReportId())) {
            errorMsg += "Already exists another TaskReport of Report with Id: '" + updatedTaskReport.getReportId()
                    + "', for Task with Id: '" + updatedTaskReport.getTaskId()+ "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return taskReportRepository.saveAndFlush(updatedTaskReport);
    }
    @Override
    public TaskReportReadDTO updateTaskReportByDTO(TaskReportUpdateDTO updatedTaskReportDTO) throws Exception {
        TaskReport updatedTaskReport = modelMapper.map(updatedTaskReportDTO, TaskReport.class);

        updatedTaskReport = updateTaskReport(updatedTaskReport);

        if (updatedTaskReport == null) 
            return null;

        return fillDTO(updatedTaskReport);
    }

    @Override
    public List<TaskReport> updateBulkTaskReport(List<TaskReport> updatedTaskReportList) throws Exception {
        Set<Long> taskReportIdSet = new HashSet<>();
        Set<Long> oldReportIdSet = new HashSet<>();
        Set<Long> oldTaskIdSet = new HashSet<>();
        Set<Long> updatedReportIdSet = new HashSet<>();
        Set<Long> updatedTaskIdSet = new HashSet<>();

        for (TaskReport updatedTaskReport: updatedTaskReportList) {
            taskReportIdSet.add(updatedTaskReport.getTaskReportId());
            updatedReportIdSet.add(updatedTaskReport.getReportId());
            updatedTaskIdSet.add(updatedTaskReport.getTaskId());
        }

        for (TaskReport oldTaskReport : getAllByIdIn(taskReportIdSet)) {
            oldReportIdSet.add(oldTaskReport.getReportId());
            oldTaskIdSet.add(oldTaskReport.getTaskId());
        }

        /* Remove all unchanged reportId & taskId */
        updatedReportIdSet.removeAll(oldReportIdSet);
        updatedTaskIdSet.removeAll(oldTaskIdSet);

        String errorMsg = "";

        /* Check FK */
        /* If there are updated reportId, need to recheck FK */
        if (!updatedReportIdSet.isEmpty()) {
            if (!reportService.existsAllByIdIn(updatedReportIdSet)) {
                errorMsg += "1 or more Report not found with Id: '"
                        + "'. Which violate constraint: FK_TaskReport_Report. ";
            }
        }

        /* If there are updated taskId, need to recheck FK */
        if (!updatedTaskIdSet.isEmpty()) {
            if (!taskService.existsAllByIdIn(updatedTaskIdSet)) {
                errorMsg += "1 or more Task not found with Id: '"
                        + "'. Which violate constraint: FK_TaskReport_Task. ";
            }
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return taskReportRepository.saveAllAndFlush(updatedTaskReportList);
    }
    @Override
    public List<TaskReportReadDTO> updateBulkTaskReportByDTOList(List<TaskReportUpdateDTO> updatedTaskReportDTOList) throws Exception {
        Set<Long> taskIdSet = new HashSet<>();

        List<TaskReport> updatedTaskReportList = updatedTaskReportDTOList.stream()
                .map(updatedTaskReportDTO -> modelMapper.map(updatedTaskReportDTO, TaskReport.class))
                .collect(Collectors.toList());

        updatedTaskReportList = updateBulkTaskReport(updatedTaskReportList);

        return updatedTaskReportList.stream()
                .map(updatedTaskReport -> modelMapper.map(updatedTaskReport, TaskReportReadDTO.class))
                .collect(Collectors.toList());
    }

    /* DELETE */
    @Override
    public boolean deleteTaskReport(long taskReportId) throws Exception {
        TaskReport taskReport = getById(taskReportId);

        if (taskReport == null) {
            return false;
            /* Not found by Id */
        }

        taskReport.setIsDeleted(true);
        taskReportRepository.saveAndFlush(taskReport);

        return true;
    }

    @Override
    public boolean deleteAllByReportId(long reportId) throws Exception {
        List<TaskReport> taskReportList = getAllByReportId(reportId);

        if (taskReportList == null) {
            return false;
            /* Not found with reportId */
        }

        taskReportList = taskReportList.stream()
                .peek(taskReport -> taskReport.setIsDeleted(true))
                .collect(Collectors.toList());

        taskReportRepository.saveAllAndFlush(taskReportList);

        return true;
    }

    @Override
    public boolean deleteAllByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<TaskReport> taskReportList = getAllByReportIdIn(reportIdCollection);

        if (taskReportList == null) {
            return false;
            /* Not found with any reportId */
        }

        taskReportList = taskReportList.stream()
                .peek(taskReport -> taskReport.setIsDeleted(true))
                .collect(Collectors.toList());

        taskReportRepository.saveAllAndFlush(taskReportList);

        return true;
    }

    @Override
    public boolean deleteAllByTaskId(long taskId) throws Exception {
        List<TaskReport> taskReportList = getAllByTaskId(taskId);

        if (taskReportList == null) {
            return false;
            /* Not found with taskId */
        }

        taskReportList = taskReportList.stream()
                .peek(taskReport -> taskReport.setIsDeleted(true))
                .collect(Collectors.toList());

        taskReportRepository.saveAllAndFlush(taskReportList);

        return true;
    }

    @Override
    public boolean deleteAllByTaskIdIn(Collection<Long> taskIdCollection) throws Exception {
        List<TaskReport> taskReportList = getAllByTaskIdIn(taskIdCollection);

        if (taskReportList == null) {
            return false;
            /* Not found with any taskId */
        }

        taskReportList = taskReportList.stream()
                .peek(taskReport -> taskReport.setIsDeleted(true))
                .collect(Collectors.toList());

        taskReportRepository.saveAllAndFlush(taskReportList);

        return true;
    }

    /* Utils */
    private TaskReportReadDTO fillDTO(TaskReport taskReport) throws Exception {
        return modelMapper.map(taskReport, TaskReportReadDTO.class);
    }

    private List<TaskReportReadDTO> fillAllDTO(Collection<TaskReport> taskReportCollection, Integer totalPage) throws Exception {
        return taskReportCollection.stream()
                .map(taskReport -> {
                    TaskReportReadDTO taskReportReadDTO =
                            modelMapper.map(taskReport, TaskReportReadDTO.class);

                    taskReportReadDTO.setTotalPage(totalPage);

                    return taskReportReadDTO;})
                .collect(Collectors.toList());
    }
}
