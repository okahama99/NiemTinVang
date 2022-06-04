package com.ntv.ntvcons_backend.entities.FileTypeModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowFileTypeModel {
    private int fileTypeId;
    private String fileTypeName, fileTypeDesc, fileTypeExtension;

}
