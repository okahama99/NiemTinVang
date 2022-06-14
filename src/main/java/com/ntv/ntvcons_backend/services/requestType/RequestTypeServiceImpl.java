package com.ntv.ntvcons_backend.services.requestType;

import com.ntv.ntvcons_backend.dtos.requestType.*;
import com.ntv.ntvcons_backend.entities.RequestType;
import com.ntv.ntvcons_backend.repositories.RequestTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestTypeServiceImpl implements RequestTypeService {
    @Autowired
    private RequestTypeRepository requestTypeRepository;
    @Autowired
    private ModelMapper modelMapper;

    /* CREATE */
    @Override
    public RequestType createRequestType(RequestType newRequestType) throws Exception {
        return requestTypeRepository.saveAndFlush(newRequestType);
    }
    @Override
    public RequestTypeReadDTO createRequestTypeByDTO(RequestTypeCreateDTO newRequestTypeDTO) throws Exception {
        RequestType newRequestType = modelMapper.map(newRequestTypeDTO, RequestType.class);

        newRequestType = createRequestType(newRequestType);

        return modelMapper.map(newRequestType, RequestTypeReadDTO.class);
    }

    /* READ */
    @Override
    public List<RequestType> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<RequestType> requestTypePage = requestTypeRepository.findAllByIsDeletedIsFalse(paging);

        if (requestTypePage.isEmpty()) {
            return null;
        }

        return requestTypePage.getContent();
    }
    @Override
    public List<RequestTypeReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        List<RequestType> requestTypeList = getAll(pageNo, pageSize, sortBy, sortType);

        if (requestTypeList != null && !requestTypeList.isEmpty()) {
            int totalPage = (int) Math.ceil((double) requestTypeList.size() / pageSize);

            return requestTypeList.stream()
                    .map(requestType -> {
                        RequestTypeReadDTO requestTypeReadDTO =
                                modelMapper.map(requestType, RequestTypeReadDTO.class);
                        requestTypeReadDTO.setTotalPage(totalPage);
                        return requestTypeReadDTO;})
                    .collect(Collectors.toList());

        } else {
            return null;
        }
    }

    @Override
    public RequestType getById(long requestTypeId) throws Exception {
        Optional<RequestType> requestType =
                requestTypeRepository.findByRequestTypeIdAndIsDeletedIsFalse(requestTypeId);

        return requestType.orElse(null);
    }
    @Override
    public RequestTypeReadDTO getDTOById(long requestTypeId) throws Exception {
        RequestType requestType = getById(requestTypeId);

        if (requestType == null) {
            return null;
        }

        return modelMapper.map(requestType, RequestTypeReadDTO.class);
    }

    @Override
    public List<RequestType> getAllByIdIn(Collection<Long> requestTypeIdCollection) throws Exception {
        List<RequestType> requestTypeList =
                requestTypeRepository.findAllByRequestTypeIdInAndIsDeletedIsFalse(requestTypeIdCollection);

        if (requestTypeList.isEmpty()) {
            return null;
        }

        return requestTypeList;
    }
    @Override
    public List<RequestTypeReadDTO> getAllDTOByIdIn(Collection<Long> requestTypeIdCollection) throws Exception {
        List<RequestType> requestTypeList = getAllByIdIn(requestTypeIdCollection);

        if (requestTypeList == null) {
            return null;
        }

        return requestTypeList.stream()
                .map(requestType -> modelMapper.map(requestType, RequestTypeReadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestType> getAllByRequestTypeNameContains(String requestTypeName) throws Exception {
        List<RequestType> requestTypeList =
                requestTypeRepository.findAllByRequestTypeNameContainsAndIsDeletedIsFalse(requestTypeName);

        if (requestTypeList.isEmpty()) {
            return null;
        }

        return requestTypeList;
    }
    @Override
    public List<RequestTypeReadDTO> getAllDTOByRequestTypeNameContains(String requestTypeName) throws Exception {
        List<RequestType> requestTypeList = getAllByRequestTypeNameContains(requestTypeName);

        if (requestTypeList == null) {
            return null;
        }

        return requestTypeList.stream()
                .map(requestType -> modelMapper.map(requestType, RequestTypeReadDTO.class))
                .collect(Collectors.toList());
    }

    /* UPDATE */
    @Override
    public RequestType updateRequestType(RequestType updatedRequestType) throws Exception {
        Optional<RequestType> requestType =
                requestTypeRepository.findByRequestTypeIdAndIsDeletedIsFalse(updatedRequestType.getRequestTypeId());

        if (!requestType.isPresent()) {
            return null;
            /* Not found by Id, return null */
        }

        return requestTypeRepository.saveAndFlush(updatedRequestType);
    }
    @Override
    public RequestTypeReadDTO updateRequestTypeByDTO(RequestTypeUpdateDTO updatedRequestTypeDTO) throws Exception {
        RequestType updatedRequestType = modelMapper.map(updatedRequestTypeDTO, RequestType.class);

        updatedRequestType = updateRequestType(updatedRequestType);

        if (updatedRequestType == null) {
            return null;
        }

        return modelMapper.map(updatedRequestType, RequestTypeReadDTO.class);
    }

    /* DELETE */
    @Override
    public boolean deleteRequestType(long requestTypeId) throws Exception {
        Optional<RequestType> requestType =
                requestTypeRepository.findByRequestTypeIdAndIsDeletedIsFalse(requestTypeId);

        if (!requestType.isPresent()) {
            return false;
            /* Not found with Id */
        }

        requestType.get().setIsDeleted(true);

        requestTypeRepository.saveAndFlush(requestType.get());

        return true;
    }
}
