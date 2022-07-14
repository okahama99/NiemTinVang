package com.ntv.ntvcons_backend.services.requestDetail;


import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailUpdateDTO;
import com.ntv.ntvcons_backend.entities.RequestDetail;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.ShowRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.UpdateRequestDetailModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RequestDetailService {
    /* CREATE */
    boolean createRequestDetail(CreateRequestDetailModel createRequestDetailModel);

    RequestDetail createRequestDetail(RequestDetail newRequestDetail) throws Exception;
    RequestDetailReadDTO createRequestDetailByDTO(RequestDetailCreateDTO newRequestDetailDTO) throws Exception;

    List<RequestDetail> createBulkRequestDetail(List<RequestDetail> newRequestDetailList) throws Exception;
    List<RequestDetailReadDTO> createBulkRequestDetailByDTOList(List<RequestDetailCreateDTO> newRequestDetailDTOList) throws Exception;

    /* READ */
    List<ShowRequestDetailModel> getAllAvailableRequestDetail(int pageNo, int pageSize, String sortBy, boolean sortType);

    Page<RequestDetail> getPageAll(Pageable paging) throws Exception;
    List<RequestDetailReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    RequestDetail getById(long requestDetailId) throws Exception;
    RequestDetailReadDTO getDTOById(long requestDetailId) throws Exception;

    List<RequestDetail> getAllByIdIn(Collection<Long> requestDetailIdCollection) throws Exception;
    List<RequestDetailReadDTO> getAllDTOByIdIn(Collection<Long> requestDetailIdCollection) throws Exception;

    List<RequestDetail> getRequestDetailByRequestId(Long requestId);

    List<RequestDetail> getAllByRequestId(long requestId) throws Exception;
    List<RequestDetailReadDTO> getAllDTOByRequestId(long requestId) throws Exception;

    List<RequestDetail> getAllByRequestIdIn(Collection<Long> requestIdCollection) throws Exception;
    List<RequestDetailReadDTO> getAllDTOByRequestIdIn(Collection<Long> requestIdCollection) throws Exception;
    Map<Long, List<RequestDetailReadDTO>> mapRequestIdRequestDetailDTOListByRequestIdIn(Collection<Long> requestIdCollection) throws Exception;

    /* UPDATE */
    boolean updateRequestDetail(UpdateRequestDetailModel updateRequestDetailModel);

    RequestDetail updateRequestDetail(RequestDetail updatedRequestDetail) throws Exception;
    RequestDetailReadDTO updateRequestDetailByDTO(RequestDetailUpdateDTO updatedRequestDetailDTO) throws Exception;

    List<RequestDetail> updateBulkRequestDetail(List<RequestDetail> updatedRequestDetailList) throws Exception;
    List<RequestDetailReadDTO> updateBulkRequestDetailByDTOList(List<RequestDetailUpdateDTO> updatedRequestDetailDTOList) throws Exception;

    /* DELETE */
    boolean deleteRequestDetail(Long requestDetailId);
}
