package com.ntv.ntvcons_backend.services.requestDetail;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailUpdateDTO;
import com.ntv.ntvcons_backend.entities.*;
import com.ntv.ntvcons_backend.entities.RequestDetail;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.ShowRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.UpdateRequestDetailModel;
import com.ntv.ntvcons_backend.repositories.RequestDetailRepository;
import com.ntv.ntvcons_backend.repositories.RequestRepository;
import com.ntv.ntvcons_backend.services.request.RequestService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestDetailServiceImpl implements RequestDetailService {
    @Autowired
    private RequestDetailRepository requestDetailRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private RequestService requestService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    /* CREATE */
    @Override
    public boolean createRequestDetail(CreateRequestDetailModel createRequestDetailModel) {
        Optional<Request> request = requestRepository.findById(createRequestDetailModel.getRequestId());
        if (request!=null)
        {
            RequestDetail requestDetail = new RequestDetail();
            requestDetail.setRequestId(createRequestDetailModel.getRequestId());
            requestDetail.setItemAmount(createRequestDetailModel.getItemAmount());
            requestDetail.setItemDesc(createRequestDetailModel.getItemDesc());
            requestDetail.setItemPrice(createRequestDetailModel.getItemPrice());
            requestDetail.setItemUnit(createRequestDetailModel.getItemUnit());

            requestDetail.setCreatedAt(LocalDateTime.now());
            requestDetail.setCreatedBy(request.get().getRequesterId());
            requestDetailRepository.saveAndFlush(requestDetail);
            return true;
        }
        return false;
    }

    @Override
    public RequestDetail createRequestDetail(RequestDetail newRequestDetail) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!requestService.existsById(newRequestDetail.getRequestId())) {
            errorMsg += "No RequestType found with Id: '" + newRequestDetail.getRequestId()
                    + "'. Which violate constraint: FK_RequestDetail_Request. ";
        }
        if (!userService.existsById(newRequestDetail.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newRequestDetail.getCreatedBy()
                    + "'. Which violate constraint: FK_RequestDetail_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (requestDetailRepository
                .existsByRequestIdAndItemDescAndItemPriceAndIsDeletedIsFalse(
                        newRequestDetail.getRequestId(),
                        newRequestDetail.getItemDesc(),
                        newRequestDetail.getItemPrice())) {
            errorMsg += "Already exist another RequestDetail of Request with Id: '" + newRequestDetail.getRequestId()
                    + "'. With itemDesc: '" + newRequestDetail.getItemDesc()
                    + "' at price: '" + newRequestDetail.getItemPrice() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return requestDetailRepository.saveAndFlush(newRequestDetail);
    }
    @Override
    public RequestDetailReadDTO createRequestDetailByDTO(RequestDetailCreateDTO newRequestDetailDTO) throws Exception {
        RequestDetail newRequestDetail = modelMapper.map(newRequestDetailDTO, RequestDetail.class);

        newRequestDetail = createRequestDetail(newRequestDetail);

        return fillDTO(newRequestDetail);
    }

    @Override
    public List<RequestDetail> createBulkRequestDetail(List<RequestDetail> newRequestDetailList) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        Set<Long> requestIdSet = new HashSet<>();
        Set<Long> createdBySet = new HashSet<>();

        Map<Long, Map<String, List<Double>>> requestIdItemDescItemPriceListMapMap = new HashMap<>();

        Long tmpRequestId;
        String tmpItemDesc;
        Double tmpItemPrice;
        List<Double> tmpItemPriceList;

        Map<String, List<Double>> tmpItemDescItemPriceListMap;

        for (RequestDetail newRequestDetail : newRequestDetailList) {
            tmpRequestId = newRequestDetail.getRequestId();
            tmpItemDesc = newRequestDetail.getItemDesc();
            tmpItemPrice = newRequestDetail.getItemPrice();

            requestIdSet.add(tmpRequestId);
            createdBySet.add(newRequestDetail.getCreatedBy());

            tmpItemDescItemPriceListMap = requestIdItemDescItemPriceListMapMap.get(tmpRequestId);

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
                        errorMsg.append("Already exist another RequestDetail of Request with Id: '").append(tmpRequestId)
                                .append("'. With itemDesc: '").append(tmpItemDesc)
                                .append("' at price: '").append(tmpItemPrice).append("'. ");
                    } else {
                        tmpItemPriceList.add(tmpItemPrice);

                        tmpItemDescItemPriceListMap
                                .put(tmpItemDesc, tmpItemPriceList);
                    }
                }
            }

            requestIdItemDescItemPriceListMapMap
                    .put(tmpRequestId, tmpItemDescItemPriceListMap);
        }

        /* Check FK */
        if (!requestService.existsAllByIdIn(requestIdSet)) {
            errorMsg.append("1 or more Request not found with Id: '")
                    .append("'. Which violate constraint: FK_RequestDetail_Request. ");
        }
        if (!userService.existsAllByIdIn(createdBySet)) {
            errorMsg.append("1 or more User (CreatedBy) not found with Id: '")
                    .append("'. Which violate constraint: FK_RequestDetail_User_CreatedBy. ");
        }

        /* Check duplicate 2 (input vs DB) */
        for (RequestDetail newRequestDetail : newRequestDetailList) {
            if (requestDetailRepository
                    .existsByRequestIdAndItemDescAndItemPriceAndIsDeletedIsFalse(
                            newRequestDetail.getRequestId(),
                            newRequestDetail.getItemDesc(),
                            newRequestDetail.getItemPrice())) {
                errorMsg.append("Already exist another RequestDetail of Request with Id: '")
                        .append(newRequestDetail.getRequestId())
                        .append("'. With itemDesc: '").append(newRequestDetail.getItemDesc())
                        .append("' at price: '").append(newRequestDetail.getItemPrice()).append("'. ");
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        return requestDetailRepository.saveAllAndFlush(newRequestDetailList);
    }
    @Override
    public List<RequestDetailReadDTO> createBulkRequestDetailByDTOList(List<RequestDetailCreateDTO> newRequestDetailDTOList) throws Exception {
        List<RequestDetail> newRequestDetailList =
                newRequestDetailDTOList.stream()
                        .map(newRequestDetailDTO -> modelMapper.map(newRequestDetailDTO, RequestDetail.class))
                        .collect(Collectors.toList());

        newRequestDetailList = createBulkRequestDetail(newRequestDetailList);

        return fillAllDTO(newRequestDetailList, null);
    }

    /* READ */
    @Override
    public List<ShowRequestDetailModel> getAllAvailableRequestDetail(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<RequestDetail> pagingResult = requestDetailRepository.findAllByIsDeletedIsFalse(paging);

        if (pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);

            Page<ShowRequestDetailModel> modelResult = pagingResult.map(new Converter<RequestDetail, ShowRequestDetailModel>() {

                @Override
                protected ShowRequestDetailModel doForward(RequestDetail requestDetail) {
                    ShowRequestDetailModel model = new ShowRequestDetailModel();
                    model.setRequestId(requestDetail.getRequestId());
                    model.setRequestDetailId(requestDetail.getRequestDetailId());
                    model.setItemAmount(requestDetail.getItemAmount());
                    model.setItemPrice(requestDetail.getItemPrice());
                    model.setItemUnit(requestDetail.getItemUnit());
                    model.setItemDesc(requestDetail.getItemDesc());

                    model.setCreatedAt(requestDetail.getCreatedAt());
                    model.setCreatedBy(requestDetail.getCreatedBy());
                    model.setUpdatedAt(requestDetail.getCreatedAt());
                    model.setUpdatedBy(requestDetail.getUpdatedBy());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected RequestDetail doBackward(ShowRequestDetailModel showRequestDetailModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ShowRequestDetailModel>();
        }
    }

    @Override
    public Page<RequestDetail> getPageAll(Pageable paging) throws Exception {
        return null;
    }
    @Override
    public List<RequestDetailReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        return null;
    }

    @Override
    public RequestDetail getById(long requestDetailId) throws Exception {
        return requestDetailRepository
                .findByRequestDetailIdAndIsDeletedIsFalse(requestDetailId)
                .orElse(null);
    }
    @Override
    public RequestDetailReadDTO getDTOById(long requestDetailId) throws Exception {
        RequestDetail requestDetail = getById(requestDetailId);

        if (requestDetail == null)
            return null;

        return fillDTO(requestDetail);
    }

    @Override
    public List<RequestDetail> getAllByIdIn(Collection<Long> requestDetailIdCollection) throws Exception {
        List<RequestDetail> requestDetailList =
                requestDetailRepository.findAllByRequestDetailIdInAndIsDeletedIsFalse(requestDetailIdCollection);

        if (requestDetailList.isEmpty())
            return null;

        return requestDetailList;
    }
    @Override
    public List<RequestDetailReadDTO> getAllDTOByIdIn(Collection<Long> requestDetailIdCollection) throws Exception {
        List<RequestDetail> requestDetailList = getAllByIdIn(requestDetailIdCollection);

        if (requestDetailList == null)
            return null;

        return fillAllDTO(requestDetailList, null);
    }

    @Override
    public List<RequestDetail> getRequestDetailByRequestId(Long requestId) {
        return requestDetailRepository.findAllByRequestIdAndIsDeletedIsFalse(requestId);
    }

    @Override
    public List<RequestDetail> getAllByRequestId(long requestId) throws Exception {
        List<RequestDetail> requestDetailList =
                requestDetailRepository.findAllByRequestIdAndIsDeletedIsFalse(requestId);

        if (requestDetailList.isEmpty()) 
            return null;

        return requestDetailList;
    }
    @Override
    public List<RequestDetailReadDTO> getAllDTOByRequestId(long requestId) throws Exception {
        List<RequestDetail> requestDetailList = getAllByRequestId(requestId);

        if (requestDetailList == null) 
            return null;

        return requestDetailList.stream()
                .map(requestDetail -> modelMapper.map(requestDetail, RequestDetailReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDetail> getAllByRequestIdIn(Collection<Long> requestIdCollection) throws Exception {
        List<RequestDetail> requestDetailList =
                requestDetailRepository.findAllByRequestIdInAndIsDeletedIsFalse(requestIdCollection);

        if (requestDetailList.isEmpty()) 
            return null;

        return requestDetailList;
    }
    @Override
    public List<RequestDetailReadDTO> getAllDTOByRequestIdIn(Collection<Long> requestIdCollection) throws Exception {
        List<RequestDetail> requestDetailList = getAllByRequestIdIn(requestIdCollection);

        if (requestDetailList == null) 
            return null;

        return fillAllDTO(requestDetailList, null);
    }
    @Override
    public Map<Long, List<RequestDetailReadDTO>> mapRequestIdRequestDetailDTOListByRequestIdIn(Collection<Long> requestIdCollection) throws Exception {
        List<RequestDetailReadDTO> requestDetailDTOList = getAllDTOByRequestIdIn(requestIdCollection);

        if (requestDetailDTOList == null) 
            return new HashMap<>();

        Map<Long, List<RequestDetailReadDTO>> requestIdRequestDetailDTOListMap = new HashMap<>();

        long tmpRequestId;
        List<RequestDetailReadDTO> tmpRequestDetailDTOList;

        for (RequestDetailReadDTO requestDetailDTO : requestDetailDTOList) {
            tmpRequestId = requestDetailDTO.getRequestId();
            tmpRequestDetailDTOList = requestIdRequestDetailDTOListMap.get(tmpRequestId);

            if (tmpRequestDetailDTOList == null) {
                requestIdRequestDetailDTOListMap
                        .put(tmpRequestId, new ArrayList<>(Collections.singletonList(requestDetailDTO)));
            } else {
                tmpRequestDetailDTOList.add(requestDetailDTO);

                requestIdRequestDetailDTOListMap.put(tmpRequestId, requestDetailDTOList);
            }
        }

        return requestIdRequestDetailDTOListMap;
    }

    /* UPDATE */
    @Override
    public boolean updateRequestDetail(UpdateRequestDetailModel updateRequestDetailModel) {
        Optional<RequestDetail> requestDetail = requestDetailRepository.findByRequestDetailIdAndIsDeletedIsFalse(updateRequestDetailModel.getRequestDetailId());
        if (requestDetail != null)
        {
            requestDetail.get().setItemUnit(updateRequestDetailModel.getItemUnit());
            requestDetail.get().setItemDesc(updateRequestDetailModel.getItemDesc());
            requestDetail.get().setItemPrice(updateRequestDetailModel.getItemPrice());
            requestDetail.get().setItemAmount(updateRequestDetailModel.getItemAmount());
            requestDetailRepository.saveAndFlush(requestDetail.get());
            return true;
        }
        return false;
    }

    @Override
    public RequestDetail updateRequestDetail(RequestDetail updatedRequestDetail) throws Exception {
        RequestDetail oldRequestDetail = getById(updatedRequestDetail.getRequestDetailId());

        if (oldRequestDetail == null)
            return null; /* Not found by Id, return null */

        String errorMsg = "";

        /* Check FK */
        if (!oldRequestDetail.getRequestId().equals(updatedRequestDetail.getRequestId())) {
            if (!requestService.existsById(updatedRequestDetail.getRequestId())) {
                errorMsg += "No Request found with Id: '" + updatedRequestDetail.getRequestId()
                        + "'. Which violate constraint: FK_RequestDetail_Request. ";
            }
        }
        if (oldRequestDetail.getUpdatedBy() != null) {
            if (!oldRequestDetail.getUpdatedBy().equals(updatedRequestDetail.getUpdatedBy())) {
                if (!requestService.existsById(updatedRequestDetail.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedRequestDetail.getUpdatedBy()
                            + "'. Which violate constraint: FK_RequestDetail_User_UpdatedBy. ";
                }
            }
        } else {
            if (!requestService.existsById(updatedRequestDetail.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedRequestDetail.getUpdatedBy()
                        + "'. Which violate constraint: FK_RequestDetail_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (requestDetailRepository
                .existsByRequestIdAndItemDescAndItemPriceAndRequestDetailIdIsNotAndIsDeletedIsFalse(
                        updatedRequestDetail.getRequestId(),
                        updatedRequestDetail.getItemDesc(),
                        updatedRequestDetail.getItemPrice(),
                        updatedRequestDetail.getRequestDetailId())) {
            errorMsg += "Already exist another RequestDetail of Request with Id: '" + updatedRequestDetail.getRequestId()
                    + "'. With itemDesc: '" + updatedRequestDetail.getItemDesc()
                    + "' at price: '" + updatedRequestDetail.getItemPrice() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedRequestDetail.setCreatedAt(oldRequestDetail.getCreatedAt());
        updatedRequestDetail.setCreatedBy(oldRequestDetail.getCreatedBy());

        return requestDetailRepository.saveAndFlush(updatedRequestDetail);
    }
    @Override
    public RequestDetailReadDTO updateRequestDetailByDTO(RequestDetailUpdateDTO updatedRequestDetailDTO) throws Exception {
        RequestDetail updatedRequestDetail = modelMapper.map(updatedRequestDetailDTO, RequestDetail.class);

        updatedRequestDetail = updateRequestDetail(updatedRequestDetail);

        if (updatedRequestDetail == null)
            return null;

        return fillDTO(updatedRequestDetail);
    }

    @Override
    public List<RequestDetail> updateBulkRequestDetail(List<RequestDetail> updatedRequestDetailList) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        Map<Long, Long> requestDetailIdCreatedByMap = new HashMap<>();
        Map<Long, LocalDateTime> requestDetailIdCreatedAtMap = new HashMap<>();

        Map<Long, Map<String, List<Double>>> requestIdItemDescItemPriceListMapMap = new HashMap<>();

        Set<Long> requestDetailIdSet = new HashSet<>();
        Set<Long> oldRequestIdSet = new HashSet<>();
        Set<Long> updatedRequestIdSet = new HashSet<>();
        Set<Long> oldUpdateBySet = new HashSet<>();
        Set<Long> updatedUpdatedBySet = new HashSet<>();

        Long tmpRequestId;
        String tmpItemDesc;
        Double tmpItemPrice;
        List<Double> tmpItemPriceList;

        Map<String, List<Double>> tmpItemDescItemPriceListMap;

        for (RequestDetail updatedRequestDetail : updatedRequestDetailList) {
            tmpRequestId = updatedRequestDetail.getRequestId();
            tmpItemDesc = updatedRequestDetail.getItemDesc();
            tmpItemPrice = updatedRequestDetail.getItemPrice();

            requestDetailIdSet.add(updatedRequestDetail.getRequestDetailId());
            updatedRequestIdSet.add(tmpRequestId);
            updatedUpdatedBySet.add(updatedRequestDetail.getUpdatedBy());

            tmpItemDescItemPriceListMap = requestIdItemDescItemPriceListMapMap.get(tmpRequestId);

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
                        errorMsg.append("Already exist another RequestDetail of Request with Id: '").append(tmpRequestId)
                                .append("'. With itemDesc: '").append(tmpItemDesc)
                                .append("' at price: '").append(tmpItemPrice).append("'. ");
                    } else {
                        tmpItemPriceList.add(tmpItemPrice);

                        tmpItemDescItemPriceListMap
                                .put(tmpItemDesc, tmpItemPriceList);
                    }
                }
            }

            requestIdItemDescItemPriceListMapMap
                    .put(tmpRequestId, tmpItemDescItemPriceListMap);
        }

        List<RequestDetail> oldRequestDetailList = getAllByIdIn(requestDetailIdSet);

        if (oldRequestDetailList == null)
            return null;

        for (RequestDetail oldRequestDetail : oldRequestDetailList) {
            oldRequestIdSet.add(oldRequestDetail.getRequestId());

            if (oldRequestDetail.getUpdatedBy() != null)
                oldUpdateBySet.add(oldRequestDetail.getUpdatedBy());

            requestDetailIdCreatedAtMap.put(oldRequestDetail.getRequestDetailId(), oldRequestDetail.getCreatedAt());
            requestDetailIdCreatedByMap.put(oldRequestDetail.getRequestDetailId(), oldRequestDetail.getCreatedBy());
        }

        /* Remove all unchanged requestId */
        updatedRequestIdSet.removeAll(oldRequestIdSet);
        /* Remove all unchanged updateBy */
        updatedUpdatedBySet.removeAll(oldUpdateBySet);

        /* Check FK (if change) */
        if (!updatedRequestIdSet.isEmpty()) {
            if (!requestService.existsAllByIdIn(updatedRequestIdSet)) {
                errorMsg.append("1 or more Request not found with Id: '")
                        .append("'. Which violate constraint: FK_RequestDetail_Request. ");
            }
        }
        if (!updatedUpdatedBySet.isEmpty()) {
            if (!userService.existsAllByIdIn(updatedUpdatedBySet)) {
                errorMsg.append("1 or more User (UpdatedBy) not found with Id: '")
                        .append("'. Which violate constraint: FK_RequestDetail_User_UpdatedBy. ");
            }
        }

        /* Check duplicate 2 (input vs DB) */
        for (RequestDetail updatedRequestDetail : updatedRequestDetailList) {
            if (requestDetailRepository
                    .existsByRequestIdAndItemDescAndItemPriceAndRequestDetailIdIsNotAndIsDeletedIsFalse(
                            updatedRequestDetail.getRequestId(),
                            updatedRequestDetail.getItemDesc(),
                            updatedRequestDetail.getItemPrice(),
                            updatedRequestDetail.getRequestDetailId())) {
                errorMsg.append("Already exist another RequestDetail of Request with Id: '")
                        .append(updatedRequestDetail.getRequestId())
                        .append("'. With itemDesc: '").append(updatedRequestDetail.getItemDesc())
                        .append("' at price: '").append(updatedRequestDetail.getItemPrice()).append("'. ");
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        updatedRequestDetailList =
                updatedRequestDetailList.stream()
                        .peek(requestDetail -> {
                            requestDetail.setCreatedAt(
                                    requestDetailIdCreatedAtMap.get(requestDetail.getRequestDetailId()));

                            requestDetail.setCreatedBy(
                                    requestDetailIdCreatedByMap.get(requestDetail.getRequestDetailId()));})
                        .collect(Collectors.toList());

        return requestDetailRepository.saveAllAndFlush(updatedRequestDetailList);
    }
    @Override
    public List<RequestDetailReadDTO> updateBulkRequestDetailByDTOList(List<RequestDetailUpdateDTO> updatedRequestDetailDTOList) throws Exception {
        List<RequestDetail> updatedRequestDetailList =
                updatedRequestDetailDTOList.stream()
                        .map(updatedRequestDetailDTO -> modelMapper.map(updatedRequestDetailDTO, RequestDetail.class))
                        .collect(Collectors.toList());

        updatedRequestDetailList = updateBulkRequestDetail(updatedRequestDetailList);

        if (updatedRequestDetailList == null)
            return null;

        return fillAllDTO(updatedRequestDetailList, null);
    }

    /* DELETE */
    @Override
    public boolean deleteRequestDetail(Long requestDetailId) {
        RequestDetail requestDetail = requestDetailRepository.findById(requestDetailId).orElse(null);
        if (requestDetail != null){
            requestDetail.setIsDeleted(true);
            requestDetailRepository.saveAndFlush(requestDetail);
            return true;
        }
        return false;
    }

    /* Utils */
    private RequestDetailReadDTO fillDTO(RequestDetail requestDetail) throws Exception {
        return modelMapper.map(requestDetail, RequestDetailReadDTO.class);
    }

    private List<RequestDetailReadDTO> fillAllDTO(List<RequestDetail> requestDetailList, Integer totalPage) throws Exception {
        return requestDetailList.stream()
                .map(requestDetail -> {
                    RequestDetailReadDTO requestDetailDTO =
                            modelMapper.map(requestDetail, RequestDetailReadDTO.class);

                    requestDetailDTO.setTotalPage(totalPage);

                    return requestDetailDTO;})
                .collect(Collectors.toList());
    }
}
