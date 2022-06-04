package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType, Integer> {
    List<ReportType> findAllByIsDeletedFalse();


    /* Id */
    Optional<ReportType> findByReportTypeIdAndIsDeletedIsFalse(int reportTypeId);
    List<ReportType> findAllByReportTypeIdInAndIsDeletedIsFalse(Collection<Integer> reportTypeIdCollection);


    /* reportTypeName */
    Optional<ReportType> findAllByReportTypeNameAndIsDeletedIsFalse(String reportTypeName);
    List<ReportType> findAllByReportTypeNameLikeAndIsDeletedIsFalse(String reportTypeName);
}
