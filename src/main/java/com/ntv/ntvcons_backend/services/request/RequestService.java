package com.ntv.ntvcons_backend.services.request;

import com.ntv.ntvcons_backend.dtos.request.RequestReadDTO;
import com.ntv.ntvcons_backend.entities.Request;
import com.ntv.ntvcons_backend.entities.RequestModels.CreateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.ShowRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestVerifierModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RequestService {
    /* CREATE */
    boolean createRequest(CreateRequestModel createRequestModel);
    /* READ */
    List<ShowRequestModel> getAllAvailableRequest(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowRequestModel> getByProjectId(Long projectId, int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Request> getAllByProjectId(long projectId) throws Exception;
    List<RequestReadDTO> getAllDTOByProjectId(long projectId) throws Exception;

    List<Request> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    List<RequestReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    Map<Long, List<RequestReadDTO>> mapProjectIdRequestDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;

    ShowRequestModel getByRequestId(Long requestId);
    /* UPDATE */
    boolean updateRequest(UpdateRequestModel updateRequestModel);

    boolean updateVerifier(UpdateRequestVerifierModel updateRequestVerifierModel);

    boolean approveUpdate(Long requestId, Boolean decision);
    /* DELETE */
    boolean deleteRequest(Long requestId);
}
