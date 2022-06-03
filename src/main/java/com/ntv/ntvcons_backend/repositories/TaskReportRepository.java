package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.TaskReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskReportRepository extends JpaRepository<TaskReport, Integer> {
    List<TaskReport> findAllByIsDeletedFalse();


    /* Id */
    Optional<TaskReport> findByTaskReportIdAndIsDeletedIsFalse(int taskReportId);
    List<TaskReport> findAllByTaskReportIdInAndIsDeletedIsFalse(Collection<Integer> taskReportIdCollection);


    /* reportId */
    List<TaskReport> findAllByReportIdAndIsDeletedIsFalse(int reportId);
    List<TaskReport> findAllByReportIdInAndIsDeletedIsFalse(Collection<Integer> reportIdCollection);


    /* managerId */
    List<TaskReport> findAllByTaskIdAndIsDeletedIsFalse(int taskId);
    List<TaskReport> findAllByTaskIdInAndIsDeletedIsFalse(Collection<Integer> taskIdCollection);
}
