package com.ntv.ntvcons_backend.services.fileType;

import com.ntv.ntvcons_backend.dtos.fileType.FileTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.FileType;

import java.util.Collection;
import java.util.List;

public interface FileTypeService {
    /* CREATE */
    FileType createFileType(FileType newFileType) throws Exception;
    FileTypeReadDTO createFileTypeByDTO(FileTypeCreateDTO newFileTypeDTO) throws Exception;

    /* READ */
    List<FileType> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;
    List<FileTypeReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception;

    FileType getById(long fileTypeId) throws Exception;
    FileTypeReadDTO getDTOById(long fileTypeId) throws Exception;

    List<FileType> getAllByIdIn(Collection<Long> fileTypeIdCollection) throws Exception;
    List<FileTypeReadDTO> getAllDTOByIdIn(Collection<Long> fileTypeIdCollection) throws Exception;

    List<FileType> getAllByFileTypeNameContains(String fileTypeExtension) throws Exception;
    List<FileTypeReadDTO> getAllDTOByFileTypeNameContains(String fileTypeExtension) throws Exception;

    List<FileType> getAllByFileTypeExtensionContains(String fileTypeName) throws Exception;
    List<FileTypeReadDTO> getAllDTOByFileTypeExtensionContains(String fileTypeName) throws Exception;

    /* UPDATE */
    FileType updateFileType(FileType updatedFileType) throws Exception;
    FileTypeReadDTO updateFileTypeByDTO(FileTypeUpdateDTO updatedFileTypeDTO) throws Exception;

    /* DELETE */
    boolean deleteFileType(long fileTypeId) throws Exception;
}
