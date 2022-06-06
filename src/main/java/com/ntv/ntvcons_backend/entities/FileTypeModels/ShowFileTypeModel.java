package com.ntv.ntvcons_backend.entities.FileTypeModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowFileTypeModel {
    private long fileTypeId;
    private String fileTypeName, fileTypeDesc, fileTypeExtension;

}
