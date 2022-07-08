package com.ntv.ntvcons_backend.services.requestDetail;


import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailReadDTO;
import com.ntv.ntvcons_backend.entities.RequestDetail;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.ShowRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.UpdateRequestDetailModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RequestDetailService {
    /* CREATE */
    boolean createRequest(CreateRequestDetailModel createRequestDetailModel);

    /* READ */
    List<ShowRequestDetailModel> getAllAvailableRequestDetail(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<RequestDetail> getRequestDetailByRequestId(Long requestId);

    List<RequestDetail> getAllByRequestId(long requestId);
    List<RequestDetailReadDTO> getAllDTOByRequestId(long requestId);

    List<RequestDetail> getAllByRequestIdIn(Collection<Long> requestIdCollection);
    List<RequestDetailReadDTO> getAllDTOByRequestIdIn(Collection<Long> requestIdCollection);
    Map<Long, List<RequestDetailReadDTO>> mapRequestIdRequestDetailDTOListByRequestIdIn(Collection<Long> requestIdCollection);

    /* UPDATE */
    boolean updateRequestDetail(UpdateRequestDetailModel updateRequestDetailModel);

    /* DELETE */
    boolean deleteRequestDetail(Long requestDetailId);
}
