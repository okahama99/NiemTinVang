package com.ntv.ntvcons_backend.services.requestDetail;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailReadDTO;
import com.ntv.ntvcons_backend.entities.Request;
import com.ntv.ntvcons_backend.entities.RequestDetail;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.ShowRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.UpdateRequestDetailModel;
import com.ntv.ntvcons_backend.repositories.RequestDetailRepository;
import com.ntv.ntvcons_backend.repositories.RequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ModelMapper modelMapper;

    /* CREATE */
    @Override
    public boolean createRequest(CreateRequestDetailModel createRequestDetailModel) {
        Optional<Request> request = requestRepository.findById(createRequestDetailModel.getRequestId());
        if(request!=null)
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

    /* READ */
    @Override
    public List<ShowRequestDetailModel> getAllAvailableRequestDetail(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<RequestDetail> pagingResult = requestDetailRepository.findAllByIsDeletedIsFalse(paging);

        if(pagingResult.hasContent()){
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
    public List<RequestDetail> getRequestDetailByRequestId(Long requestId) {
        return requestDetailRepository.findAllByRequestIdAndIsDeletedIsFalse(requestId);
    }

    @Override
    public List<RequestDetail> getAllByRequestId(long requestId) {
        List<RequestDetail> requestDetailList =
                requestDetailRepository.findAllByRequestIdAndIsDeletedIsFalse(requestId);

        if (requestDetailList.isEmpty()) {
            return null;
        }

        return requestDetailList;
    }
    @Override
    public List<RequestDetailReadDTO> getAllDTOByRequestId(long requestId) {
        List<RequestDetail> requestDetailList = getAllByRequestId(requestId);

        if (requestDetailList == null) {
            return null;
        }

        return requestDetailList.stream()
                .map(requestDetail -> modelMapper.map(requestDetail, RequestDetailReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDetail> getAllByRequestIdIn(Collection<Long> requestIdCollection) {
        List<RequestDetail> requestDetailList =
                requestDetailRepository.findAllByRequestIdInAndIsDeletedIsFalse(requestIdCollection);

        if (requestDetailList.isEmpty()) {
            return null;
        }

        return requestDetailList;
    }
    @Override
    public List<RequestDetailReadDTO> getAllDTOByRequestIdIn(Collection<Long> requestIdCollection) {
        List<RequestDetail> requestDetailList = getAllByRequestIdIn(requestIdCollection);

        if (requestDetailList == null) {
            return null;
        }

        return requestDetailList.stream()
                .map(requestDetail -> modelMapper.map(requestDetail, RequestDetailReadDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public Map<Long, List<RequestDetailReadDTO>> mapRequestIdRequestDetailDTOListByRequestIdIn(Collection<Long> requestIdCollection) {
        List<RequestDetailReadDTO> requestDetailDTOList = getAllDTOByRequestIdIn(requestIdCollection);

        if (requestDetailDTOList == null) {
            return new HashMap<>();
        }

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
        if(requestDetail != null)
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

    /* DELETE */
    @Override
    public boolean deleteRequestDetail(Long requestDetailId) {
        RequestDetail requestDetail = requestDetailRepository.findById(requestDetailId).orElse(null);
        if(requestDetail != null){
            requestDetail.setIsDeleted(true);
            requestDetailRepository.saveAndFlush(requestDetail);
            return true;
        }
        return false;
    }
}
