package com.ntv.ntvcons_backend.services.fileType;

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
    FileType updateFileType(int fileTypeId, String fileTypeName, String fileTypeDesc, String fileTypeExtension);


    /* DELETE */
    boolean deleteFileType(int fileTypeId);
}
