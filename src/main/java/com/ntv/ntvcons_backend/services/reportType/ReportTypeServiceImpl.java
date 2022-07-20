package com.ntv.ntvcons_backend.services.reportType;

import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.ReportType;
import com.ntv.ntvcons_backend.repositories.ReportTypeRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportTypeServiceImpl implements ReportTypeService {
    @Autowired
    private ReportTypeRepository reportTypeRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    /* CREATE */
    @Override
    public ReportType createReportType(ReportType newReportType) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (userService.existsById(newReportType.getCreatedBy())){
            errorMsg += "No User (CreatedBy) found with Id: '" + newReportType.getCreatedBy()
                    + "'. Which violate constraint: FK_ReportType_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (reportTypeRepository.existsByReportTypeNameAndIsDeletedIsFalse(newReportType.getReportTypeName())){
            errorMsg += "Already exists another ReportType with name: '" 
                    + newReportType.getReportTypeName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return reportTypeRepository.saveAndFlush(newReportType);
    }
    @Override
    public ReportTypeReadDTO createReportTypeByDTO(ReportTypeCreateDTO newReportTypeDTO) throws Exception {
        ReportType newReportType = modelMapper.map(newReportTypeDTO, ReportType.class);

        newReportType = createReportType(newReportType);

        return fillDTO(newReportType);
    }

    /* READ */
    @Override
    public Page<ReportType> getPageAll(Pageable paging) throws Exception {
        Page<ReportType> reportTypePage = reportTypeRepository.findAllByIsDeletedIsFalse(paging);

        if (reportTypePage.isEmpty()) 
            return null;

        return reportTypePage;
    }
    @Override
    public List<ReportTypeReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<ReportType> reportTypePage = getPageAll(paging);

        if (reportTypePage == null)
            return null;

        List<ReportType> reportTypeList = reportTypePage.getContent();

        if (reportTypePage.isEmpty())
            return null;

        return fillAllDTO(reportTypeList, reportTypePage.getTotalPages());
    }

    @Override
    public boolean existsById(long reportTypeId) throws Exception {
        return reportTypeRepository.existsByReportTypeIdAndIsDeletedIsFalse(reportTypeId);
    }
    @Override
    public ReportType getById(long reportTypeId) throws Exception {
        return reportTypeRepository
                .findByReportTypeIdAndIsDeletedIsFalse(reportTypeId)
                .orElse(null);
    }
    @Override
    public ReportTypeReadDTO getDTOById(long reportTypeId) throws Exception {
        ReportType reportType = getById(reportTypeId);

        if (reportType == null) 
            return null;

        return fillDTO(reportType);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> reportTypeIdCollection) throws Exception {
        return reportTypeRepository.existsAllByReportTypeIdInAndIsDeletedIsFalse(reportTypeIdCollection);
    }
    @Override
    public List<ReportType> getAllByIdIn(Collection<Long> reportTypeIdCollection) throws Exception {
        List<ReportType> reportTypeList =
                reportTypeRepository.findAllByReportTypeIdInAndIsDeletedIsFalse(reportTypeIdCollection);

        if (reportTypeList.isEmpty()) 
            return null;

        return reportTypeList;
    }
    @Override
    public List<ReportTypeReadDTO> getAllDTOByIdIn(Collection<Long> reportTypeIdCollection) throws Exception {
        List<ReportType> reportTypeList = getAllByIdIn(reportTypeIdCollection);

        if (reportTypeList == null) 
            return null;

        return fillAllDTO(reportTypeList, null);
    }
    @Override
    public Map<Long, ReportTypeReadDTO> mapReportTypeIdReportTypeDTOByIdIn(Collection<Long> reportTypeIdCollection) throws Exception {
        List<ReportTypeReadDTO> reportTypeDTOList = getAllDTOByIdIn(reportTypeIdCollection);

        if (reportTypeDTOList == null) 
            return new HashMap<>();

        return reportTypeDTOList.stream()
                .collect(Collectors.toMap(ReportTypeReadDTO::getReportTypeId, Function.identity()));
    }

    @Override
    public ReportType getByReportTypeName(String reportTypeName) throws Exception {
        return reportTypeRepository
                .findByReportTypeNameAndIsDeletedIsFalse(reportTypeName)
                .orElse(null);
    }
    @Override
    public ReportTypeReadDTO getDTOByReportTypeName(String reportTypeName) throws Exception {
        ReportType reportType = getByReportTypeName(reportTypeName);

        if (reportType == null)
            return null;

        return fillDTO(reportType);
    }

    @Override
    public List<ReportType> getAllByReportTypeNameContains(String reportTypeName) throws Exception {
        List<ReportType> reportTypeList =
                reportTypeRepository.findAllByReportTypeNameContainsAndIsDeletedIsFalse(reportTypeName);

        if (reportTypeList.isEmpty()) 
            return null;

        return reportTypeList;
    }
    @Override
    public List<ReportTypeReadDTO> getAllDTOByReportTypeNameContains(String reportTypeName) throws Exception {
        List<ReportType> reportTypeList = getAllByReportTypeNameContains(reportTypeName);

        if (reportTypeList == null) 
            return null;

        return fillAllDTO(reportTypeList, null);
    }
    @Override
    public Page<ReportType> getPageAllByReportTypeNameContains(Pageable paging, String reportTypeName) throws Exception {
        Page<ReportType> reportTypePage =
                reportTypeRepository.findAllByReportTypeNameContainsAndIsDeletedIsFalse(reportTypeName, paging);

        if (reportTypePage.isEmpty())
            return null;

        return reportTypePage;
    }
    @Override
    public List<ReportTypeReadDTO> getAllDTOInPagingByReportTypeNameContains(Pageable paging, String reportTypeName) throws Exception {
        Page<ReportType> reportTypePage = getPageAllByReportTypeNameContains(paging, reportTypeName);

        if (reportTypePage == null)
            return null;

        List<ReportType> reportTypeList = reportTypePage.getContent();

        if (reportTypePage.isEmpty())
            return null;

        return fillAllDTO(reportTypeList, reportTypePage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public ReportType updateReportType(ReportType updatedReportType) throws Exception {
        ReportType oldReportType = getById(updatedReportType.getReportTypeId());

        if (oldReportType == null) 
            return null; /* Not found by Id, return null */

        String errorMsg = "";

        /* Check FK */
        if (oldReportType.getUpdatedBy() != null) {
            if (!oldReportType.getUpdatedBy().equals(updatedReportType.getUpdatedBy())) {
                if (userService.existsById(updatedReportType.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedReportType.getUpdatedBy()
                            + "'. Which violate constraint: FK_ReportType_User_UpdatedBy. ";
                }
            }
        } else {
            if (userService.existsById(updatedReportType.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedReportType.getUpdatedBy()
                        + "'. Which violate constraint: FK_ReportType_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (reportTypeRepository
                .existsByReportTypeNameAndReportTypeIdIsNotAndIsDeletedIsFalse(
                        updatedReportType.getReportTypeName(),
                        updatedReportType.getReportTypeId())) {
            errorMsg += "Already exists another ReportType with name: '"
                    + updatedReportType.getReportTypeName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedReportType.setCreatedAt(oldReportType.getCreatedAt());
        updatedReportType.setCreatedBy(oldReportType.getCreatedBy());

        return reportTypeRepository.saveAndFlush(updatedReportType);
    }
    @Override
    public ReportTypeReadDTO updateReportTypeByDTO(ReportTypeUpdateDTO updatedReportTypeDTO) throws Exception {
        ReportType updatedReportType = modelMapper.map(updatedReportTypeDTO, ReportType.class);

        updatedReportType = updateReportType(updatedReportType);

        if (updatedReportType == null) 
            return null;

        return modelMapper.map(updatedReportType, ReportTypeReadDTO.class);
    }

    /* DELETE */
    @Override
    public boolean deleteReportType(long reportTypeId) throws Exception {
        ReportType reportType = getById(reportTypeId);

        if (reportType == null) {
            return false;
            /* Not found with Id */
        }

        reportType.setIsDeleted(true);
        reportTypeRepository.saveAndFlush(reportType);

        return true;
    }

    /* Utils */
    private ReportTypeReadDTO fillDTO(ReportType reportType) throws Exception {
        return modelMapper.map(reportType, ReportTypeReadDTO.class);
    }

    private List<ReportTypeReadDTO> fillAllDTO(Collection<ReportType> reportTypeCollection, Integer totalPage) throws Exception {
        return reportTypeCollection.stream()
                .map(reportType -> {
                    ReportTypeReadDTO reportTypeDTO =
                            modelMapper.map(reportType, ReportTypeReadDTO.class);

                    reportTypeDTO.setTotalPage(totalPage);

                    return reportTypeDTO;})
                .collect(Collectors.toList());
    }
}