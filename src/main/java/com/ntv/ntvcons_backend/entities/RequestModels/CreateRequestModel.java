package com.ntv.ntvcons_backend.entities.RequestModels;

import com.ntv.ntvcons_backend.entities.RequestDetailModels.RequestDetailModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateRequestModel {
    private Long projectId, requesterId, requestTypeId;
    private String requestDesc;
    private String requestDate;
    private List<RequestDetailModel> modelList;

}
