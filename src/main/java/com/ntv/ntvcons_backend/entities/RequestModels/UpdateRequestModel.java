package com.ntv.ntvcons_backend.entities.RequestModels;

import com.ntv.ntvcons_backend.entities.RequestDetailModels.UpdateRequestDetailModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateRequestModel {
    private Long requestId, projectId, requesterId, requestTypeId;
    private String requestDesc, requestName;
    private List<UpdateRequestDetailModel> updateRequestDetailModels;
}
