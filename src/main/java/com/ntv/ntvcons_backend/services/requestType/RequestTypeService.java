package com.ntv.ntvcons_backend.services.requestType;

import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.RequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RequestTypeService {
    /* CREATE */
    RequestType createRequestType(RequestType newRequestType) throws Exception;
    RequestTypeReadDTO createRequestTypeByDTO(RequestTypeCreateDTO newRequestTypeDTO) throws Exception;

    /* READ */
    Page<RequestType> getPageAll(Pageable paging) throws Exception;
    List<RequestTypeReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    boolean existsById(long requestTypeId) throws Exception;
    RequestType getById(long requestTypeId) throws Exception;
    RequestTypeReadDTO getDTOById(long requestTypeId) throws Exception;

    List<RequestType> getAllByIdIn(Collection<Long> requestTypeIdCollection) throws Exception;
    List<RequestTypeReadDTO> getAllDTOByIdIn(Collection<Long> requestTypeIdCollection) throws Exception;
    Map<Long, RequestTypeReadDTO> mapRequestTypeIdRequestTypeDTOByIdIn(Collection<Long> requestTypeIdCollection) throws Exception;

    RequestType getByRequestTypeName(String requestTypeName) throws Exception;
    RequestTypeReadDTO getDTOByRequestTypeName(String requestTypeName) throws Exception;

    List<RequestType> getAllByRequestTypeNameContains(String requestTypeName) throws Exception;
    List<RequestTypeReadDTO> getAllDTOByRequestTypeNameContains(String requestTypeName) throws Exception;
    Page<RequestType> getPageAllByRequestTypeNameContains(Pageable paging, String requestTypeName) throws Exception;
    List<RequestTypeReadDTO> getAllDTOInPagingByRequestTypeNameContains(Pageable paging, String requestTypeName) throws Exception;

    /* UPDATE */
    RequestType updateRequestType(RequestType updatedRequestType) throws Exception;
    RequestTypeReadDTO updateRequestTypeByDTO(RequestTypeUpdateDTO updatedRequestTypeDTO) throws Exception;

    /* DELETE */
    boolean deleteRequestType(long requestTypeId) throws Exception;
}