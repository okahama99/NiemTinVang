package com.ntv.ntvcons_backend.services.misc;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface FileCombineService {
    /* CREATE */
    boolean saveFileInDBAndFirebase(
            MultipartFile multipartFile, FileType fileType,
            long entityId, EntityType type, long createdBy) throws Exception;

    boolean saveAllFileInDBAndFirebase(
            List<MultipartFile> multipartFileList, FileType fileType,
            long entityId, EntityType type, long createdBy) throws Exception;

    /* READ */

    /* UPDATE */

    /* DELETE */
    boolean deleteFileInDBAndFirebaseByFileDTO(ExternalFileReadDTO fileDTO) throws Exception;

    boolean deleteAllFileInDBAndFirebaseByFileDTO(Collection<ExternalFileReadDTO> fileDTOCollection) throws Exception;
}
