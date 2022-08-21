package com.ntv.ntvcons_backend.services.reportType;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.blueprint.BlueprintReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.Blueprint;
import com.ntv.ntvcons_backend.entities.ReportType;
import com.ntv.ntvcons_backend.repositories.ReportTypeRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
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
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public ReportType createReportType(ReportType newReportType) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (newReportType.getCreatedBy() != null) {
            if (!userService.existsById(newReportType.getCreatedBy())) {
                errorMsg += "No User (CreatedBy) found with Id: '" + newReportType.getCreatedBy()
                        + "'. Which violate constraint: FK_ReportType_User_CreatedBy. ";
            }
        }

        /* Check duplicate */
        if (reportTypeRepository
                .existsByReportTypeNameAndStatusNotIn(
                        newReportType.getReportTypeName(),
                        N_D_S_STATUS_LIST)) {
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
        Page<ReportType> reportTypePage =
                reportTypeRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

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
        return reportTypeRepository
                .existsByReportTypeIdAndStatusNotIn(reportTypeId, N_D_S_STATUS_LIST);
    }
    @Override
    public ReportType getById(long reportTypeId) throws Exception {
        return reportTypeRepository
                .findByReportTypeIdAndStatusNotIn(reportTypeId, N_D_S_STATUS_LIST)
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
        return reportTypeRepository
                .existsAllByReportTypeIdInAndStatusNotIn(reportTypeIdCollection, N_D_S_STATUS_LIST);
    }
    @Override
    public List<ReportType> getAllByIdIn(Collection<Long> reportTypeIdCollection) throws Exception {
        List<ReportType> reportTypeList =
                reportTypeRepository.findAllByReportTypeIdInAndStatusNotIn(reportTypeIdCollection, N_D_S_STATUS_LIST);

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
                .findByReportTypeNameAndStatusNotIn(reportTypeName, N_D_S_STATUS_LIST)
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
                reportTypeRepository.findAllByReportTypeNameContainsAndStatusNotIn(reportTypeName, N_D_S_STATUS_LIST);

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
                reportTypeRepository
                        .findAllByReportTypeNameContainsAndStatusNotIn(reportTypeName, N_D_S_STATUS_LIST, paging);

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
        if (updatedReportType.getUpdatedBy() != null) {
            if (oldReportType.getUpdatedBy() != null) {
                if (!oldReportType.getUpdatedBy().equals(updatedReportType.getUpdatedBy())) {
                    if (!userService.existsById(updatedReportType.getUpdatedBy())) {
                        errorMsg += "No User (UpdatedBy) found with Id: '" + updatedReportType.getUpdatedBy()
                                + "'. Which violate constraint: FK_ReportType_User_UpdatedBy. ";
                    }
                }
            } else {
                if (!userService.existsById(updatedReportType.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedReportType.getUpdatedBy()
                            + "'. Which violate constraint: FK_ReportType_User_UpdatedBy. ";
                }
            }
        }

        /* Check duplicate */
        if (reportTypeRepository
                .existsByReportTypeNameAndReportTypeIdIsNotAndStatusNotIn(
                        updatedReportType.getReportTypeName(),
                        updatedReportType.getReportTypeId(),
                        N_D_S_STATUS_LIST)) {
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

        reportType.setStatus(Status.DELETED);
        reportTypeRepository.saveAndFlush(reportType);

        return true;
    }

    /* Utils */
    private ReportTypeReadDTO fillDTO(ReportType reportType) throws Exception {
        modelMapper.typeMap(ReportType.class, ReportTypeReadDTO.class)
            .addMappings(mapper -> {
                mapper.skip(ReportTypeReadDTO::setCreatedAt);
                mapper.skip(ReportTypeReadDTO::setUpdatedAt);});

        ReportTypeReadDTO reportTypeDTO =
                modelMapper.map(reportType, ReportTypeReadDTO.class);

        if (reportType.getCreatedAt() != null)
            reportTypeDTO.setCreatedAt(reportType.getCreatedAt().format(dateTimeFormatter));
        if (reportType.getUpdatedAt() != null)
            reportTypeDTO.setUpdatedAt(reportType.getUpdatedAt().format(dateTimeFormatter));

        return reportTypeDTO;
    }

    private List<ReportTypeReadDTO> fillAllDTO(Collection<ReportType> reportTypeCollection, Integer totalPage) throws Exception {
        modelMapper.typeMap(ReportType.class, ReportTypeReadDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(ReportTypeReadDTO::setCreatedAt);
                    mapper.skip(ReportTypeReadDTO::setUpdatedAt);});

        return reportTypeCollection.stream()
                .map(reportType -> {
                    ReportTypeReadDTO reportTypeDTO =
                            modelMapper.map(reportType, ReportTypeReadDTO.class);

                    if (reportType.getCreatedAt() != null)
                        reportTypeDTO.setCreatedAt(reportType.getCreatedAt().format(dateTimeFormatter));
                    if (reportType.getUpdatedAt() != null)
                        reportTypeDTO.setUpdatedAt(reportType.getUpdatedAt().format(dateTimeFormatter));

                    reportTypeDTO.setTotalPage(totalPage);

                    return reportTypeDTO;})
                .collect(Collectors.toList());
    }
}