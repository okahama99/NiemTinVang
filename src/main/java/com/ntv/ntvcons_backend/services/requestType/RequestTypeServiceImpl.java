package com.ntv.ntvcons_backend.services.requestType;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.RequestType;
import com.ntv.ntvcons_backend.repositories.RequestTypeRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public RequestType createRequestType(RequestType newRequestType) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!userService.existsById(newRequestType.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newRequestType.getCreatedBy()
                    + "'. Which violate constraint: FK_RequestType_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (requestTypeRepository
                .existsByRequestTypeNameAndStatusNotIn(
                        newRequestType.getRequestTypeName(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another RequestType with name: '"
                    + newRequestType.getRequestTypeName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return requestTypeRepository.saveAndFlush(newRequestType);
    }
    @Override
    public RequestTypeReadDTO createRequestTypeByDTO(RequestTypeCreateDTO newRequestTypeDTO) throws Exception {
        RequestType newRequestType = modelMapper.map(newRequestTypeDTO, RequestType.class);

        newRequestType = createRequestType(newRequestType);

        return fillDTO(newRequestType);
    }

    /* READ */
    @Override
    public Page<RequestType> getPageAll(Pageable paging) throws Exception {
        Page<RequestType> requestTypePage =
                requestTypeRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (requestTypePage.isEmpty()) 
            return null;

        return requestTypePage;
    }
    @Override
    public List<RequestTypeReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<RequestType> requestTypePage = getPageAll(paging);

        if (requestTypePage == null)
            return null;

        List<RequestType> requestTypeList = requestTypePage.getContent();

        if (requestTypeList.isEmpty())
            return null;

        return fillAllDTO(requestTypeList, requestTypePage.getTotalPages());
    }

    @Override
    public boolean existsById(long requestTypeId) throws Exception {
        return requestTypeRepository
                .existsByRequestTypeIdAndStatusNotIn(requestTypeId, N_D_S_STATUS_LIST);
    }
    @Override
    public RequestType getById(long requestTypeId) throws Exception {
        return requestTypeRepository
                .findByRequestTypeIdAndStatusNotIn(requestTypeId, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public RequestTypeReadDTO getDTOById(long requestTypeId) throws Exception {
        RequestType requestType = getById(requestTypeId);

        if (requestType == null) 
            return null;

        return fillDTO(requestType);
    }

    @Override
    public List<RequestType> getAllByIdIn(Collection<Long> requestTypeIdCollection) throws Exception {
        List<RequestType> requestTypeList =
                requestTypeRepository.findAllByRequestTypeIdInAndStatusNotIn(requestTypeIdCollection, N_D_S_STATUS_LIST);

        if (requestTypeList.isEmpty()) 
            return null;

        return requestTypeList;
    }
    @Override
    public List<RequestTypeReadDTO> getAllDTOByIdIn(Collection<Long> requestTypeIdCollection) throws Exception {
        List<RequestType> requestTypeList = getAllByIdIn(requestTypeIdCollection);

        if (requestTypeList == null) 
            return null;

        return fillAllDTO(requestTypeList, null);
    }
    @Override
    public Map<Long, RequestTypeReadDTO> mapRequestTypeIdRequestTypeDTOByIdIn(Collection<Long> requestTypeIdCollection) throws Exception {
        List<RequestTypeReadDTO> requestTypeDTOList = getAllDTOByIdIn(requestTypeIdCollection);

        if (requestTypeDTOList == null) 
            return new HashMap<>();

        return requestTypeDTOList.stream()
                .collect(Collectors.toMap(RequestTypeReadDTO::getRequestTypeId, Function.identity()));
    }

    @Override
    public RequestType getByRequestTypeName(String requestTypeName) throws Exception {
        return requestTypeRepository
                .findByRequestTypeNameAndStatusNotIn(requestTypeName, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public RequestTypeReadDTO getDTOByRequestTypeName(String requestTypeName) throws Exception {
        RequestType requestType = getByRequestTypeName(requestTypeName);

        if (requestType == null)
            return null;

        return fillDTO(requestType);
    }

    @Override
    public List<RequestType> getAllByRequestTypeNameContains(String requestTypeName) throws Exception {
        List<RequestType> requestTypeList =
                requestTypeRepository.findAllByRequestTypeNameContainsAndStatusNotIn(requestTypeName, N_D_S_STATUS_LIST);

        if (requestTypeList.isEmpty())
            return null;

        return requestTypeList;
    }
    @Override
    public List<RequestTypeReadDTO> getAllDTOByRequestTypeNameContains(String requestTypeName) throws Exception {
        List<RequestType> requestTypeList = getAllByRequestTypeNameContains(requestTypeName);

        if (requestTypeList == null)
            return null;

        return fillAllDTO(requestTypeList, null);
    }
    @Override
    public Page<RequestType> getPageAllByRequestTypeNameContains(Pageable paging, String requestTypeName) throws Exception {
        Page<RequestType> requestTypePage =
                requestTypeRepository
                        .findAllByRequestTypeNameContainsAndStatusNotIn(requestTypeName, N_D_S_STATUS_LIST, paging);

        if (requestTypePage.isEmpty())
            return null;

        return requestTypePage;
    }
    @Override
    public List<RequestTypeReadDTO> getAllDTOInPagingByRequestTypeNameContains(Pageable paging, String requestTypeName) throws Exception {
        Page<RequestType> requestTypePage = getPageAllByRequestTypeNameContains(paging, requestTypeName);

        if (requestTypePage == null)
            return null;

        List<RequestType> requestTypeList = requestTypePage.getContent();

        if (requestTypeList.isEmpty())
            return null;

        return fillAllDTO(requestTypeList, requestTypePage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public RequestType updateRequestType(RequestType updatedRequestType) throws Exception {
        RequestType oldRequestType = getById(updatedRequestType.getRequestTypeId());

        if (oldRequestType == null)
            return null; /* Not found by Id, return null */

        String errorMsg = "";

        /* Check FK */
        if (oldRequestType.getUpdatedBy() != null) {
            if (!oldRequestType.getUpdatedBy().equals(updatedRequestType.getUpdatedBy())) {
                if (!userService.existsById(updatedRequestType.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedRequestType.getUpdatedBy()
                            + "'. Which violate constraint: FK_RequestType_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedRequestType.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedRequestType.getUpdatedBy()
                        + "'. Which violate constraint: FK_RequestType_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (requestTypeRepository
                .existsByRequestTypeNameAndRequestTypeIdIsNotAndStatusNotIn(
                        updatedRequestType.getRequestTypeName(),
                        updatedRequestType.getRequestTypeId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another RequestType with name: '" 
                    + updatedRequestType.getRequestTypeName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        updatedRequestType.setCreatedAt(oldRequestType.getCreatedAt());
        updatedRequestType.setCreatedBy(oldRequestType.getCreatedBy());

        return requestTypeRepository.saveAndFlush(updatedRequestType);
    }
    @Override
    public RequestTypeReadDTO updateRequestTypeByDTO(RequestTypeUpdateDTO updatedRequestTypeDTO) throws Exception {
        RequestType updatedRequestType = modelMapper.map(updatedRequestTypeDTO, RequestType.class);

        updatedRequestType = updateRequestType(updatedRequestType);

        if (updatedRequestType == null) 
            return null;

        return fillDTO(updatedRequestType);
    }

    /* DELETE */
    @Override
    public boolean deleteRequestType(long requestTypeId) throws Exception {
        RequestType requestType = getById(requestTypeId);

        if (requestType == null) {
            return false;
            /* Not found with Id */
        }

        requestType.setStatus(Status.DELETED);
        requestTypeRepository.saveAndFlush(requestType);

        return true;
    }

    /* Utils */
    private RequestTypeReadDTO fillDTO(RequestType requestType) throws Exception {
        return modelMapper.map(requestType, RequestTypeReadDTO.class);
    }

    private List<RequestTypeReadDTO> fillAllDTO(Collection<RequestType> requestTypeCollection, Integer totalPage) throws Exception {
        return requestTypeCollection.stream()
                .map(requestType -> {
                    RequestTypeReadDTO requestTypeDTO =
                            modelMapper.map(requestType, RequestTypeReadDTO.class);

                    requestTypeDTO.setTotalPage(totalPage);

                    return requestTypeDTO;})
                .collect(Collectors.toList());
    }
}