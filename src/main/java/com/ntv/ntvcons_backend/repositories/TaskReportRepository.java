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
    Page<TaskReport> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<TaskReport> findByTaskReportIdAndIsDeletedIsFalse(long taskReportId);
    List<TaskReport> findAllByTaskReportIdInAndIsDeletedIsFalse(Collection<Long> taskReportIdCollection);


    /* reportId */
    List<TaskReport> findAllByReportIdAndIsDeletedIsFalse(long reportId);
    List<TaskReport> findAllByReportIdInAndIsDeletedIsFalse(Collection<Long> reportIdCollection);


    /* managerId */
    List<TaskReport> findAllByTaskIdAndIsDeletedIsFalse(long taskId);
    List<TaskReport> findAllByTaskIdInAndIsDeletedIsFalse(Collection<Long> taskIdCollection);
}
