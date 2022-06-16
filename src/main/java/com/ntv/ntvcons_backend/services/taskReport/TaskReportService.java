package com.ntv.ntvcons_backend.services.taskReport;

import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportCreateDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportReadDTO;
import com.ntv.ntvcons_backend.dtos.taskReport.TaskReportUpdateDTO;
import com.ntv.ntvcons_backend.entities.TaskReport;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TaskReportService {
    /* CREATE */
    TaskReport createTaskReport(TaskReport newTaskReport) throws Exception;
    TaskReportReadDTO createTaskReportByDTO(TaskReportCreateDTO newTaskReportDTO) throws Exception;
    List<TaskReport> createBulkTaskReport(List<TaskReport> newTaskReportList) throws Exception;
    List<TaskReportReadDTO> createBulkTaskReportByDTOList(List<TaskReportCreateDTO> newTaskReportDTOList) throws Exception;

    /* READ */
    List<TaskReport> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<TaskReportReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    boolean existsById(long taskReportId) throws Exception;
    TaskReport getById(long taskReportId) throws Exception;
    TaskReportReadDTO getDTOById(long taskReportId) throws Exception;

    List<TaskReport> getAllByIdIn(Collection<Long> taskReportIdCollection) throws Exception;
    List<TaskReportReadDTO> getAllDTOByIdIn(Collection<Long> taskReportIdCollection) throws Exception;

    List<TaskReport> getAllByReportId(long reportId) throws Exception;
    List<TaskReportReadDTO> getAllDTOByReportId(long reportId) throws Exception;

    List<TaskReport> getAllByReportIdIn(Collection<Long> reportIdCollection) throws Exception;
    List<TaskReportReadDTO> getAllDTOByReportIdIn(Collection<Long> reportIdCollection) throws Exception;
    Map<Long, List<TaskReport>> mapReportIdTaskReportListByReportIdIn(Collection<Long> reportIdCollection) throws Exception;
    Map<Long, List<TaskReportReadDTO>> mapReportIdTaskReportDTOListByReportIdIn(Collection<Long> reportIdCollection) throws Exception;

    List<TaskReport> getAllByTaskId(long taskId) throws Exception;
    List<TaskReportReadDTO> getAllDTOByTaskId(long taskId) throws Exception;

    List<TaskReport> getAllByTaskIdIn(Collection<Long> taskIdCollection) throws Exception;
    List<TaskReportReadDTO> getAllDTOByTaskIdIn(Collection<Long> taskIdCollection) throws Exception;
    Map<Long, List<TaskReport>> mapTaskIdTaskReportListByTaskIdIn(Collection<Long> reportIdCollection) throws Exception;
//    Map<Long, List<TaskReportReadDTO>> mapTaskIdTaskReportDTOListByTaskIdIn(Collection<Long> reportIdCollection) throws Exception;

    /* UPDATE */
    TaskReport updateTaskReport(TaskReport updatedTaskReport) throws Exception;
    TaskReportReadDTO updateTaskReportByDTO(TaskReportUpdateDTO updatedTaskReportDTO) throws Exception;

    /* DELETE */
    boolean deleteTaskReport(long taskReportId) throws Exception;

    /** Cascade when delete Report */
    boolean deleteAllByReportId(long reportId) throws Exception;

    /** Cascade when delete bulk Report */
    boolean deleteAllByReportIdIn(Collection<Long> reportIdCollection) throws Exception;

    /** Cascade when delete Task */
    boolean deleteAllByTaskId(long taskId) throws Exception;

    /** Cascade when delete bulk Task */
    boolean deleteAllByTaskIdIn(Collection<Long> taskIdCollection) throws Exception;

}
