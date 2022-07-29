package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType, Long> {
    Page<ReportType> findAllByStatusNotIn
            (Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByReportTypeIdAndStatusNotIn
            (long reportTypeId, Collection<Status> statusCollection);
    Optional<ReportType> findByReportTypeIdAndStatusNotIn
            (long reportTypeId, Collection<Status> statusCollection);
    boolean existsAllByReportTypeIdInAndStatusNotIn
            (Collection<Long> reportTypeIdCollection, Collection<Status> statusCollection);
    List<ReportType> findAllByReportTypeIdInAndStatusNotIn
            (Collection<Long> reportTypeIdCollection, Collection<Status> statusCollection);
    /* Id & reportTypeName */
    /** Check duplicate for Update */
    boolean existsByReportTypeNameAndReportTypeIdIsNotAndStatusNotIn
            (String reportTypeName, long reportTypeId, Collection<Status> statusCollection);


    /* reportTypeName */
    /** Check duplicate for Create */
    boolean existsByReportTypeNameAndStatusNotIn
            (String reportTypeName, Collection<Status> statusCollection);
    Optional<ReportType> findByReportTypeNameAndStatusNotIn
            (String reportTypeName, Collection<Status> statusCollection);
    List<ReportType> findAllByReportTypeNameContainsAndStatusNotIn
            (String reportTypeName, Collection<Status> statusCollection);
    Page<ReportType> findAllByReportTypeNameContainsAndStatusNotIn
            (String reportTypeName, Collection<Status> statusCollection, Pageable paging);
}
