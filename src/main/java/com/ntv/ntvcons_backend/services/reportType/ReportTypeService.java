package com.ntv.ntvcons_backend.services.reportType;

import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ReportTypeService {
    /* CREATE */
    ReportType createReportType(ReportType newReportType) throws Exception;
    ReportTypeReadDTO createReportTypeByDTO(ReportTypeCreateDTO newReportTypeDTO) throws Exception;

    /* READ */
    Page<ReportType> getPageAll(Pageable paging) throws Exception;
    List<ReportTypeReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    boolean existsById(long reportTypeId) throws Exception;
    ReportType getById(long reportTypeId) throws Exception;
    ReportTypeReadDTO getDTOById(long reportTypeId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> reportTypeIdCollection) throws Exception;
    List<ReportType> getAllByIdIn(Collection<Long> reportTypeIdCollection) throws Exception;
    List<ReportTypeReadDTO> getAllDTOByIdIn(Collection<Long> reportTypeIdCollection) throws Exception;
    Map<Long, ReportTypeReadDTO> mapReportTypeIdReportTypeDTOByIdIn(Collection<Long> reportTypeIdCollection) throws Exception;

    ReportType getByReportTypeName(String reportTypeName) throws Exception;
    ReportTypeReadDTO getDTOByReportTypeName(String reportTypeName) throws Exception;

    List<ReportType> getAllByReportTypeNameContains(String reportTypeName) throws Exception;
    List<ReportTypeReadDTO> getAllDTOByReportTypeNameContains(String reportTypeName) throws Exception;

    /* UPDATE */
    ReportType updateReportType(ReportType updatedReportType) throws Exception;
    ReportTypeReadDTO updateReportTypeByDTO(ReportTypeUpdateDTO updatedReportTypeDTO) throws Exception;

    /* DELETE */
    boolean deleteReportType(long reportTypeId) throws Exception;
}
