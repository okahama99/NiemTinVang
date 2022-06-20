package com.ntv.ntvcons_backend.services.requestDetail;


import com.ntv.ntvcons_backend.entities.RequestDetail;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.ShowRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.UpdateRequestDetailModel;

import java.util.List;

public interface RequestDetailService {
    /* CREATE */
    boolean createRequest(CreateRequestDetailModel createRequestDetailModel);

    /* READ */
    List<ShowRequestDetailModel> getAllAvailableRequestDetail(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<RequestDetail> getRequestDetailByRequestId(Long requestId);

    /* UPDATE */
    boolean updateRequestDetail(UpdateRequestDetailModel updateRequestDetailModel);

    /* DELETE */
    boolean deleteRequestDetail(Long requestDetailId);

}
