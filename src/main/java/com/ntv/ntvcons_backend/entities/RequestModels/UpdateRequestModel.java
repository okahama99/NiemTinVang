package com.ntv.ntvcons_backend.entities.RequestModels;

import com.ntv.ntvcons_backend.entities.RequestDetailModels.CreateRequestDetailModel;
import com.ntv.ntvcons_backend.entities.RequestDetailModels.UpdateRequestDetailModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UpdateRequestModel {
    private Long requestId, projectId, requesterId, requestTypeId;
    private String requestDesc;
    private List<CreateRequestDetailModel> createRequestDetailModels;
    private List<UpdateRequestDetailModel> updateRequestDetailModels;
}
