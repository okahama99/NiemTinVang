package com.ntv.ntvcons_backend.services.reportType;

import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.ReportType;

import java.util.Collection;
import java.util.List;

public interface ReportTypeService {
    /* CREATE */
    ReportType createReportType(ReportType newReportType) throws Exception;
    ReportTypeReadDTO createReportTypeByDTO(ReportTypeCreateDTO newReportTypeDTO) throws Exception;

    /* READ */
    List<ReportType> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<ReportTypeReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    ReportType getById(long reportTypeId) throws Exception;
    ReportTypeReadDTO getDTOById(long reportTypeId) throws Exception;

    List<ReportType> getAllByIdIn(Collection<Long> reportTypeIdCollection) throws Exception;
    List<ReportTypeReadDTO> getAllDTOByIdIn(Collection<Long> reportTypeIdCollection) throws Exception;

    List<ReportType> getAllByReportTypeNameContains(String reportTypeName) throws Exception;
    List<ReportTypeReadDTO> getAllDTOByReportTypeNameContains(String reportTypeName) throws Exception;

    /* UPDATE */
    ReportType updateReportType(ReportType updatedReportType) throws Exception;
    ReportTypeReadDTO updateReportTypeByDTO(ReportTypeUpdateDTO updatedReportTypeDTO) throws Exception;

    /* DELETE */
    boolean deleteReportType(long reportTypeId) throws Exception;
}
