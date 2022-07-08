package com.ntv.ntvcons_backend.dtos.externalFile;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeReadDTO;
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
    private FileTypeReadDTO fileType;
}
