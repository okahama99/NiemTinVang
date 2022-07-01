package com.ntv.ntvcons_backend.services.request;

import com.ntv.ntvcons_backend.entities.RequestModels.CreateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.ShowRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestVerifierModel;

import java.util.List;

public interface RequestService {
    /* CREATE */
    boolean createRequest(CreateRequestModel createRequestModel);
    /* READ */
    List<ShowRequestModel> getAllAvailableRequest(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowRequestModel> getByProjectId(Long projectId, int pageNo, int pageSize, String sortBy, boolean sortType);

    ShowRequestModel getByRequestId(Long requestId);
    /* UPDATE */
    boolean updateRequest(UpdateRequestModel updateRequestModel);

    boolean updateVerifier(UpdateRequestVerifierModel updateRequestVerifierModel);

    boolean approveUpdate(Long requestId, Boolean decision);
    /* DELETE */
    boolean deleteRequest(Long requestId);
}
