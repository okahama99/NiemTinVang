package com.ntv.ntvcons_backend.services.request;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.dtos.request.RequestReadDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeReadDTO;
import com.ntv.ntvcons_backend.entities.*;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.RequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.UpdateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestModels.CreateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.ShowRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestVerifierModel;
import com.ntv.ntvcons_backend.repositories.*;
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

import java.time.*;
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
    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RequestTypeRepository requestTypeRepository;
    @Autowired
    private RequestTypeService requestTypeService;
    @Autowired
    RequestDetailService requestDetailService;
    @Autowired
    RequestDetailRepository requestDetailRepository;

    @Override
    public boolean createRequest(CreateRequestModel createRequestModel) {
        Request request = new Request();
        request.setProjectId(createRequestModel.getProjectId());
        request.setRequesterId(createRequestModel.getRequesterId());
        request.setRequestTypeId(createRequestModel.getRequestTypeId());
        request.setRequestDesc(createRequestModel.getRequestDesc());
        request.setRequestName(createRequestModel.getRequestName());

        request.setCreatedAt(LocalDateTime.parse(createRequestModel.getRequestDate(),dateTimeFormatter));

        request.setCreatedBy(createRequestModel.getRequesterId());
        request.setRequestDate(LocalDateTime.parse(createRequestModel.getRequestDate(),dateTimeFormatter));
        requestRepository.saveAndFlush(request);
        if(createRequestModel.getModelList() != null)
        {
            for (RequestDetailModel requestDetailModel : createRequestModel.getModelList()) {
                CreateRequestDetailModel createRequestDetailModel = new CreateRequestDetailModel();
                createRequestDetailModel.setRequestId(request.getRequestId());
                createRequestDetailModel.setItemDesc(requestDetailModel.getItemDesc());
                createRequestDetailModel.setItemAmount(requestDetailModel.getItemAmount());
                createRequestDetailModel.setItemPrice(requestDetailModel.getItemPrice());
                createRequestDetailModel.setItemUnit(requestDetailModel.getItemUnit());
                requestDetailService.createRequest(createRequestDetailModel);
            }
        }
        return true;
    }

    @Override
    public List<ShowRequestModel> getAllAvailableRequest(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Request> pagingResult = requestRepository.findAllByIsDeletedIsFalse(paging);

        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);

            Page<ShowRequestModel> modelResult = pagingResult.map(new Converter<Request, ShowRequestModel>() {

                        @Override
                        protected ShowRequestModel doForward(Request request) {
                            ShowRequestModel model = new ShowRequestModel();
                            Optional<Project> project = projectRepository.findById(request.getProjectId());
                            Optional<User> requester = userRepository.findById(request.getRequesterId());
                            List<RequestDetail> detail = requestDetailRepository.findAllByRequestIdAndIsDeletedIsFalse(request.getRequestId());

                            Optional<RequestType> requestType = requestTypeRepository.findById(request.getRequestTypeId());

                            model.setRequestId(request.getRequestId());
                            model.setRequestName(request.getRequestName());
                            model.setProjectId(request.getProjectId());
                            if(project.isPresent()){
                                model.setProjectName(project.get().getProjectName());
                            }else{
                                model.setProjectName(null);
                            }

                            model.setRequesterId(request.getRequesterId());
                            if(requester.isPresent()){
                                model.setRequesterName(requester.get().getUsername());
                            }else{
                                model.setRequesterName(null);
                            }

                            model.setRequestTypeId(request.getRequestTypeId());
                            if(requestType.isPresent()){
                                model.setRequestTypeName(requestType.get().getRequestTypeName());
                            }else{
                                model.setRequestTypeName(null);
                            }

                            model.setRequestDate(request.getRequestDate());
                            model.setRequestDesc(request.getRequestDesc());

                            if(request.getVerifierId() !=null)
                            {
                                model.setVerifierId(request.getVerifierId());
                                Optional<User> verifier = userRepository.findById(request.getVerifierId());
                                if(verifier.isPresent()){
                                    model.setVerifierName(verifier.get().getUsername());
                                }else{
                                    model.setVerifierName(null);
                                }
                            }else{
                                model.setVerifierId(null);
                                model.setVerifierName("");
                            }

                            if(detail != null)
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

    @Override
    public List<ShowRequestModel> getByProjectId(Long projectId, int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Request> pagingResult = requestRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId,paging);

        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);

            Page<ShowRequestModel> modelResult = pagingResult.map(new Converter<Request, ShowRequestModel>() {

                @Override
                protected ShowRequestModel doForward(Request request) {
                    ShowRequestModel model = new ShowRequestModel();
                    Optional<Project> project = projectRepository.findById(request.getProjectId());
                    Optional<User> requester = userRepository.findById(request.getRequesterId());
                    List<RequestDetail> detail = requestDetailRepository.findAllByRequestIdAndIsDeletedIsFalse(request.getRequestId());

                    Optional<RequestType> requestType = requestTypeRepository.findById(request.getRequestTypeId());

                    model.setRequestId(request.getRequestId());
                    model.setRequestName(request.getRequestName());
                    model.setProjectId(request.getProjectId());
                    if(project.isPresent()){
                        model.setProjectName(project.get().getProjectName());
                    }else{
                        model.setProjectName(null);
                    }

                    model.setRequesterId(request.getRequesterId());
                    if(requester.isPresent()){
                        model.setRequesterName(requester.get().getUsername());
                    }else{
                        model.setRequesterName(null);
                    }

                    model.setRequestTypeId(request.getRequestTypeId());
                    if(requestType.isPresent()){
                        model.setRequestTypeName(requestType.get().getRequestTypeName());
                    }else{
                        model.setRequestTypeName(null);
                    }

                    model.setRequestDate(request.getRequestDate());
                    model.setRequestDesc(request.getRequestDesc());

                    if(request.getVerifierId() !=null)
                    {
                        model.setVerifierId(request.getVerifierId());
                        Optional<User> verifier = userRepository.findById(request.getVerifierId());
                        if(verifier.isPresent()){
                            model.setVerifierName(verifier.get().getUsername());
                        }else{
                            model.setVerifierName(null);
                        }
                    }else{
                        model.setVerifierId(null);
                        model.setVerifierName("");
                    }

                    if(detail != null)
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

    @Override
    public List<Request> getAllByProjectId(long projectId) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId);

        if (requestList.isEmpty()) {
            return null;
        }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByProjectId(long projectId) throws Exception {
        List<Request> requestList = getAllByProjectId(projectId);

        if (requestList == null) {
            return null;
        }

        Set<Long> requestIdSet = new HashSet<>();
        Set<Long> requestTypeIdSet = new HashSet<>();

        for (Request request : requestList) {
            requestIdSet.add(request.getRequestId());
            requestTypeIdSet.add(request.getRequestTypeId());
        }

        /* Get associated RequestType */
        Map<Long, RequestTypeReadDTO> requestTypeIdRequestTypeDTOMap =
                requestTypeService.mapRequestTypeIdRequestTypeDTOByIdIn(requestTypeIdSet);

        /* Get associated RequestDetail */
        Map<Long, List<RequestDetailReadDTO>> requestIdRequestDetailDTOListMap =
                requestDetailService.mapRequestIdRequestDetailDTOListByRequestIdIn(requestIdSet);

        return requestList.stream()
                .map(request -> {
                    RequestReadDTO requestDTO =
                            modelMapper.map(request, RequestReadDTO.class);

                    requestDTO.setRequestType(requestTypeIdRequestTypeDTOMap.get(request.getRequestTypeId()));

                    requestDTO.setRequestDetailList(requestIdRequestDetailDTOListMap.get(request.getRequestId()));

                    return requestDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<Request> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByProjectIdInAndIsDeletedIsFalse(projectIdCollection);

        if (requestList.isEmpty()) {
            return null;
        }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<Request> requestList = getAllByProjectIdIn(projectIdCollection);

        if (requestList == null) {
            return null;
        }

        Set<Long> requestIdSet = new HashSet<>();
        Set<Long> requestTypeIdSet = new HashSet<>();

        for (Request request : requestList) {
            requestIdSet.add(request.getRequestId());
            requestTypeIdSet.add(request.getRequestTypeId());
        }

        /* Get associated RequestType */
        Map<Long, RequestTypeReadDTO> requestTypeIdRequestTypeDTOMap =
                requestTypeService.mapRequestTypeIdRequestTypeDTOByIdIn(requestTypeIdSet);

        /* Get associated RequestDetail */
        Map<Long, List<RequestDetailReadDTO>> requestIdRequestDetailDTOListMap =
                requestDetailService.mapRequestIdRequestDetailDTOListByRequestIdIn(requestIdSet);

        return requestList.stream()
                .map(request -> {
                    RequestReadDTO requestDTO =
                            modelMapper.map(request, RequestReadDTO.class);

                    requestDTO.setRequestType(requestTypeIdRequestTypeDTOMap.get(request.getRequestTypeId()));

                    requestDTO.setRequestDetailList(requestIdRequestDetailDTOListMap.get(request.getRequestId()));

                    return requestDTO;})
                .collect(Collectors.toList());
    }
    @Override
    public Map<Long, List<RequestReadDTO>> mapProjectIdRequestDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<RequestReadDTO> requestDTOList = getAllDTOByProjectIdIn(projectIdCollection);

        if (requestDTOList == null) {
            return new HashMap<>();
        }

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
    public ShowRequestModel getByRequestId(Long requestId) {
        Optional<Request> request = requestRepository.findByRequestIdAndIsDeletedIsFalse(requestId);
        ShowRequestModel model = new ShowRequestModel();
        if(request!=null) {
            Optional<Project> project = projectRepository.findById(request.get().getProjectId());
            Optional<User> requester = userRepository.findById(request.get().getRequesterId());
            List<RequestDetail> detail = requestDetailRepository.findAllByRequestIdAndIsDeletedIsFalse(request.get().getRequestId());

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
    public boolean updateRequest(UpdateRequestModel updateRequestModel) {
        Request request = requestRepository.findById(updateRequestModel.getRequestId()).orElse(null);
        Optional<Project> project = projectRepository.findById(request.getProjectId());
        Optional<User> requester = userRepository.findById(request.getRequesterId());
        Optional<RequestType> requestType = requestTypeRepository.findById(request.getRequestTypeId());
        if(request != null || project != null || requester != null || requestType != null)
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
        if(request != null || verifier != null)
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
        if(request != null){
            request.setIsApproved(decision);
            requestRepository.saveAndFlush(request);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRequest(Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if(request != null){
            request.setIsDeleted(true);
            requestRepository.saveAndFlush(request);
            return true;
        }
        return false;
    }

}
