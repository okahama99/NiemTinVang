package com.ntv.ntvcons_backend.dtos.externalFile;

import com.ntv.ntvcons_backend.dtos.fileType.FileTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalFileDTO implements Serializable {
    private Integer fileId;
    private String fileName;
    private String fileLink;
    private FileTypeDTO fileType;
    private Boolean isDeleted = false;
}
