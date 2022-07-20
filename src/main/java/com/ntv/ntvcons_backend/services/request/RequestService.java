package com.ntv.ntvcons_backend.services.request;

import com.ntv.ntvcons_backend.dtos.request.RequestCreateDTO;
import com.ntv.ntvcons_backend.dtos.request.RequestReadDTO;
import com.ntv.ntvcons_backend.dtos.request.RequestUpdateDTO;
import com.ntv.ntvcons_backend.entities.Request;
import com.ntv.ntvcons_backend.entities.RequestModels.CreateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.ShowRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestVerifierModel;
import com.ntv.ntvcons_backend.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RequestService extends BaseService {
    /* CREATE */
    boolean createRequest(CreateRequestModel createRequestModel);

    Request createRequest(Request newRequest) throws Exception;
    RequestReadDTO createRequestByDTO(RequestCreateDTO newRequestDTO) throws Exception;

    /* READ */
    List<ShowRequestModel> getAllAvailableRequest(int pageNo, int pageSize, String sortBy, boolean sortType);

    Page<Request> getPageAll(Pageable paging) throws Exception;
    List<RequestReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    ShowRequestModel getByRequestId(Long requestId);

    boolean existsById(long requestId) throws Exception;
    Request getById(long requestId) throws Exception;
    RequestReadDTO getDTOById(long requestId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> requestIdCollection) throws Exception;
    List<Request> getAllByIdIn(Collection<Long> requestIdCollection) throws Exception;
    List<RequestReadDTO> getAllDTOByIdIn(Collection<Long> requestIdCollection) throws Exception;

    List<ShowRequestModel> getByProjectId(Long projectId, int pageNo, int pageSize, String sortBy, boolean sortType);

    List<Request> getAllByProjectId(long projectId) throws Exception;
    List<RequestReadDTO> getAllDTOByProjectId(long projectId) throws Exception;
    Page<Request> getPageAllByProjectId(Pageable paging, long projectId) throws Exception;
    List<RequestReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception;

    List<Request> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    List<RequestReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    Map<Long, List<RequestReadDTO>> mapProjectIdRequestDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;
    Page<Request> getPageAllByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception;
    List<RequestReadDTO> getAllDTOInPagingByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception;

    List<Request> getAllByRequestName(String requestName) throws Exception;
    List<RequestReadDTO> getAllDTOByRequestName(String requestName) throws Exception;
    Page<Request> getPageAllByRequestName(Pageable paging, String requestName) throws Exception;
    List<RequestReadDTO> getAllDTOInPagingByRequestName(Pageable paging, String requestName) throws Exception;


    List<Request> getAllByRequestNameContains(String requestName) throws Exception;
    List<RequestReadDTO> getAllDTOByRequestNameContains(String requestName) throws Exception;
    Page<Request> getPageAllByRequestNameContains(Pageable paging, String requestName) throws Exception;
    List<RequestReadDTO> getAllDTOInPagingByRequestNameContains(Pageable paging, String requestName) throws Exception;

    List<Request> getAllByRequestTypeId(long requestTypeId) throws Exception;
    List<RequestReadDTO> getAllDTOByRequestTypeId(long requestTypeId) throws Exception;
    Page<Request> getPageAllByRequestTypeId(Pageable paging, long requestTypeId) throws Exception;
    List<RequestReadDTO> getAllDTOInPagingByRequestTypeId(Pageable paging, long requestTypeId) throws Exception;

    List<Request> getAllByRequesterId(long requesterId) throws Exception;
    List<RequestReadDTO> getAllDTOByRequesterId(long requesterId) throws Exception;
    Page<Request> getPageAllByRequesterId(Pageable paging, long requesterId) throws Exception;
    List<RequestReadDTO> getAllDTOInPagingByRequesterId(Pageable paging, long requesterId) throws Exception;

    List<Request> getAllByRequesterIdIn(Collection<Long> requesterIdCollection) throws Exception;
    List<RequestReadDTO> getAllDTOByRequesterIdIn(Collection<Long> requesterIdCollection) throws Exception;
    Page<Request> getPageAllByRequesterIdIn(Pageable paging, Collection<Long> requesterIdCollection) throws Exception;
    List<RequestReadDTO> getAllDTOInPagingByRequesterIdIn(Pageable paging, Collection<Long> requesterIdCollection) throws Exception;

    List<Request> getAllByVerifierId(long verifierId) throws Exception;
    List<RequestReadDTO> getAllDTOByVerifierId(long verifierId) throws Exception;
    Page<Request> getPageAllByVerifierId(Pageable paging, long verifierId) throws Exception;
    List<RequestReadDTO> getAllDTOInPagingByVerifierId(Pageable paging, long verifierId) throws Exception;

    List<Request> getAllByVerifierIdIn(Collection<Long> verifierIdCollection) throws Exception;
    List<RequestReadDTO> getAllDTOByVerifierIdIn(Collection<Long> verifierIdCollection) throws Exception;
    Page<Request> getPageAllByVerifierIdIn(Pageable paging, Collection<Long> verifierIdCollection) throws Exception;
    List<RequestReadDTO> getAllDTOInPagingByVerifierIdIn(Pageable paging, Collection<Long> verifierIdCollection) throws Exception;

    /* UPDATE */
    boolean updateRequest(UpdateRequestModel updateRequestModel);

    boolean updateVerifier(UpdateRequestVerifierModel updateRequestVerifierModel);

    boolean approveUpdate(Long requestId, Boolean decision);

    Request updateRequest(Request updatedRequest) throws Exception;
    RequestReadDTO updateRequestByDTO(RequestUpdateDTO updatedRequestDTO) throws Exception;

    /* DELETE */
    boolean deleteRequest(Long requestId) throws Exception;

    boolean deleteAllByProjectId(long projectId) throws Exception;
    boolean deleteAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception;

    boolean deleteAllByUserId(long userId) throws Exception;
    boolean deleteAllByUserIdIn(Collection<Long> userIdCollection) throws Exception;
}
