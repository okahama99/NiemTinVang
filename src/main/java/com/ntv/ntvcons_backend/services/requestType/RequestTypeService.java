package com.ntv.ntvcons_backend.services.requestType;

import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.RequestType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RequestTypeService {
    /* CREATE */
    RequestType createRequestType(RequestType newRequestType) throws Exception;
    RequestTypeReadDTO createRequestTypeByDTO(RequestTypeCreateDTO newRequestTypeDTO) throws Exception;

    /* READ */
    List<RequestType> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<RequestTypeReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    boolean existsById(long requestTypeId) throws Exception;
    RequestType getById(long requestTypeId) throws Exception;
    RequestTypeReadDTO getDTOById(long requestTypeId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> requestTypeIdCollection) throws Exception;
    List<RequestType> getAllByIdIn(Collection<Long> requestTypeIdCollection) throws Exception;
    List<RequestTypeReadDTO> getAllDTOByIdIn(Collection<Long> requestTypeIdCollection) throws Exception;
    Map<Long, RequestType> mapRequestTypeIdRequestTypeByIdIn(Collection<Long> requestTypeIdCollection) throws Exception;
    Map<Long, RequestTypeReadDTO> mapRequestTypeIdRequestTypeDTOByIdIn(Collection<Long> requestTypeIdCollection) throws Exception;

    List<RequestType> getAllByRequestTypeNameContains(String requestTypeName) throws Exception;
    List<RequestTypeReadDTO> getAllDTOByRequestTypeNameContains(String requestTypeName) throws Exception;

    /* UPDATE */
    RequestType updateRequestType(RequestType updatedRequestType) throws Exception;
    RequestTypeReadDTO updateRequestTypeByDTO(RequestTypeUpdateDTO updatedRequestTypeDTO) throws Exception;

    /* DELETE */
    boolean deleteRequestType(long requestTypeId) throws Exception;
}