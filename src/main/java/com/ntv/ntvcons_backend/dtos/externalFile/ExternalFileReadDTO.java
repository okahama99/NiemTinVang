package com.ntv.ntvcons_backend.dtos.externalFile;

import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalFileReadDTO extends BaseReadDTO {
    private Long fileId;
    private String fileName;
    private String fileLink;
    private FileType fileType;
}
