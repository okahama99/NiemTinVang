package com.ntv.ntvcons_backend.services.reportType;

import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.reportType.ReportTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.ReportType;
import com.ntv.ntvcons_backend.repositories.ReportTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    /* CREATE */
    @Override
    public ReportType createReportType(ReportType newReportType) throws Exception {
        String errorMsg = "";

        /* Check duplicate */
        if (reportTypeRepository.existsByReportTypeNameAndIsDeletedIsFalse(newReportType.getReportTypeName())){
            errorMsg += "Already exists another ReportType with name: " + newReportType.getReportTypeName() +". ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return reportTypeRepository.saveAndFlush(newReportType);
    }
    @Override
    public ReportTypeReadDTO createReportTypeByDTO(ReportTypeCreateDTO newReportTypeDTO) throws Exception {
        ReportType newReportType = modelMapper.map(newReportTypeDTO, ReportType.class);

        newReportType = createReportType(newReportType);

        return modelMapper.map(newReportType, ReportTypeReadDTO.class);
    }

    /* READ */
    @Override
    public List<ReportType> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<ReportType> reportTypePage = reportTypeRepository.findAllByIsDeletedIsFalse(paging);

        if (reportTypePage.isEmpty()) {
            return null;
        }

        return reportTypePage.getContent();
    }
    @Override
    public List<ReportTypeReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        List<ReportType> reportTypeList = getAll(pageNo, pageSize, sortBy, sortType);

        if (reportTypeList != null && !reportTypeList.isEmpty()) {
            int totalPage = (int) Math.ceil((double) reportTypeList.size() / pageSize);

            return reportTypeList.stream()
                    .map(reportType -> {
                        ReportTypeReadDTO reportTypeReadDTO =
                                modelMapper.map(reportType, ReportTypeReadDTO.class);
                        reportTypeReadDTO.setTotalPage(totalPage);
                        return reportTypeReadDTO;})
                    .collect(Collectors.toList());

        } 
            
        return null;
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

        if (reportType == null) {
            return null;
        }

        return modelMapper.map(reportType, ReportTypeReadDTO.class);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> reportTypeIdCollection) throws Exception {
        return reportTypeRepository.existsAllByReportTypeIdInAndIsDeletedIsFalse(reportTypeIdCollection);
    }
    @Override
    public List<ReportType> getAllByIdIn(Collection<Long> reportTypeIdCollection) throws Exception {
        List<ReportType> reportTypeList =
                reportTypeRepository.findAllByReportTypeIdInAndIsDeletedIsFalse(reportTypeIdCollection);

        if (reportTypeList.isEmpty()) {
            return null;
        }

        return reportTypeList;
    }
    @Override
    public List<ReportTypeReadDTO> getAllDTOByIdIn(Collection<Long> reportTypeIdCollection) throws Exception {
        List<ReportType> reportTypeList = getAllByIdIn(reportTypeIdCollection);

        if (reportTypeList == null) {
            return null;
        }

        return reportTypeList.stream()
                .map(reportType -> modelMapper.map(reportType, ReportTypeReadDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public Map<Long, ReportType> mapReportTypeIdReportTypeByIdIn(Collection<Long> reportTypeIdCollection) throws Exception {
        List<ReportType> reportTypeList = getAllByIdIn(reportTypeIdCollection);

        if (reportTypeList == null) {
            return new HashMap<>();
        }

        return reportTypeList.stream()
                .collect(Collectors.toMap(ReportType::getReportTypeId, Function.identity()));
    }
    @Override
    public Map<Long, ReportTypeReadDTO> mapReportTypeIdReportTypeDTOByIdIn(Collection<Long> reportTypeIdCollection) throws Exception {
        List<ReportTypeReadDTO> reportTypeDTOList = getAllDTOByIdIn(reportTypeIdCollection);

        if (reportTypeDTOList == null) {
            return new HashMap<>();
        }

        return reportTypeDTOList.stream()
                .collect(Collectors.toMap(ReportTypeReadDTO::getReportTypeId, Function.identity()));
    }

    @Override
    public List<ReportType> getAllByReportTypeNameContains(String reportTypeName) throws Exception {
        List<ReportType> reportTypeList =
                reportTypeRepository.findAllByReportTypeNameContainsAndIsDeletedIsFalse(reportTypeName);

        if (reportTypeList.isEmpty()) {
            return null;
        }

        return reportTypeList;
    }
    @Override
    public List<ReportTypeReadDTO> getAllDTOByReportTypeNameContains(String reportTypeName) throws Exception {
        List<ReportType> reportTypeList = getAllByReportTypeNameContains(reportTypeName);

        if (reportTypeList == null) {
            return null;
        }

        return reportTypeList.stream()
                .map(reportType -> modelMapper.map(reportType, ReportTypeReadDTO.class))
                .collect(Collectors.toList());
    }

    /* UPDATE */
    @Override
    public ReportType updateReportType(ReportType updatedReportType) throws Exception {
        ReportType reportType = getById(updatedReportType.getReportTypeId());

        if (reportType == null) {
            return null;
            /* Not found by Id, return null */
        }

        String errorMsg = "";

        /* Check duplicate */
        if (reportTypeRepository
                .existsByReportTypeNameAndReportTypeIdIsNotAndIsDeletedIsFalse(
                        updatedReportType.getReportTypeName(),
                        updatedReportType.getReportTypeId())) {
            errorMsg += "Already exists another ReportType with name: " + updatedReportType.getReportTypeName() +". ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return reportTypeRepository.saveAndFlush(updatedReportType);
    }
    @Override
    public ReportTypeReadDTO updateReportTypeByDTO(ReportTypeUpdateDTO updatedReportTypeDTO) throws Exception {
        ReportType updatedReportType = modelMapper.map(updatedReportTypeDTO, ReportType.class);

        updatedReportType = updateReportType(updatedReportType);

        if (updatedReportType == null) {
            return null;
        }

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
}