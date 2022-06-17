package com.ntv.ntvcons_backend.services.requestType;

import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeUpdateDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        String errorMsg = "";

        /* Check duplicate */
        if (requestTypeRepository.existsByRequestTypeNameAndIsDeletedIsFalse(newRequestType.getRequestTypeName())) {
            errorMsg += "Already exists another RequestType with name: " + newRequestType.getRequestTypeName() + "\n";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

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

        } 
            
        return null;
    }

    @Override
    public boolean existsById(long requestTypeId) throws Exception {
        return requestTypeRepository.existsByRequestTypeIdAndIsDeletedIsFalse(requestTypeId);
    }
    @Override
    public RequestType getById(long requestTypeId) throws Exception {
        return requestTypeRepository
                .findByRequestTypeIdAndIsDeletedIsFalse(requestTypeId)
                .orElse(null);
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
    public boolean existsAllByIdIn(Collection<Long> requestTypeIdCollection) throws Exception {
        return requestTypeRepository.existsAllByRequestTypeIdInAndIsDeletedIsFalse(requestTypeIdCollection);
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
    public Map<Long, RequestType> mapRequestTypeIdRequestTypeByIdIn(Collection<Long> requestTypeIdCollection) throws Exception {
        List<RequestType> requestTypeList = getAllByIdIn(requestTypeIdCollection);

        if (requestTypeList == null) {
            return new HashMap<>();
        }

        return requestTypeList.stream()
                .collect(Collectors.toMap(RequestType::getRequestTypeId, Function.identity()));
    }
    @Override
    public Map<Long, RequestTypeReadDTO> mapRequestTypeIdRequestTypeDTOByIdIn(Collection<Long> requestTypeIdCollection) throws Exception {
        List<RequestTypeReadDTO> requestTypeDTOList = getAllDTOByIdIn(requestTypeIdCollection);

        if (requestTypeDTOList == null) {
            return new HashMap<>();
        }

        return requestTypeDTOList.stream()
                .collect(Collectors.toMap(RequestTypeReadDTO::getRequestTypeId, Function.identity()));
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
        RequestType requestType = getById(updatedRequestType.getRequestTypeId());

        if (requestType == null) {
            return null;
            /* Not found by Id, return null */
        }

        String errorMsg = "";

        /* Check duplicate */
        if (requestTypeRepository
                .existsByRequestTypeNameAndRequestTypeIdIsNotAndIsDeletedIsFalse(
                        updatedRequestType.getRequestTypeName(),
                        updatedRequestType.getRequestTypeId())) {
            errorMsg += "Already exists another RequestType with name: " + updatedRequestType.getRequestTypeName() + "\n";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
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
        RequestType requestType = getById(requestTypeId);

        if (requestType == null) {
            return false;
            /* Not found with Id */
        }

        requestType.setIsDeleted(true);
        requestTypeRepository.saveAndFlush(requestType);

        return true;
    }
}