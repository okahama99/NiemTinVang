package com.ntv.ntvcons_backend.services.request;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.request.RequestCreateDTO;
import com.ntv.ntvcons_backend.dtos.request.RequestReadDTO;
import com.ntv.ntvcons_backend.dtos.request.RequestUpdateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailUpdateDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.entities.*;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.RequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.UpdateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestModels.CreateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.ShowRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestVerifierModel;
import com.ntv.ntvcons_backend.repositories.*;
import com.ntv.ntvcons_backend.services.entityWrapper.EntityWrapperService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import com.ntv.ntvcons_backend.services.requestDetail.RequestDetailService;
import com.ntv.ntvcons_backend.services.requestType.RequestTypeService;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService{
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Autowired
    private UserRepository userRepository;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RequestTypeRepository requestTypeRepository;
    @Autowired
    private RequestTypeService requestTypeService;
    @Autowired
    private RequestDetailService requestDetailService;
    @Autowired
    private RequestDetailRepository requestDetailRepository;
    @Autowired
    private EntityWrapperService entityWrapperService;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;

    private final EntityType ENTITY_TYPE = EntityType.REQUEST_ENTITY;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public boolean createRequest(CreateRequestModel createRequestModel) {
        Request request = new Request();
        request.setProjectId(createRequestModel.getProjectId());
        request.setRequesterId(createRequestModel.getRequesterId());
        request.setRequestTypeId(createRequestModel.getRequestTypeId());
        request.setRequestDesc(createRequestModel.getRequestDesc());
        request.setRequestName(createRequestModel.getRequestName());

        request.setCreatedAt(LocalDateTime.parse(createRequestModel.getRequestDate(), dateTimeFormatter));

        request.setCreatedBy(createRequestModel.getRequesterId());
        request.setRequestDate(LocalDateTime.parse(createRequestModel.getRequestDate(), dateTimeFormatter));
        requestRepository.saveAndFlush(request);
        if (createRequestModel.getModelList() != null) {
            for (RequestDetailModel requestDetailModel : createRequestModel.getModelList()) {
                CreateRequestDetailModel createRequestDetailModel = new CreateRequestDetailModel();
                createRequestDetailModel.setRequestId(request.getRequestId());
                createRequestDetailModel.setItemDesc(requestDetailModel.getItemDesc());
                createRequestDetailModel.setItemAmount(requestDetailModel.getItemAmount());
                createRequestDetailModel.setItemPrice(requestDetailModel.getItemPrice());
                createRequestDetailModel.setItemUnit(requestDetailModel.getItemUnit());
                requestDetailService.createRequestDetail(createRequestDetailModel);
            }
        }
        return true;
    }

    @Override
    public Request createRequest(Request newRequest) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!projectService.existsById(newRequest.getProjectId())) {
            errorMsg += "No Project found with Id: '" + newRequest.getProjectId()
                    + "'. Which violate constraint: FK_Request_Project. ";
        }
        if (!requestTypeService.existsById(newRequest.getRequestTypeId())) {
            errorMsg += "No RequestType found with Id: '" + newRequest.getRequestTypeId()
                    + "'. Which violate constraint: FK_Request_RequestType. ";
        }
        if (!userService.existsById(newRequest.getRequesterId())) {
            errorMsg += "No User (Requester) found with Id: '" + newRequest.getRequesterId()
                    + "'. Which violate constraint: FK_Request_User_RequesterId. ";
        }
        if (!userService.existsById(newRequest.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newRequest.getCreatedBy()
                    + "'. Which violate constraint: FK_Request_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (requestRepository
                .existsByProjectIdAndRequestNameAndStatusNotIn(
                        newRequest.getProjectId(),
                        newRequest.getRequestName(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another Request with name: '" + newRequest.getRequestName()
                    + "' for Project with Id:' " +  newRequest.getProjectId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return requestRepository.saveAndFlush(newRequest);
    }
    @Override
    public RequestReadDTO createRequestByDTO(RequestCreateDTO newRequestDTO) throws Exception {
        modelMapper.typeMap(RequestCreateDTO.class, Request.class)
                .addMappings(mapper -> {
                    mapper.skip(Request::setRequestDate);});

        Request newRequest = modelMapper.map(newRequestDTO, Request.class);

        /* Already check NOT NULL */
        newRequest.setRequestDate(
                LocalDateTime.parse(newRequestDTO.getRequestDate(), dateTimeFormatter));

//        if (newRequest.getRequestDate().isAfter(LocalDateTime.now()))
//            throw new IllegalArgumentException("requestDate can't be in the future");

        newRequest = createRequest(newRequest);

        long newRequestId = newRequest.getRequestId();

        /* Create associated EntityWrapper */
        entityWrapperService
                .createEntityWrapper(newRequestId, ENTITY_TYPE, newRequest.getCreatedBy());

        /* Create associated RequestDetail; Set required FK requestId */
        /* Already check NOT NULL & size > 1 */
        List<RequestDetailCreateDTO> newRequestDetailDTOList = newRequestDTO.getRequestDetailList();

        newRequestDetailDTOList = newRequestDetailDTOList.stream()
                .peek(newRequestDetailDTO -> newRequestDetailDTO.setRequestId(newRequestId))
                .collect(Collectors.toList());

        requestDetailService.createBulkRequestDetailByDTOList(newRequestDetailDTOList);

        return fillDTO(newRequest);
    }

    /* READ */
    @Override
    public List<ShowRequestModel> getAllAvailableRequest(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Request> pagingResult = requestRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        return fillAllModel(pagingResult);
    }

    @Override
    public Page<Request> getPageAll(Pageable paging) throws Exception {
        Page<Request> requestPage = requestRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (requestPage.isEmpty())
            return null;

        return requestPage;
    }
    @Override
    public List<RequestReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<Request> requestPage = getPageAll(paging);

        if (requestPage == null) 
            return null;

        List<Request> requestList = requestPage.getContent();

        if (requestList.isEmpty()) 
            return null;

        return fillAllDTO(requestList, requestPage.getTotalPages());
    }

    @Override
    public ShowRequestModel getByRequestId(Long requestId) {
        Optional<Request> request = requestRepository.findByRequestIdAndStatusNotIn(requestId, N_D_S_STATUS_LIST);
        ShowRequestModel model = new ShowRequestModel();
        if (request.isPresent()) {
            Optional<Project> project = projectRepository.findById(request.get().getProjectId());
            Optional<User> requester = userRepository.findById(request.get().getRequesterId());
            List<RequestDetail> detail =
                    requestDetailRepository.findAllByRequestIdAndStatusNotIn(request.get().getRequestId(), N_D_S_STATUS_LIST);

            Optional<RequestType> requestType = requestTypeRepository.findById(request.get().getRequestTypeId());

            model.setRequestId(request.get().getRequestId());
            model.setRequestName(request.get().getRequestName());
            model.setProjectId(request.get().getProjectId());
            if (project.isPresent()) {
                model.setProjectName(project.get().getProjectName());
            } else {
                model.setProjectName(null);
            }

            model.setRequesterId(request.get().getRequesterId());
            if (requester.isPresent()) {
                model.setRequesterName(requester.get().getUsername());
            } else {
                model.setRequesterName(null);
            }

            model.setRequestTypeId(request.get().getRequestTypeId());
            if (requestType.isPresent()) {
                model.setRequestTypeName(requestType.get().getRequestTypeName());
            } else {
                model.setRequestTypeName(null);
            }

            model.setRequestDate(request.get().getRequestDate());
            model.setRequestDesc(request.get().getRequestDesc());

            if (request.get().getVerifierId() != null) {
                model.setVerifierId(request.get().getVerifierId());
                Optional<User> verifier = userRepository.findById(request.get().getVerifierId());
                if (verifier.isPresent()) {
                    model.setVerifierName(verifier.get().getUsername());
                } else {
                    model.setVerifierName(null);
                }
            } else {
                model.setVerifierId(null);
                model.setVerifierName("");
            }

            if (detail != null) {
                model.setRequestDetailList(detail);
            } else {
                model.setRequestDetailList(null);
            }

            model.setVerifyDate(request.get().getVerifyDate());
            model.setVerifyNote(request.get().getVerifyNote());
            model.setIsVerified(request.get().getIsVerified());
            model.setIsApproved(request.get().getIsApproved());

            model.setCreatedAt(request.get().getCreatedAt());
            model.setCreatedBy(request.get().getCreatedBy());
            model.setUpdatedAt(request.get().getCreatedAt());
            model.setUpdatedBy(request.get().getUpdatedBy());
        }
        return model;
    }

    @Override
    public boolean existsById(long requestId) throws Exception {
        return requestRepository
                .existsByRequestIdAndStatusNotIn(requestId, N_D_S_STATUS_LIST);
    }
    @Override
    public Request getById(long requestId) throws Exception {
        return requestRepository
                .findByRequestIdAndStatusNotIn(requestId, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public RequestReadDTO getDTOById(long requestId) throws Exception {
        Request request = getById(requestId);

        if (request == null) 
            return null;

        return fillDTO(request);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> requestIdCollection) throws Exception {
        return requestRepository
                .existsAllByRequestIdInAndStatusNotIn(requestIdCollection, N_D_S_STATUS_LIST);
    }
    @Override
    public List<Request> getAllByIdIn(Collection<Long> requestIdCollection) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByRequestIdInAndStatusNotIn(requestIdCollection, N_D_S_STATUS_LIST);

        if (requestList.isEmpty())
            return null;

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByIdIn(Collection<Long> requestIdCollection) throws Exception {
        List<Request> requestList = getAllByIdIn(requestIdCollection);

        if (requestList == null)
            return null;

        return fillAllDTO(requestList, null);
    }

    @Override
    public List<ShowRequestModel> getByProjectId(Long projectId, int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Request> pagingResult =
                requestRepository.findAllByProjectIdAndStatusNotIn(projectId, N_D_S_STATUS_LIST, paging);

        return fillAllModel(pagingResult);
    }

    @Override
    public List<Request> getAllByProjectId(long projectId) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByProjectIdAndStatusNotIn(projectId, N_D_S_STATUS_LIST);

        if (requestList.isEmpty()) 
            return null;

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByProjectId(long projectId) throws Exception {
        List<Request> requestList = getAllByProjectId(projectId);

        if (requestList == null) 
            return null;

        return fillAllDTO(requestList, null);
    }
    @Override
    public Page<Request> getPageAllByProjectId(Pageable paging, long projectId) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByProjectIdAndStatusNotIn(projectId, N_D_S_STATUS_LIST, paging);

        if (requestPage.isEmpty())
            return null;

        return requestPage;
    }
    @Override
    public List<RequestReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception {
        Page<Request> requestPage = getPageAllByProjectId(paging, projectId);

        if (requestPage == null) 
            return null;

        List<Request> requestList = requestPage.getContent();

        if (requestList.isEmpty()) 
            return null;

        return fillAllDTO(requestList, requestPage.getTotalPages());
    }

    @Override
    public List<Request> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByProjectIdInAndStatusNotIn(projectIdCollection, N_D_S_STATUS_LIST);

        if (requestList.isEmpty()) 
            return null;

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Request> requestList = getAllByProjectIdIn(projectIdCollection);

        if (requestList == null) 
            return null;

        return fillAllDTO(requestList, null);
    }
    @Override
    public Map<Long, List<RequestReadDTO>> mapProjectIdRequestDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<RequestReadDTO> requestDTOList = getAllDTOByProjectIdIn(projectIdCollection);

        if (requestDTOList == null) 
            return new HashMap<>();

        Map<Long, List<RequestReadDTO>> projectIdRequestDTOListMap = new HashMap<>();

        long tmpProjectId;
        List<RequestReadDTO> tmpRequestDTOList;

        for (RequestReadDTO requestDTO : requestDTOList) {
            tmpProjectId = requestDTO.getProjectId();
            tmpRequestDTOList = projectIdRequestDTOListMap.get(tmpProjectId);

            if (tmpRequestDTOList == null) {
                projectIdRequestDTOListMap.put(tmpProjectId, new ArrayList<>(Collections.singletonList(requestDTO)));
            } else {
                tmpRequestDTOList.add(requestDTO);

                projectIdRequestDTOListMap.put(tmpProjectId, tmpRequestDTOList);
            }
        }

        return projectIdRequestDTOListMap;
    }
    @Override
    public Page<Request> getPageAllByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByProjectIdInAndStatusNotIn(projectIdCollection, N_D_S_STATUS_LIST, paging);

        if (requestPage.isEmpty())
            return null;

        return requestPage;
    }
    @Override
    public List<RequestReadDTO> getAllDTOInPagingByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception {
        Page<Request> requestPage = getPageAllByProjectIdIn(paging, projectIdCollection);

        if (requestPage == null) 
            return null;

        List<Request> requestList = requestPage.getContent();

        if (requestList.isEmpty()) 
            return null;

        return fillAllDTO(requestList, requestPage.getTotalPages());
    }

    @Override
    public List<Request> getAllByRequestName(String requestName) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByRequestNameAndStatusNotIn(requestName, N_D_S_STATUS_LIST);

        if (requestList.isEmpty())
            return null;

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByRequestName(String requestName) throws Exception {
        List<Request> requestList = getAllByRequestName(requestName);

        if (requestList == null)
            return null;

        return fillAllDTO(requestList, null);
    }
    @Override
    public Page<Request> getPageAllByRequestName(Pageable paging, String requestName) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByRequestNameAndStatusNotIn(requestName, N_D_S_STATUS_LIST, paging);

        if (requestPage.isEmpty())
            return null;

        return requestPage;
    }
    @Override
    public List<RequestReadDTO> getAllDTOInPagingByRequestName(Pageable paging, String requestName) throws Exception {
        Page<Request> requestPage = getPageAllByRequestName(paging, requestName);

        if (requestPage == null)
            return null;

        List<Request> requestList = requestPage.getContent();

        if (requestList.isEmpty())
            return null;

        return fillAllDTO(requestList, requestPage.getTotalPages());
    }

    @Override
    public List<Request> getAllByRequestNameContains(String requestName) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByRequestNameContainsAndStatusNotIn(requestName, N_D_S_STATUS_LIST);

        if (requestList.isEmpty()) 
            return null;

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByRequestNameContains(String requestName) throws Exception {
        List<Request> requestList = getAllByRequestNameContains(requestName);

        if (requestList == null) 
            return null;

        return fillAllDTO(requestList, null);
    }
    @Override
    public Page<Request> getPageAllByRequestNameContains(Pageable paging, String requestName) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByRequestNameContainsAndStatusNotIn(requestName, N_D_S_STATUS_LIST, paging);

        if (requestPage.isEmpty())
            return null;

        return requestPage;
    }
    @Override
    public List<RequestReadDTO> getAllDTOInPagingByRequestNameContains(Pageable paging, String requestName) throws Exception {
        Page<Request> requestPage = getPageAllByRequestNameContains(paging, requestName);

        if (requestPage == null) 
            return null;

        List<Request> requestList = requestPage.getContent();

        if (requestList.isEmpty()) 
            return null;

        return fillAllDTO(requestList, requestPage.getTotalPages());
    }

    @Override
    public List<Request> getAllByRequestTypeId(long requestTypeId) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByRequestTypeIdAndStatusNotIn(requestTypeId, N_D_S_STATUS_LIST);

        if (requestList.isEmpty()) 
            return null;

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByRequestTypeId(long requestTypeId) throws Exception {
        List<Request> requestList = getAllByRequestTypeId(requestTypeId);

        if (requestList == null) 
            return null;

        return fillAllDTO(requestList, null);
    }
    @Override
    public Page<Request> getPageAllByRequestTypeId(Pageable paging, long requestTypeId) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByRequestTypeIdAndStatusNotIn(requestTypeId, N_D_S_STATUS_LIST, paging);

        if (requestPage.isEmpty()) 
            return null;

        return requestPage;
    }
    @Override
    public List<RequestReadDTO> getAllDTOInPagingByRequestTypeId(Pageable paging, long requestTypeId) throws Exception {
        Page<Request> requestPage = getPageAllByRequestTypeId(paging, requestTypeId);

        if (requestPage == null) 
            return null;

        List<Request> requestList = requestPage.getContent();

        if (requestList.isEmpty()) 
            return null;

        return fillAllDTO(requestList, requestPage.getTotalPages());
    }

    @Override
    public List<Request> getAllByRequesterId(long requesterId) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByRequesterIdAndStatusNotIn(requesterId, N_D_S_STATUS_LIST);

        if (requestList.isEmpty()) 
            return null;

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByRequesterId(long requesterId) throws Exception {
        List<Request> requestList = getAllByRequesterId(requesterId);

        if (requestList == null) 
            return null;

        return fillAllDTO(requestList, null);
    }
    @Override
    public Page<Request> getPageAllByRequesterId(Pageable paging, long requesterId) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByRequesterIdAndStatusNotIn(requesterId, N_D_S_STATUS_LIST, paging);

        if (requestPage.isEmpty()) 
            return null;

        return requestPage;
    }
    @Override
    public List<RequestReadDTO> getAllDTOInPagingByRequesterId(Pageable paging, long requesterId) throws Exception {
        Page<Request> requestPage = getPageAllByRequesterId(paging, requesterId);

        if (requestPage == null) 
            return null;

        List<Request> requestList = requestPage.getContent();

        if (requestList.isEmpty()) 
            return null;

        return fillAllDTO(requestList, requestPage.getTotalPages());
    }

    @Override
    public List<Request> getAllByRequesterIdIn(Collection<Long> requesterIdCollection) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByRequesterIdInAndStatusNotIn(requesterIdCollection, N_D_S_STATUS_LIST);

        if (requestList.isEmpty()) 
            return null;

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByRequesterIdIn(Collection<Long> requesterIdCollection) throws Exception {
        List<Request> requestList = getAllByRequesterIdIn(requesterIdCollection);

        if (requestList == null) 
            return null;

        return fillAllDTO(requestList, null);
    }
    @Override
    public Page<Request> getPageAllByRequesterIdIn(Pageable paging, Collection<Long> requesterIdCollection) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByRequesterIdInAndStatusNotIn(requesterIdCollection, N_D_S_STATUS_LIST, paging);

        if (requestPage.isEmpty()) 
            return null;

        return requestPage;
    }
    @Override
    public List<RequestReadDTO> getAllDTOInPagingByRequesterIdIn(Pageable paging, Collection<Long> requesterIdCollection) throws Exception {
        Page<Request> requestPage = getPageAllByRequesterIdIn(paging, requesterIdCollection);

        if (requestPage == null) 
            return null;

        List<Request> requestList = requestPage.getContent();

        if (requestList.isEmpty()) 
            return null;

        return fillAllDTO(requestList, requestPage.getTotalPages());
    }

    @Override
    public List<Request> getAllByVerifierId(long verifierId) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByVerifierIdAndStatusNotIn(verifierId, N_D_S_STATUS_LIST);

        if (requestList.isEmpty()) 
            return null;

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByVerifierId(long verifierId) throws Exception {
        List<Request> requestList = getAllByVerifierId(verifierId);

        if (requestList == null) 
            return null;

        return fillAllDTO(requestList, null);
    }
    @Override
    public Page<Request> getPageAllByVerifierId(Pageable paging, long verifierId) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByVerifierIdAndStatusNotIn(verifierId, N_D_S_STATUS_LIST, paging);

        if (requestPage.isEmpty()) 
            return null;

        return requestPage;
    }
    @Override
    public List<RequestReadDTO> getAllDTOInPagingByVerifierId(Pageable paging, long verifierId) throws Exception {
        Page<Request> requestPage = getPageAllByVerifierId(paging, verifierId);

        if (requestPage == null) 
            return null;

        List<Request> requestList = requestPage.getContent();

        if (requestList.isEmpty()) 
            return null;

        return fillAllDTO(requestList, requestPage.getTotalPages());
    }

    @Override
    public List<Request> getAllByVerifierIdIn(Collection<Long> verifierIdCollection) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByVerifierIdInAndStatusNotIn(verifierIdCollection, N_D_S_STATUS_LIST);

        if (requestList.isEmpty()) 
            return null;

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByVerifierIdIn(Collection<Long> verifierIdCollection) throws Exception {
        List<Request> requestList = getAllByVerifierIdIn(verifierIdCollection);

        if (requestList == null) 
            return null;

        return fillAllDTO(requestList, null);
    }
    @Override
    public Page<Request> getPageAllByVerifierIdIn(Pageable paging, Collection<Long> verifierIdCollection) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByVerifierIdInAndStatusNotIn(verifierIdCollection, N_D_S_STATUS_LIST, paging);

        if (requestPage.isEmpty()) 
            return null;

        return requestPage;
    }
    @Override
    public List<RequestReadDTO> getAllDTOInPagingByVerifierIdIn(Pageable paging, Collection<Long> verifierIdCollection) throws Exception {
        Page<Request> requestPage = getPageAllByVerifierIdIn(paging, verifierIdCollection);

        if (requestPage == null) 
            return null;

        List<Request> requestList = requestPage.getContent();

        if (requestList.isEmpty()) 
            return null;

        return fillAllDTO(requestList, requestPage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public boolean updateRequest(UpdateRequestModel updateRequestModel) {
        Request request = requestRepository.findById(updateRequestModel.getRequestId()).orElse(null);
        Optional<Project> project = projectRepository.findById(request.getProjectId());
        Optional<User> requester = userRepository.findById(request.getRequesterId());
        Optional<RequestType> requestType = requestTypeRepository.findById(request.getRequestTypeId());
        if (request != null || project != null || requester != null || requestType != null)
        {
            request.setProjectId(updateRequestModel.getProjectId());
            request.setRequesterId(updateRequestModel.getRequesterId());
            request.setRequestName(updateRequestModel.getRequestName());
            request.setRequestTypeId(updateRequestModel.getRequestTypeId());
            request.setRequestDesc(updateRequestModel.getRequestDesc());
            request.setUpdatedAt(LocalDateTime.now());
            request.setUpdatedBy(updateRequestModel.getRequesterId());
            for (UpdateRequestDetailModel updateRequestDetailModel : updateRequestModel.getUpdateRequestDetailModels()) {
                requestDetailService.updateRequestDetail(updateRequestDetailModel);
            }
            requestRepository.saveAndFlush(request);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateVerifier(UpdateRequestVerifierModel updateRequestVerifierModel) {
        Request request = requestRepository.findById(updateRequestVerifierModel.getRequestId()).orElse(null);
        Optional<User> verifier = userRepository.findById(request.getVerifierId());
        if (request != null || verifier != null)
        {
            request.setVerifyNote(updateRequestVerifierModel.getVerifierNote());
            request.setIsVerified(updateRequestVerifierModel.getIsVerified());
            request.setVerifierId(verifier.get().getUserId());
            request.setVerifyDate(LocalDateTime.now());
            requestRepository.saveAndFlush(request);
            return true;
        }
        return false;
    }

    @Override
    public boolean approveUpdate(Long requestId, Boolean decision) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request != null){
            request.setIsApproved(decision);
            requestRepository.saveAndFlush(request);
            return true;
        }
        return false;
    }

    @Override
    public Request updateRequest(Request updatedRequest) throws Exception {
        Request oldRequest = getById(updatedRequest.getRequestId());

        if (oldRequest == null)
            return null;

        String errorMsg = "";

        /* Check FK */
        if (!oldRequest.getProjectId().equals(updatedRequest.getProjectId())) {
            if (!projectService.existsById(updatedRequest.getProjectId())) {
                errorMsg += "No Project found with Id: '" + updatedRequest.getProjectId()
                        + "'. Which violate constraint: FK_Request_Project. ";
            }
        }
        if (!oldRequest.getRequestTypeId().equals(updatedRequest.getRequestTypeId())) {
            if (!requestTypeService.existsById(updatedRequest.getRequestTypeId())) {
                errorMsg += "No RequestType found with Id: '" + updatedRequest.getRequestTypeId()
                        + "'. Which violate constraint: FK_Request_RequestType. ";
            }
        }
        if (!oldRequest.getRequesterId().equals(updatedRequest.getRequesterId())) {
            if (!userService.existsById(updatedRequest.getRequesterId())) {
                errorMsg += "No User (Requester) found with Id: '" + updatedRequest.getRequesterId()
                        + "'. Which violate constraint: FK_Request_User_RequesterId. ";
            }
        }
        if (oldRequest.getVerifierId() != null) {
            if (!oldRequest.getVerifierId().equals(updatedRequest.getVerifierId())) {
                if (!userService.existsById(updatedRequest.getVerifierId())) {
                    errorMsg += "No User (Verifier) found with Id: '" + updatedRequest.getVerifierId()
                            + "'. Which violate constraint: FK_Request_User_VerifierId. ";
                }
            }
        } else {
            if (!userService.existsById(updatedRequest.getVerifierId())) {
                errorMsg += "No User (Verifier) found with Id: '" + updatedRequest.getVerifierId()
                        + "'. Which violate constraint: FK_Request_User_VerifierId. ";
            }
        }
        if (oldRequest.getUpdatedBy() != null) {
            if (!oldRequest.getUpdatedBy().equals(updatedRequest.getUpdatedBy())) {
                if (!userService.existsById(updatedRequest.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedRequest.getUpdatedBy()
                            + "'. Which violate constraint: FK_Request_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedRequest.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedRequest.getUpdatedBy()
                        + "'. Which violate constraint: FK_Request_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (requestRepository
                .existsByProjectIdAndRequestNameAndRequestIdIsNotAndStatusNotIn(
                        updatedRequest.getProjectId(),
                        updatedRequest.getRequestName(),
                        updatedRequest.getRequestId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another Request with name: '" + updatedRequest.getRequestName()
                    + "' for Project with Id:' " +  updatedRequest.getProjectId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedRequest.setCreatedAt(oldRequest.getCreatedAt());
        updatedRequest.setCreatedBy(oldRequest.getCreatedBy());

        return requestRepository.saveAndFlush(updatedRequest);
    }
    @Override
    public RequestReadDTO updateRequestByDTO(RequestUpdateDTO updatedRequestDTO) throws Exception {
        modelMapper.typeMap(RequestUpdateDTO.class, Request.class)
                .addMappings(mapper -> {
                    mapper.skip(Request::setRequestDate);
                    mapper.skip(Request::setVerifyDate);});

        Request updatedRequest = modelMapper.map(updatedRequestDTO, Request.class);

        /* Already check NOT NULL */
        updatedRequest.setRequestDate(
                LocalDateTime.parse(updatedRequestDTO.getRequestDate(), dateTimeFormatter));

//        if (updatedRequest.getRequestDate().isAfter(LocalDateTime.now()))
//            throw new IllegalArgumentException("requestDate can't be in the future");
        
        if (updatedRequestDTO.getVerifyDate() != null) {
            updatedRequest.setVerifyDate(
                    LocalDateTime.parse(updatedRequestDTO.getRequestDate(), dateTimeFormatter));
            
//            if (updatedRequest.getVerifyDate().isAfter(LocalDateTime.now()))
//                throw new IllegalArgumentException("verifyDate can't be in the future");
            
            if (updatedRequest.getVerifyDate().isBefore(updatedRequest.getRequestDate())) 
                throw new IllegalArgumentException("verifyDate is before requestDate");
        }

        updatedRequest = updateRequest(updatedRequest);

        if (updatedRequest == null)
            return null;

        long updatedRequestId = updatedRequest.getRequestId();

        /* (If change) Update/Create associated RequestDetail; Set required FK requestId */
        List<RequestDetailUpdateDTO> requestDetailDTOList = updatedRequestDTO.getRequestDetailList();
        if (requestDetailDTOList != null) {
            requestDetailDTOList =
                    requestDetailDTOList.stream()
                            .peek(requestDetailDTO -> requestDetailDTO.setRequestId(updatedRequestId))
                            .collect(Collectors.toList());

            /* TODO: to use later when login done
            modelMapper.typeMap(RequestDetailUpdateDTO.class, RequestDetailCreateDTO.class)
                    .addMappings(mapper -> {
                        mapper.map(RequestDetailUpdateDTO::getUpdatedBy, RequestDetailCreateDTO::setCreatedBy);});*/

            List<RequestDetailCreateDTO> newRequestDetailDTOList = new ArrayList<>();
            List<RequestDetailUpdateDTO> updatedRequestDetailDTOList = new ArrayList<>();

            for (RequestDetailUpdateDTO updatedRequestDetailDTO : requestDetailDTOList) {
                if (updatedRequestDetailDTO.getRequestDetailId() <= 0) {
                    newRequestDetailDTOList.add(
                            modelMapper.map(updatedRequestDetailDTO, RequestDetailCreateDTO.class));
                } else {
                    updatedRequestDetailDTOList.add(updatedRequestDetailDTO);
                }
            }

            /* Create associated RequestDetail */
            if (!newRequestDetailDTOList.isEmpty()) {
                requestDetailService.createBulkRequestDetailByDTOList(newRequestDetailDTOList);
            }

            /* Update associated RequestDetail */
            if (!updatedRequestDetailDTOList.isEmpty()) {
                requestDetailService.updateBulkRequestDetailByDTOList(updatedRequestDetailDTOList);
            }
        }

        return fillDTO(updatedRequest);
    }

    /* DELETE */
    @Override
    public boolean deleteRequest(Long requestId) throws Exception {
        Request request = getById(requestId);

        if (request == null)
            return false;

        /* Delete all associate detail */
        requestDetailService.deleteAllByRequestId(requestId);
        /* Delete associated EntityWrapper => All EFEWPairing */
        entityWrapperService.deleteByEntityIdAndEntityType(requestId, ENTITY_TYPE);

        request.setStatus(Status.DELETED);
        requestRepository.saveAndFlush(request);

        return true;
    }

    @Override
    public boolean deleteAllByProjectId(long projectId) throws Exception {
        List<Request> requestList = getAllByProjectId(projectId);

        if (requestList == null)
            return false;

        Set<Long> requestIdSet = new HashSet<>();

        requestList = requestList.stream()
                .peek(request -> {
                    requestIdSet.add(request.getRequesterId());

                    request.setStatus(Status.DELETED);})
                .collect(Collectors.toList());

        /* Delete all associate detail */
        requestDetailService.deleteAllByRequestIdIn(requestIdSet);
        /* Delete all associated EntityWrapper => All EFEWPairing */
        entityWrapperService.deleteAllByEntityIdInAndEntityType(requestIdSet, ENTITY_TYPE);

        requestRepository.saveAllAndFlush(requestList);

        return true;
    }
    @Override
    public boolean deleteAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Request> requestList = getAllByProjectIdIn(projectIdCollection);

        if (requestList == null)
            return false;

        Set<Long> requestIdSet = new HashSet<>();

        requestList = requestList.stream()
                .peek(request -> {
                    requestIdSet.add(request.getRequesterId());

                    request.setStatus(Status.DELETED);})
                .collect(Collectors.toList());

        /* Delete all associate detail */
        requestDetailService.deleteAllByRequestIdIn(requestIdSet);
        /* Delete all associated EntityWrapper => All EFEWPairing */
        entityWrapperService.deleteAllByEntityIdInAndEntityType(requestIdSet, ENTITY_TYPE);

        requestRepository.saveAllAndFlush(requestList);

        return true;
    }

    @Override
    public boolean deleteAllByUserId(long userId) throws Exception {
        Set<Request> requestSet = new HashSet<>();

        List<Request> requestList = getAllByRequesterId(userId);
        if (requestList != null){
            requestSet.addAll(requestList);
        }

        requestList = getAllByVerifierId(userId);
        if (requestList != null){
            requestSet.addAll(requestList);
        }

        if (requestSet.isEmpty())
            return false;

        Set<Long> requestIdSet = new HashSet<>();

        requestSet = requestSet.stream()
                .peek(request -> {
                    requestIdSet.add(request.getRequesterId());

                    request.setStatus(Status.DELETED);})
                .collect(Collectors.toSet());

        /* Delete all associate detail */
        requestDetailService.deleteAllByRequestIdIn(requestIdSet);
        /* Delete all associated EntityWrapper => All EFEWPairing */
        entityWrapperService.deleteAllByEntityIdInAndEntityType(requestIdSet, ENTITY_TYPE);

        requestRepository.saveAllAndFlush(requestSet);

        return true;
    }
    @Override
    public boolean deleteAllByUserIdIn(Collection<Long> userIdCollection) throws Exception {
        Set<Request> requestSet = new HashSet<>();

        List<Request> requestList = getAllByRequesterIdIn(userIdCollection);
        if (requestList != null){
            requestSet.addAll(requestList);
        }

        requestList = getAllByVerifierIdIn(userIdCollection);
        if (requestList != null){
            requestSet.addAll(requestList);
        }

        if (requestSet.isEmpty())
            return false;

        Set<Long> requestIdSet = new HashSet<>();

        requestSet = requestSet.stream()
                .peek(request -> {
                    requestIdSet.add(request.getRequesterId());

                    request.setStatus(Status.DELETED);})
                .collect(Collectors.toSet());

        /* Delete all associate detail */
        requestDetailService.deleteAllByRequestIdIn(requestIdSet);
        /* Delete all associated EntityWrapper => All EFEWPairing */
        entityWrapperService.deleteAllByEntityIdInAndEntityType(requestIdSet, ENTITY_TYPE);

        requestRepository.saveAllAndFlush(requestSet);

        return true;
    }

    /* Utils */
    private RequestReadDTO fillDTO(Request request) throws Exception{
        long requestId = request.getRequestId();

        RequestReadDTO requestDTO = modelMapper.map(request, RequestReadDTO.class);

        /* Get associated RequestType */
        requestDTO.setRequestType(
                requestTypeService.getDTOById(request.getRequestTypeId()));

        /* Get associated Requester */
        requestDTO.setRequester(
                userService.getDTOById(request.getRequesterId()));

        /* Get associated Verifier */
        if (request.getVerifierId() != null) {
            requestDTO.setVerifier(
                    userService.getDTOById(request.getVerifierId()));
        }

        /* Get associated RequestDetail */
        requestDTO.setRequestDetailList(
                requestDetailService.getAllDTOByRequestId(requestId));
        /* Get associated ExternalFile */
//        requestDTO.setFileList(
//                eFEWPairingService
//                        .getAllExternalFileDTOByEntityIdAndEntityType(requestId, ENTITY_TYPE));

        return requestDTO;
    }

    private List<RequestReadDTO> fillAllDTO(Collection<Request> requestCollection, Integer totalPage) throws Exception{
        Set<Long> requestTypeIdSet = new HashSet<>();
        Set<Long> userIdSet = new HashSet<>();
        Set<Long> requestIdSet = new HashSet<>();

        for (Request request : requestCollection) {
            requestTypeIdSet.add(request.getRequestTypeId());
            userIdSet.add(request.getRequesterId());
            if (request.getVerifierId() != null)
                userIdSet.add(request.getVerifierId());

            requestIdSet.add(request.getRequestId());
        }

        /* Get associated RequestType */
        Map<Long, RequestTypeReadDTO> requestTypeIdRequestTypeDTOMap =
                requestTypeService.mapRequestTypeIdRequestTypeDTOByIdIn(requestTypeIdSet);

        /* Get associated Requester & Verifier */
        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(userIdSet);

        /* Get associated RequestDetail */
        Map<Long, List<RequestDetailReadDTO>> requestIdRequestDetailDTOListMap =
                requestDetailService.mapRequestIdRequestDetailDTOListByRequestIdIn(requestIdSet);
        /* Get associated ExternalFile */
//        Map<Long, List<ExternalFileReadDTO>> requestIdExternalFileDTOListMap =
//                eFEWPairingService
//                        .mapEntityIdExternalFileDTOListByEntityIdInAndEntityType(requestIdSet, ENTITY_TYPE);

        return requestCollection.stream()
                .map(request -> {
                    RequestReadDTO requestDTO =
                            modelMapper.map(request, RequestReadDTO.class);

                    long tmpRequestId = request.getRequestId();

                    requestDTO.setRequestType(requestTypeIdRequestTypeDTOMap.get(request.getRequestTypeId()));
                    requestDTO.setRequester(userIdUserDTOMap.get(request.getRequesterId()));

                    if (request.getVerifierId() != null)
                        requestDTO.setVerifier(userIdUserDTOMap.get(request.getVerifierId()));

                    requestDTO.setRequestDetailList(requestIdRequestDetailDTOListMap.get(tmpRequestId));
//                    requestDTO.setFileList(
//                            requestIdExternalFileDTOListMap.get(tmpRequestId));

                    requestDTO.setTotalPage(totalPage);

                    return requestDTO;})
                .collect(Collectors.toList());
    }

    private List<ShowRequestModel> fillAllModel(Page<Request> pagingResult) {
        if (pagingResult.hasContent()){
            int totalPage = pagingResult.getTotalPages();

            Page<ShowRequestModel> modelResult = pagingResult.map(new Converter<Request, ShowRequestModel>() {

                @Override
                protected ShowRequestModel doForward(Request request) {
                    ShowRequestModel model = new ShowRequestModel();
                    Optional<Project> project = projectRepository.findById(request.getProjectId());
                    Optional<User> requester = userRepository.findById(request.getRequesterId());
                    List<RequestDetail> detail =
                            requestDetailRepository.findAllByRequestIdAndStatusNotIn(request.getRequestId(), N_D_S_STATUS_LIST);

                    Optional<RequestType> requestType = requestTypeRepository.findById(request.getRequestTypeId());

                    model.setRequestId(request.getRequestId());
                    model.setRequestName(request.getRequestName());
                    model.setProjectId(request.getProjectId());
                    if (project.isPresent()){
                        model.setProjectName(project.get().getProjectName());
                    }else{
                        model.setProjectName(null);
                    }

                    model.setRequesterId(request.getRequesterId());
                    if (requester.isPresent()){
                        model.setRequesterName(requester.get().getUsername());
                    }else{
                        model.setRequesterName(null);
                    }

                    model.setRequestTypeId(request.getRequestTypeId());
                    if (requestType.isPresent()){
                        model.setRequestTypeName(requestType.get().getRequestTypeName());
                    }else{
                        model.setRequestTypeName(null);
                    }

                    model.setRequestDate(request.getRequestDate());
                    model.setRequestDesc(request.getRequestDesc());

                    if (request.getVerifierId() !=null)
                    {
                        model.setVerifierId(request.getVerifierId());
                        Optional<User> verifier = userRepository.findById(request.getVerifierId());
                        if (verifier.isPresent()){
                            model.setVerifierName(verifier.get().getUsername());
                        }else{
                            model.setVerifierName(null);
                        }
                    }else{
                        model.setVerifierId(null);
                        model.setVerifierName("");
                    }

                    if (detail != null)
                    {
                        model.setRequestDetailList(detail);
                    }else{
                        model.setRequestDetailList(null);
                    }

                    model.setVerifyDate(request.getVerifyDate());
                    model.setVerifyNote(request.getVerifyNote());
                    model.setIsVerified(request.getIsVerified());
                    model.setIsApproved(request.getIsApproved());

                    model.setCreatedAt(request.getCreatedAt());
                    model.setCreatedBy(request.getCreatedBy());
                    model.setUpdatedAt(request.getCreatedAt());
                    model.setUpdatedBy(request.getUpdatedBy());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Request doBackward(ShowRequestModel showRequestModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ShowRequestModel>();
        }
    }
}
