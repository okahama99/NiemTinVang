package com.ntv.ntvcons_backend.services.request;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.Request;
import com.ntv.ntvcons_backend.entities.RequestModels.CreateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.ShowRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestModel;
import com.ntv.ntvcons_backend.entities.RequestModels.UpdateRequestVerifierModel;
import com.ntv.ntvcons_backend.entities.RequestType;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.repositories.ProjectRepository;
import com.ntv.ntvcons_backend.repositories.RequestRepository;
import com.ntv.ntvcons_backend.repositories.RequestTypeRepository;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService{

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RequestTypeRepository requestTypeRepository;

    @Autowired
    private DateTimeFormatter dateTimeFormatter;

    @Override
    public boolean createRequest(CreateRequestModel createRequestModel) {
        Request request = new Request();
        request.setProjectId(createRequestModel.getProjectId());
        request.setRequesterId(createRequestModel.getRequesterId());
        request.setRequestTypeId(createRequestModel.getRequestTypeId());
        request.setRequestDesc(createRequestModel.getRequestDesc());
        request.setCreatedAt(LocalDateTime.parse(createRequestModel.getRequestDate(),dateTimeFormatter));
        request.setCreatedBy(createRequestModel.getRequesterId());
        request.setRequestDate(LocalDateTime.parse(createRequestModel.getRequestDate(),dateTimeFormatter));
        requestRepository.saveAndFlush(request);
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

                            Optional<RequestType> requestType = requestTypeRepository.findById(request.getRequestTypeId());

                            model.setRequestId(request.getRequestId());
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
    public boolean updateRequest(UpdateRequestModel updateRequestModel) {
        Request request = requestRepository.findById(updateRequestModel.getRequestId()).orElse(null);
        Optional<Project> project = projectRepository.findById(request.getProjectId());
        Optional<User> requester = userRepository.findById(request.getRequesterId());
        Optional<RequestType> requestType = requestTypeRepository.findById(request.getRequestTypeId());
        if(request != null || project != null || requester != null || requestType != null)
        {
            request.setProjectId(updateRequestModel.getProjectId());
            request.setRequesterId(updateRequestModel.getRequesterId());
            request.setRequestTypeId(updateRequestModel.getRequestTypeId());
            request.setRequestDesc(updateRequestModel.getRequestDesc());
            request.setUpdatedAt(LocalDateTime.now());
            request.setUpdatedBy(updateRequestModel.getRequesterId());
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
