package com.ntv.ntvcons_backend.services.fileType;

import com.ntv.ntvcons_backend.entities.FileType;
import com.ntv.ntvcons_backend.entities.FileTypeModels.ShowFileTypeModel;

import java.util.List;

public interface FileTypeService {
    /* CREATE */
    FileType createFileType(String fileTypeName, String fileTypeDesc, String fileTypeExtension);

    /* READ */
    List<FileType> getAll();

    List<FileType> getAllByFileTypeNameContains(String fileTypeName);

    List<FileType> getAllByFileTypeExtensionContains(String fileTypeExtension);

    FileType getByFileTypeName(String fileTypeName);

    FileType getByFileTypeExtension(String fileTypeExtension);

    FileType getById(int fileTypeId);

    /* UPDATE */
    boolean updateFileType(ShowFileTypeModel showFileTypeModel);

    /* DELETE */
    boolean deleteFileType(int fileTypeId);
}
