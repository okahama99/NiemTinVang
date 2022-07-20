package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.TaskReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskReportRepository extends JpaRepository<TaskReport, Long> {
    Page<TaskReport> findAllByIsDeletedIsFalse(Pageable paging);


    /* Id */
    boolean existsByTaskReportIdAndIsDeletedIsFalse(long taskReportId);
    Optional<TaskReport> findByTaskReportIdAndIsDeletedIsFalse(long taskReportId);
    List<TaskReport> findAllByTaskReportIdInAndIsDeletedIsFalse(Collection<Long> taskReportIdCollection);
    /* Id & reportId & taskId & taskProgress */
    /** Check duplicate reportId & taskId & taskProgress for update */
    boolean existsByReportIdAndTaskIdAndTaskReportIdIsNotAndIsDeletedIsFalse
            (long reportId, long taskId, long taskReportId);


    /* reportId */
    List<TaskReport> findAllByReportIdAndIsDeletedIsFalse(long reportId);
    List<TaskReport> findAllByReportIdInAndIsDeletedIsFalse(Collection<Long> reportIdCollection);


    /* taskId */
    List<TaskReport> findAllByTaskIdAndIsDeletedIsFalse(long taskId);
    List<TaskReport> findAllByTaskIdInAndIsDeletedIsFalse(Collection<Long> taskIdCollection);
    /* Id & reportId & taskId */
    boolean existsByReportIdAndTaskIdAndIsDeletedIsFalse(long reportId, long taskId);
}
