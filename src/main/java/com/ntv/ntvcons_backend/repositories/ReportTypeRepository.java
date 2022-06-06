package com.ntv.ntvcons_backend.repositories;

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
    Page<ReportType> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<ReportType> findByReportTypeIdAndIsDeletedIsFalse(long reportTypeId);
    List<ReportType> findAllByReportTypeIdInAndIsDeletedIsFalse(Collection<Long> reportTypeIdCollection);


    /* reportTypeName */
    Optional<ReportType> findAllByReportTypeNameAndIsDeletedIsFalse(String reportTypeName);
    List<ReportType> findAllByReportTypeNameLikeAndIsDeletedIsFalse(String reportTypeName);
}
