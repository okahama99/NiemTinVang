package com.ntv.ntvcons_backend.services.reportDetail;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.entities.ProjectManager;
import com.ntv.ntvcons_backend.entities.ReportDetail;
import com.ntv.ntvcons_backend.repositories.ReportDetailRepository;
import com.ntv.ntvcons_backend.services.report.ReportService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportDetailServiceImpl implements ReportDetailService {
    @Autowired
    private ReportDetailRepository reportDetailRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ReportService reportService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public ReportDetail createReportDetail(ReportDetail newReportDetail) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!reportService.existsById(newReportDetail.getReportId())) {
            errorMsg += "No Report found with Id: '" + newReportDetail.getReportId()
                    + "'. Which violate constraint: FK_ReportDetail_Report. ";
        }
        if (newReportDetail.getCreatedBy() != null) {
            if (!userService.existsById(newReportDetail.getCreatedBy())) {
                errorMsg += "No User (CreatedBy) found with Id: '" + newReportDetail.getCreatedBy()
                        + "'. Which violate constraint: FK_ReportDetail_User_CreatedBy. ";
            }
        }

        /* Check duplicate */
        if (reportDetailRepository
                .existsByReportIdAndItemDescAndItemPriceAndStatusNotIn(
                        newReportDetail.getReportId(),
                        newReportDetail.getItemDesc(),
                        newReportDetail.getItemPrice(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exist another ReportDetail of Report with Id: '" + newReportDetail.getReportId()
                    + "'. With itemDesc: '" + newReportDetail.getItemDesc()
                    + "' at price: '" + newReportDetail.getItemPrice() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return reportDetailRepository.saveAndFlush(newReportDetail);
    }
    @Override
    public ReportDetailReadDTO createReportDetailByDTO(ReportDetailCreateDTO newReportDetailDTO) throws Exception {
        ReportDetail newReportDetail = modelMapper.map(newReportDetailDTO, ReportDetail.class);

        newReportDetail = createReportDetail(newReportDetail);

        return fillDTO(newReportDetail);
    }

    @Override
    public List<ReportDetail> createBulkReportDetail(Collection<ReportDetail> newReportDetailList) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        Set<Long> reportIdSet = new HashSet<>();
        Set<Long> createdBySet = new HashSet<>();

        Map<Long, Map<String, List<Double>>> reportIdItemDescItemPriceListMapMap = new HashMap<>();

        Long tmpReportId;
        String tmpItemDesc;
        Double tmpItemPrice;
        List<Double> tmpItemPriceList;

        boolean isDuplicated = false;

        Map<String, List<Double>> tmpItemDescItemPriceListMap;

        for (ReportDetail newReportDetail : newReportDetailList) {
            tmpReportId = newReportDetail.getReportId();
            tmpItemDesc = newReportDetail.getItemDesc();
            tmpItemPrice = newReportDetail.getItemPrice();

            reportIdSet.add(tmpReportId);
            if (newReportDetail.getCreatedBy() != null)
                createdBySet.add(newReportDetail.getCreatedBy());

            tmpItemDescItemPriceListMap = reportIdItemDescItemPriceListMapMap.get(tmpReportId);

            /* Check duplicate 1 (at input) */
            if (tmpItemDescItemPriceListMap == null) {
                tmpItemDescItemPriceListMap = new HashMap<>();

                tmpItemDescItemPriceListMap
                        .put(tmpItemDesc, new ArrayList<>(Collections.singletonList(tmpItemPrice)));
            } else {
                tmpItemPriceList = tmpItemDescItemPriceListMap.get(tmpItemDesc);

                if (tmpItemPriceList == null) {
                    tmpItemDescItemPriceListMap
                            .put(tmpItemDesc, new ArrayList<>(Collections.singletonList(tmpItemPrice)));
                } else {
                    if (tmpItemPriceList.contains(tmpItemPrice)) {
                        isDuplicated = true;

                        errorMsg.append("Already exist another ReportDetail of Report with Id: '").append(tmpReportId)
                                .append("'. With itemDesc: '").append(tmpItemDesc)
                                .append("' at price: '").append(tmpItemPrice).append("'. ");
                    } else {
                        tmpItemPriceList.add(tmpItemPrice);

                        tmpItemDescItemPriceListMap
                                .put(tmpItemDesc, tmpItemPriceList);
                    }
                }
            }

            reportIdItemDescItemPriceListMapMap
                    .put(tmpReportId, tmpItemDescItemPriceListMap);
        }

        /* Check FK */
        if (!reportService.existsAllByIdIn(reportIdSet)) {
            errorMsg.append("1 or more Report not found with Id: '")
                    .append("'. Which violate constraint: FK_ReportDetail_Report. ");
        }
        if (!userService.existsAllByIdIn(createdBySet)) {
            errorMsg.append("1 or more User (CreatedBy) not found with Id: '")
                    .append("'. Which violate constraint: FK_ReportDetail_User_CreatedBy. ");
        }

        /* If already duplicated within input, no need to check with DB */
        if (!isDuplicated) {
            /* Check duplicate 2 (input vs DB) */
            for (ReportDetail newReportDetail : newReportDetailList) {
                if (reportDetailRepository
                        .existsByReportIdAndItemDescAndItemPriceAndStatusNotIn(
                                newReportDetail.getReportId(),
                                newReportDetail.getItemDesc(),
                                newReportDetail.getItemPrice(),
                                N_D_S_STATUS_LIST)) {
                    errorMsg.append("Already exist another ReportDetail of Report with Id: '")
                            .append(newReportDetail.getReportId())
                            .append("'. With itemDesc: '").append(newReportDetail.getItemDesc())
                            .append("' at price: '").append(newReportDetail.getItemPrice()).append("'. ");
                }
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        return reportDetailRepository.saveAllAndFlush(newReportDetailList);
    }
    @Override
    public List<ReportDetailReadDTO> createBulkReportDetailByDTOList(Collection<ReportDetailCreateDTO> newReportDetailDTOList) throws Exception {
        List<ReportDetail> newReportDetailList =
                newReportDetailDTOList.stream()
                        .map(newReportDetailDTO -> modelMapper.map(newReportDetailDTO, ReportDetail.class))
                        .collect(Collectors.toList());

        newReportDetailList = createBulkReportDetail(newReportDetailList);

        return fillAllDTO(newReportDetailList, null);
    }

    /* READ */
    @Override
    public Page<ReportDetail> getPageAll(Pageable paging) throws Exception {
        Page<ReportDetail> reportDetailPage =
                reportDetailRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (reportDetailPage.isEmpty()) 
            return null;

        return reportDetailPage;
    }
    @Override
    public List<ReportDetailReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<ReportDetail> reportDetailPage = getPageAll(paging);

        if (reportDetailPage == null)
            return null;

        List<ReportDetail> reportDetailList = reportDetailPage.getContent();

        if (reportDetailList.isEmpty())
            return null;

        return fillAllDTO(reportDetailList, reportDetailPage.getTotalPages());
    }

    @Override
    public ReportDetail getById(long reportDetailId) throws Exception {
        Optional<ReportDetail> reportDetail =
                reportDetailRepository.findByReportDetailIdAndStatusNotIn(reportDetailId, N_D_S_STATUS_LIST);

        return reportDetail.orElse(null);
    }
    @Override
    public ReportDetailReadDTO getDTOById(long reportDetailId) throws Exception {
        ReportDetail reportDetail = getById(reportDetailId);

        if (reportDetail == null) 
            return null;

        return fillDTO(reportDetail);
    }

    @Override
    public List<ReportDetail> getAllByIdIn(Collection<Long> reportDetailIdCollection) throws Exception {
        List<ReportDetail> reportDetailList =
                reportDetailRepository
                        .findAllByReportDetailIdInAndStatusNotIn(reportDetailIdCollection, N_D_S_STATUS_LIST);

        if (reportDetailList.isEmpty()) 
            return null;

        return reportDetailList;
    }
    @Override
    public List<ReportDetailReadDTO> getAllDTOByIdIn(Collection<Long> reportDetailIdCollection) throws Exception {
        List<ReportDetail> reportDetailList = getAllByIdIn(reportDetailIdCollection);

        if (reportDetailList == null) 
            return null;

        return fillAllDTO(reportDetailList, null);
    }

    @Override
    public List<ReportDetail> getAllByReportId(long reportId) throws Exception {
        List<ReportDetail> reportDetailList =
                reportDetailRepository.findAllByReportIdAndStatusNotIn(reportId, N_D_S_STATUS_LIST);

        if (reportDetailList.isEmpty()) 
            return null;

        return reportDetailList;
    }
    @Override
    public List<ReportDetailReadDTO> getAllDTOByReportId(long reportId) throws Exception {
        List<ReportDetail> reportDetailList = getAllByReportId(reportId);

        if (reportDetailList == null) 
            return null;

        return fillAllDTO(reportDetailList, null);
    }
    @Override
    public Page<ReportDetail> getPageAllByReportId(Pageable paging, long reportId) throws Exception {
        Page<ReportDetail> reportDetailPage =
                reportDetailRepository.findAllByReportIdAndStatusNotIn(reportId, N_D_S_STATUS_LIST, paging);

        if (reportDetailPage.isEmpty()) 
            return null;

        return reportDetailPage;
    }
    @Override
    public List<ReportDetailReadDTO> getAllDTOInPagingByReportId(Pageable paging, long reportId) throws Exception {
        Page<ReportDetail> reportDetailPage = getPageAllByReportId(paging, reportId);

        if (reportDetailPage == null)
            return null;

        List<ReportDetail> reportDetailList = reportDetailPage.getContent();

        if (reportDetailList.isEmpty())
            return null;

        return fillAllDTO(reportDetailList, reportDetailPage.getTotalPages());
    }

    @Override
    public List<ReportDetail> getAllByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<ReportDetail> reportDetailList =
                reportDetailRepository.findAllByReportIdInAndStatusNotIn(reportIdCollection, N_D_S_STATUS_LIST);

        if (reportDetailList.isEmpty()) 
            return null;

        return reportDetailList;
    }
    @Override
    public List<ReportDetailReadDTO> getAllDTOByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<ReportDetail> reportDetailList = getAllByReportIdIn(reportIdCollection);

        if (reportDetailList == null) 
            return null;

        return fillAllDTO(reportDetailList, null);
    }
    @Override
    public Map<Long, List<ReportDetailReadDTO>> mapReportIdReportDetailDTOListByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<ReportDetailReadDTO> reportDetailDTOList = getAllDTOByReportIdIn(reportIdCollection);

        if (reportDetailDTOList == null) 
            return new HashMap<>();

        Map<Long, List<ReportDetailReadDTO>> reportIdReportDetailDTOListMap = new HashMap<>();

        long tmpReportId;
        List<ReportDetailReadDTO> tmpReportDetailDTOList;

        for (ReportDetailReadDTO reportDetailDTO : reportDetailDTOList) {
            tmpReportId = reportDetailDTO.getReportId();
            tmpReportDetailDTOList = reportIdReportDetailDTOListMap.get(tmpReportId);

            if (tmpReportDetailDTOList == null) {
                reportIdReportDetailDTOListMap.put(tmpReportId, new ArrayList<>(Collections.singletonList(reportDetailDTO)));
            } else {
                tmpReportDetailDTOList.add(reportDetailDTO);

                reportIdReportDetailDTOListMap.put(tmpReportId, tmpReportDetailDTOList);
            }
        }

        return reportIdReportDetailDTOListMap;
    }
    @Override
    public Page<ReportDetail> getPageAllByReportIdIn(Pageable paging, Collection<Long> reportIdCollection) throws Exception {
        Page<ReportDetail> reportDetailPage =
                reportDetailRepository.findAllByReportIdInAndStatusNotIn(reportIdCollection, N_D_S_STATUS_LIST, paging);

        if (reportDetailPage.isEmpty()) 
            return null;

        return reportDetailPage;
    }
    @Override
    public List<ReportDetailReadDTO> getAllDTOInPagingByReportIdIn(Pageable paging, Collection<Long> reportIdCollection) throws Exception {
        Page<ReportDetail> reportDetailPage = getPageAllByReportIdIn(paging, reportIdCollection);

        if (reportDetailPage == null)
            return null;

        List<ReportDetail> reportDetailList = reportDetailPage.getContent();

        if (reportDetailList.isEmpty())
            return null;

        return fillAllDTO(reportDetailList, reportDetailPage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public ReportDetail updateReportDetail(ReportDetail updatedReportDetail) throws Exception {
        ReportDetail oldReportDetail = getById(updatedReportDetail.getReportDetailId());

        if (oldReportDetail == null) 
            return null; /* Not found by Id, return null */

        String errorMsg = "";

        /* Check FK */
        if (!oldReportDetail.getReportId().equals(updatedReportDetail.getReportId())) {
            if (!reportService.existsById(updatedReportDetail.getReportId())) {
                errorMsg += "No Report found with Id: '" + updatedReportDetail.getReportId()
                        + "'. Which violate constraint: FK_ReportDetail_Report. ";
            }
        }
        if (updatedReportDetail.getUpdatedBy() != null) {
            if (oldReportDetail.getUpdatedBy() != null) {
                if (!oldReportDetail.getUpdatedBy().equals(updatedReportDetail.getUpdatedBy())) {
                    if (!reportService.existsById(updatedReportDetail.getUpdatedBy())) {
                        errorMsg += "No User (UpdatedBy) found with Id: '" + updatedReportDetail.getUpdatedBy()
                                + "'. Which violate constraint: FK_ReportDetail_User_UpdatedBy. ";
                    }
                }
            } else {
                if (!reportService.existsById(updatedReportDetail.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedReportDetail.getUpdatedBy()
                            + "'. Which violate constraint: FK_ReportDetail_User_UpdatedBy. ";
                }
            }
        }

        /* Check duplicate */
        if (reportDetailRepository
                .existsByReportIdAndItemDescAndItemPriceAndReportDetailIdIsNotAndStatusNotIn(
                        updatedReportDetail.getReportId(),
                        updatedReportDetail.getItemDesc(),
                        updatedReportDetail.getItemPrice(),
                        updatedReportDetail.getReportDetailId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exist another ReportDetail of Report with Id: '" + updatedReportDetail.getReportId()
                    + "'. With itemDesc: '" + updatedReportDetail.getItemDesc()
                    + "' at price: '" + updatedReportDetail.getItemPrice() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedReportDetail.setCreatedAt(oldReportDetail.getCreatedAt());
        updatedReportDetail.setCreatedBy(oldReportDetail.getCreatedBy());

        return reportDetailRepository.saveAndFlush(updatedReportDetail);
    }
    @Override
    public ReportDetailReadDTO updateReportDetailByDTO(ReportDetailUpdateDTO updatedReportDetailDTO) throws Exception {
        ReportDetail updatedReportDetail = modelMapper.map(updatedReportDetailDTO, ReportDetail.class);

        updatedReportDetail = updateReportDetail(updatedReportDetail);

        if (updatedReportDetail == null) 
            return null;

        return fillDTO(updatedReportDetail);
    }

    @Override
    public List<ReportDetail> updateBulkReportDetail(Collection<ReportDetail> updatedReportDetailList) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        Map<Long, Map<String, List<Double>>> reportIdItemDescItemPriceListMapMap = new HashMap<>();

        Set<Long> reportDetailIdSet = new HashSet<>();
        Set<Long> updatedReportIdSet = new HashSet<>();
        Set<Long> updatedUpdatedBySet = new HashSet<>();

        Long tmpReportId;
        String tmpItemDesc;
        Double tmpItemPrice;
        List<Double> tmpItemPriceList;

        Map<String, List<Double>> tmpItemDescItemPriceListMap;

        for (ReportDetail updatedReportDetail : updatedReportDetailList) {
            tmpReportId = updatedReportDetail.getReportId();
            tmpItemDesc = updatedReportDetail.getItemDesc();
            tmpItemPrice = updatedReportDetail.getItemPrice();

            reportDetailIdSet.add(updatedReportDetail.getReportDetailId());
            updatedReportIdSet.add(tmpReportId);
            if (updatedReportDetail.getUpdatedBy() != null)
                updatedUpdatedBySet.add(updatedReportDetail.getUpdatedBy());

            tmpItemDescItemPriceListMap = reportIdItemDescItemPriceListMapMap.get(tmpReportId);

            /* Check duplicate 1 (at input) */
            if (tmpItemDescItemPriceListMap == null) {
                tmpItemDescItemPriceListMap = new HashMap<>();

                tmpItemDescItemPriceListMap
                        .put(tmpItemDesc, new ArrayList<>(Collections.singletonList(tmpItemPrice)));
            } else {
                tmpItemPriceList = tmpItemDescItemPriceListMap.get(tmpItemDesc);

                if (tmpItemPriceList == null) {
                    tmpItemDescItemPriceListMap
                            .put(tmpItemDesc, new ArrayList<>(Collections.singletonList(tmpItemPrice)));
                } else {
                    if (tmpItemPriceList.contains(tmpItemPrice)) {
                        errorMsg.append("Already exist another ReportDetail of Report with Id: '").append(tmpReportId)
                                .append("'. With itemDesc: '").append(tmpItemDesc)
                                .append("' at price: '").append(tmpItemPrice).append("'. ");
                    } else {
                        tmpItemPriceList.add(tmpItemPrice);

                        tmpItemDescItemPriceListMap
                                .put(tmpItemDesc, tmpItemPriceList);
                    }
                }
            }

            reportIdItemDescItemPriceListMapMap
                    .put(tmpReportId, tmpItemDescItemPriceListMap);
        }

        List<ReportDetail> oldReportDetailList = getAllByIdIn(reportDetailIdSet);

        if (oldReportDetailList == null)
            return null;

        Set<Long> oldReportIdSet = new HashSet<>();
        Set<Long> oldUpdateBySet = new HashSet<>();

        Map<Long, Long> reportDetailIdCreatedByMap = new HashMap<>();
        Map<Long, LocalDateTime> reportDetailIdCreatedAtMap = new HashMap<>();

        for (ReportDetail oldReportDetail : oldReportDetailList) {
            oldReportIdSet.add(oldReportDetail.getReportId());

            if (oldReportDetail.getUpdatedBy() != null)
                oldUpdateBySet.add(oldReportDetail.getUpdatedBy());

            reportDetailIdCreatedByMap.put(oldReportDetail.getReportDetailId(), oldReportDetail.getCreatedBy());
            reportDetailIdCreatedAtMap.put(oldReportDetail.getReportDetailId(), oldReportDetail.getCreatedAt());
        }

        /* Remove all unchanged reportId */
        updatedReportIdSet.removeAll(oldReportIdSet);
        /* Remove all unchanged updateBy */
        updatedUpdatedBySet.removeAll(oldUpdateBySet);

        /* Check FK (if change) */
        if (!updatedReportIdSet.isEmpty()) {
            if (!reportService.existsAllByIdIn(updatedReportIdSet)) {
                errorMsg.append("1 or more Report not found with Id. ")
                        .append("Which violate constraint: FK_ReportDetail_Report. ");
            }
        }
        if (!updatedUpdatedBySet.isEmpty()) {
            if (!userService.existsAllByIdIn(updatedUpdatedBySet)) {
                errorMsg.append("1 or more User (UpdatedBy) not found with Id. ")
                        .append("Which violate constraint: FK_ReportDetail_User_UpdatedBy. ");
            }
        }

        /* Check duplicate 2 (input vs DB) */
        for (ReportDetail updatedReportDetail : updatedReportDetailList) {
            if (reportDetailRepository
                    .existsByReportIdAndItemDescAndItemPriceAndReportDetailIdIsNotAndStatusNotIn(
                            updatedReportDetail.getReportId(),
                            updatedReportDetail.getItemDesc(),
                            updatedReportDetail.getItemPrice(),
                            updatedReportDetail.getReportDetailId(),
                            N_D_S_STATUS_LIST)) {
                errorMsg.append("Already exist another ReportDetail of Report with Id: '")
                        .append(updatedReportDetail.getReportId())
                        .append("'. With itemDesc: '").append(updatedReportDetail.getItemDesc())
                        .append("' at price: '").append(updatedReportDetail.getItemPrice()).append("'. ");
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        updatedReportDetailList =
                updatedReportDetailList.stream()
                        .peek(reportDetail -> {
                            long reportDetailId = reportDetail.getReportDetailId();

                            reportDetail.setCreatedAt(
                                    reportDetailIdCreatedAtMap.get(reportDetailId));

                            reportDetail.setCreatedBy(
                                    reportDetailIdCreatedByMap.get(reportDetailId));})
                        .collect(Collectors.toList());

        return reportDetailRepository.saveAllAndFlush(updatedReportDetailList);
    }
    @Override
    public List<ReportDetailReadDTO> updateBulkReportDetailByDTOList(Collection<ReportDetailUpdateDTO> updatedReportDetailDTOList) throws Exception {
        List<ReportDetail> updatedReportDetailList =
                updatedReportDetailDTOList.stream()
                        .map(updatedReportDetailDTO -> modelMapper.map(updatedReportDetailDTO, ReportDetail.class))
                        .collect(Collectors.toList());

        updatedReportDetailList = updateBulkReportDetail(updatedReportDetailList);

        if (updatedReportDetailList == null)
            return null;

        return fillAllDTO(updatedReportDetailList, null);
    }

    /* DELETE */
    @Override
    public boolean deleteReportDetail(long reportDetailId) throws Exception {
        Optional<ReportDetail> reportDetail =
                reportDetailRepository.findByReportDetailIdAndStatusNotIn(reportDetailId, N_D_S_STATUS_LIST);

        if (!reportDetail.isPresent()) {
            return false;
            /* Not found with Id */
        }

        reportDetail.get().setStatus(Status.DELETED);
        reportDetailRepository.saveAndFlush(reportDetail.get());

        return true;
    }

    @Override
    public boolean deleteAllByReportId(long reportId) throws Exception {
        List<ReportDetail> reportDetailList = getAllByReportId(reportId);

        if (reportDetailList == null) {
            return false;
            /* Not found with reportId */
        }

        reportDetailRepository.saveAllAndFlush(
                reportDetailList.stream()
                        .peek(reportDetail -> reportDetail.setStatus(Status.DELETED))
                        .collect(Collectors.toList()));

        return true;
    }
    
    @Override
    public boolean deleteAllByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<ReportDetail> reportDetailList = getAllByReportIdIn(reportIdCollection);

        if (reportDetailList == null) {
            return false;
            /* Not found with any reportId */
        }

        reportDetailRepository.saveAllAndFlush(
                reportDetailList.stream()
                        .peek(reportDetail -> reportDetail.setStatus(Status.DELETED))
                        .collect(Collectors.toList()));

        return true;
    }

    /* Utils */
    private ReportDetailReadDTO fillDTO(ReportDetail reportDetail) throws Exception {
        modelMapper.typeMap(ReportDetail.class, ReportDetailReadDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(ReportDetailReadDTO::setCreatedAt);
                    mapper.skip(ReportDetailReadDTO::setUpdatedAt);});

        ReportDetailReadDTO reportDetailDTO =
                modelMapper.map(reportDetail, ReportDetailReadDTO.class);

        if (reportDetail.getCreatedAt() != null)
            reportDetailDTO.setCreatedAt(reportDetail.getCreatedAt().format(dateTimeFormatter));
        if (reportDetail.getUpdatedAt() != null)
            reportDetailDTO.setUpdatedAt(reportDetail.getUpdatedAt().format(dateTimeFormatter));

        return reportDetailDTO;
    }

    private List<ReportDetailReadDTO> fillAllDTO(Collection<ReportDetail> reportDetailCollection, Integer totalPage) throws Exception {
        modelMapper.typeMap(ReportDetail.class, ReportDetailReadDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(ReportDetailReadDTO::setCreatedAt);
                    mapper.skip(ReportDetailReadDTO::setUpdatedAt);});

        return reportDetailCollection.stream()
                .map(reportDetail -> {
                    ReportDetailReadDTO reportDetailDTO =
                            modelMapper.map(reportDetail, ReportDetailReadDTO.class);

                    if (reportDetail.getCreatedAt() != null)
                        reportDetailDTO.setCreatedAt(reportDetail.getCreatedAt().format(dateTimeFormatter));
                    if (reportDetail.getUpdatedAt() != null)
                        reportDetailDTO.setUpdatedAt(reportDetail.getUpdatedAt().format(dateTimeFormatter));

                    reportDetailDTO.setTotalPage(totalPage);

                    return reportDetailDTO;})
                .collect(Collectors.toList());
    }
}
