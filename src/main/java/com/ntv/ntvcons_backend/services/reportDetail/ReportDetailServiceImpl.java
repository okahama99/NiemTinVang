package com.ntv.ntvcons_backend.services.reportDetail;

import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.reportDetail.ReportDetailUpdateDTO;
import com.ntv.ntvcons_backend.entities.ReportDetail;
import com.ntv.ntvcons_backend.repositories.ReportDetailRepository;
import com.ntv.ntvcons_backend.services.report.ReportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportDetailServiceImpl implements ReportDetailService {
    @Autowired
    private ReportDetailRepository reportDetailRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ReportService reportService;

    /* CREATE */
    @Override
    public ReportDetail createReportDetail(ReportDetail newReportDetail) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!reportService.existsById(newReportDetail.getReportId())) {
            errorMsg += "No Report found with Id: " + newReportDetail.getReportId()
                    + "Which violate constraint: FK_ReportDetail_Report. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return reportDetailRepository.saveAndFlush(newReportDetail);
    }
    @Override
    public ReportDetailReadDTO createReportDetailByDTO(ReportDetailCreateDTO newReportDetailDTO) throws Exception {
        ReportDetail newReportDetail = modelMapper.map(newReportDetailDTO, ReportDetail.class);

        newReportDetail = createReportDetail(newReportDetail);

        return modelMapper.map(newReportDetail, ReportDetailReadDTO.class);
    }

    @Override
    public List<ReportDetail> createBulkReportDetail(Collection<ReportDetail> newReportDetailList) throws Exception {
        String errorMsg = "";

        if (!reportService.existsAllByIdIn(
                newReportDetailList.stream()
                        .map(ReportDetail::getReportId)
                        .collect(Collectors.toSet()))) {
            errorMsg += "1 or more Report not found with Id"
                    + "Which violate constraint: FK_ReportDetail_Report. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return reportDetailRepository.saveAllAndFlush(newReportDetailList);
    }
    @Override
    public List<ReportDetailReadDTO> createBulkReportDetailByDTOList(Collection<ReportDetailCreateDTO> newReportDetailDTOList) throws Exception {
        List<ReportDetail> newReportDetailList = newReportDetailDTOList.stream()
                .map(newReportDetailDTO -> modelMapper.map(newReportDetailDTO, ReportDetail.class))
                .collect(Collectors.toList());

        newReportDetailList = createBulkReportDetail(newReportDetailList);

        return newReportDetailList.stream()
                .map(newReportDetail -> modelMapper.map(newReportDetail, ReportDetailReadDTO.class))
                .collect(Collectors.toList());
    }

    /* READ */
    @Override
    public List<ReportDetail> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<ReportDetail> reportDetailPage = reportDetailRepository.findAllByIsDeletedIsFalse(paging);

        if (reportDetailPage.isEmpty()) {
            return null;
        }

        return reportDetailPage.getContent();
    }
    @Override
    public List<ReportDetailReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        List<ReportDetail> reportDetailList = getAll(pageNo, pageSize, sortBy, sortType);

        if (reportDetailList != null && !reportDetailList.isEmpty()) {
            int totalPage = (int) Math.ceil((double) reportDetailList.size() / pageSize);

            return reportDetailList.stream()
                    .map(reportDetail -> {
                        ReportDetailReadDTO reportDetailReadDTO =
                                modelMapper.map(reportDetail, ReportDetailReadDTO.class);
                        reportDetailReadDTO.setTotalPage(totalPage);
                        return reportDetailReadDTO;
                    })
                    .collect(Collectors.toList());

        } 
            
        return null;
    }

    @Override
    public ReportDetail getById(long reportDetailId) throws Exception {
        Optional<ReportDetail> reportDetail =
                reportDetailRepository.findByReportDetailIdAndIsDeletedIsFalse(reportDetailId);

        return reportDetail.orElse(null);
    }
    @Override
    public ReportDetailReadDTO getDTOById(long reportDetailId) throws Exception {
        ReportDetail reportDetail = getById(reportDetailId);

        if (reportDetail == null) {
            return null;
        }

        return modelMapper.map(reportDetail, ReportDetailReadDTO.class);
    }

    @Override
    public List<ReportDetail> getAllByIdIn(Collection<Long> reportDetailIdCollection) throws Exception {
        List<ReportDetail> reportDetailList =
                reportDetailRepository.findAllByReportDetailIdInAndIsDeletedIsFalse(reportDetailIdCollection);

        if (reportDetailList.isEmpty()) {
            return null;
        }

        return reportDetailList;
    }
    @Override
    public List<ReportDetailReadDTO> getAllDTOByIdIn(Collection<Long> reportDetailIdCollection) throws Exception {
        List<ReportDetail> reportDetailList = getAllByIdIn(reportDetailIdCollection);

        if (reportDetailList == null) {
            return null;
        }

        return reportDetailList.stream()
                .map(reportDetail -> modelMapper.map(reportDetail, ReportDetailReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportDetail> getAllByReportId(long reportId) throws Exception {
        List<ReportDetail> reportDetailList =
                reportDetailRepository.findAllByReportIdAndIsDeletedIsFalse(reportId);

        if (reportDetailList.isEmpty()) {
            return null;
        }

        return reportDetailList;
    }
    @Override
    public List<ReportDetailReadDTO> getAllDTOByReportId(long reportId) throws Exception {
        List<ReportDetail> reportDetailList = getAllByReportId(reportId);

        if (reportDetailList == null) {
            return null;
        }

        return reportDetailList.stream()
                .map(reportDetail -> modelMapper.map(reportDetail, ReportDetailReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportDetail> getAllByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<ReportDetail> reportDetailList =
                reportDetailRepository.findAllByReportIdInAndIsDeletedIsFalse(reportIdCollection);

        if (reportDetailList.isEmpty()) {
            return null;
        }

        return reportDetailList;
    }
    @Override
    public List<ReportDetailReadDTO> getAllDTOByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<ReportDetail> reportDetailList = getAllByReportIdIn(reportIdCollection);

        if (reportDetailList == null) {
            return null;
        }

        return reportDetailList.stream()
                .map(reportDetail -> modelMapper.map(reportDetail, ReportDetailReadDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public Map<Long, List<ReportDetail>> mapReportIdReportDetailListByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<ReportDetail> reportDetailList = getAllByReportIdIn(reportIdCollection);

        if (reportDetailList == null) {
            return new HashMap<>();
        }

        Map<Long, List<ReportDetail>> reportIdReportDetailListMap = new HashMap<>();

        List<ReportDetail> tmpReportDetailList;
        long tmpReportId;

        for (ReportDetail reportDetail : reportDetailList) {
            tmpReportId = reportDetail.getReportId();
            tmpReportDetailList = reportIdReportDetailListMap.get(tmpReportId);

            if (tmpReportDetailList == null) {
                reportIdReportDetailListMap.put(tmpReportId, new ArrayList<>(Collections.singletonList(reportDetail)));
            } else {
                tmpReportDetailList.add(reportDetail);
                reportIdReportDetailListMap.put(tmpReportId, tmpReportDetailList);
            }
        }

        return reportIdReportDetailListMap;
    }

    @Override
    public Map<Long, List<ReportDetailReadDTO>> mapReportIdReportDetailDTOListByReportIdIn(Collection<Long> reportIdCollection) throws Exception {
        List<ReportDetailReadDTO> reportDetailDTOList = getAllDTOByReportIdIn(reportIdCollection);

        if (reportDetailDTOList == null) {
            return new HashMap<>();
        }

        Map<Long, List<ReportDetailReadDTO>> reportIdReportDetailDTOListMap = new HashMap<>();

        List<ReportDetailReadDTO> tmpReportDetailDTOList;
        long tmpReportId;

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

    /* UPDATE */
    @Override
    public ReportDetail updateReportDetail(ReportDetail updatedReportDetail) throws Exception {
        ReportDetail oldReportDetail = getById(updatedReportDetail.getReportDetailId());

        if (oldReportDetail == null) {
            return null;
            /* Not found by Id, return null */
        }

        String errorMsg = "";

        /* Check FK */
        if (!oldReportDetail.getReportId().equals(updatedReportDetail.getReportId())) {
            if (!reportService.existsById(updatedReportDetail.getReportId())) {
                errorMsg += "No Report found with Id: " + updatedReportDetail.getReportId()
                        + "Which violate constraint: FK_ReportDetail_Report. ";
            }
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return reportDetailRepository.saveAndFlush(updatedReportDetail);
    }
    @Override
    public ReportDetailReadDTO updateReportDetailByDTO(ReportDetailUpdateDTO updatedReportDetailDTO) throws Exception {
        ReportDetail updatedReportDetail = modelMapper.map(updatedReportDetailDTO, ReportDetail.class);

        updatedReportDetail = updateReportDetail(updatedReportDetail);

        if (updatedReportDetail == null) {
            return null;
        }

        return modelMapper.map(updatedReportDetail, ReportDetailReadDTO.class);
    }

    @Override
    public List<ReportDetail> updateBulkReportDetail(Collection<ReportDetail> updatedReportDetailList) throws Exception {
        Set<Long> reportDetailIdSet = new HashSet<>();
        Set<Long> oldReportIdSet = new HashSet<>();
        Set<Long> updatedReportIdSet = new HashSet<>();

        for (ReportDetail updatedReportDetail : updatedReportDetailList) {
            reportDetailIdSet.add(updatedReportDetail.getReportDetailId());
            updatedReportIdSet.add(updatedReportDetail.getReportId());
        }

        for (ReportDetail oldReportDetail : getAllByIdIn(reportDetailIdSet)) {
            oldReportIdSet.add(oldReportDetail.getReportId());
        }

        /* Remove all unchanged reportId */
        updatedReportIdSet.removeAll(oldReportIdSet);

        String errorMsg = "";

        /* Check FK */
        /* If there are updated reportId, need to recheck FK */
        if (updatedReportIdSet.size() <= 0) {
            if (!reportService.existsAllByIdIn(updatedReportIdSet)) {
                errorMsg += "1 or more Report not found with Id"
                        + "Which violate constraint: FK_ReportDetail_Report. ";
            }
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return reportDetailRepository.saveAllAndFlush(updatedReportDetailList);
    }
    @Override
    public List<ReportDetailReadDTO> updateBulkReportDetailByDTOList(Collection<ReportDetailUpdateDTO> updatedReportDetailDTOList) throws Exception {
        List<ReportDetail> updatedReportDetailList = updatedReportDetailDTOList.stream()
                .map(updatedReportDetailDTO -> modelMapper.map(updatedReportDetailDTO, ReportDetail.class))
                .collect(Collectors.toList());

        updatedReportDetailList = updateBulkReportDetail(updatedReportDetailList);

        return updatedReportDetailList.stream()
                .map(updatedReportDetail -> modelMapper.map(updatedReportDetail, ReportDetailReadDTO.class))
                .collect(Collectors.toList());
    }

    /* DELETE */
    @Override
    public boolean deleteReportDetail(long reportDetailId) throws Exception {
        Optional<ReportDetail> reportDetail = reportDetailRepository.findByReportDetailIdAndIsDeletedIsFalse(reportDetailId);

        if (!reportDetail.isPresent()) {
            return false;
            /* Not found with Id */
        }

        reportDetail.get().setIsDeleted(true);
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
                        .peek(reportDetail -> reportDetail.setIsDeleted(true))
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
                        .peek(reportDetail -> reportDetail.setIsDeleted(true))
                        .collect(Collectors.toList()));

        return true;
    }
}
