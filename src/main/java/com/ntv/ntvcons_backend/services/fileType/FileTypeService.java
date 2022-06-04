package com.ntv.ntvcons_backend.services.fileType;

import com.ntv.ntvcons_backend.entities.FileType;
import com.ntv.ntvcons_backend.entities.fileTypeModels.FileTypeModel;

import java.util.List;

public interface FileTypeService {
    /* CREATE */
    FileType createFileType(String fileTypeName, String fileTypeDesc, String fileTypeExtension);

    /* READ */
    List<FileType> getAll();

    List<FileType> getAllByFileTypeNameLike(String fileTypeName);

    List<FileType> getAllByFileTypeExtensionLike(String fileTypeExtension);

    FileType getByFileTypeName(String fileTypeName);

    FileType getByFileTypeExtension(String fileTypeExtension);

    FileType getById(int fileTypeId);

    /* UPDATE */
    boolean updateFileType(FileTypeModel fileTypeModel);


    /* DELETE */
    boolean deleteFileType(int fileTypeId);
}
