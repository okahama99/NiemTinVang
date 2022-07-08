package com.ntv.ntvcons_backend.services.fileType;

import com.ntv.ntvcons_backend.dtos.fileType.FileTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.FileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FileTypeService {
    /* CREATE */
    FileType createFileType(FileType newFileType) throws Exception;
    FileTypeReadDTO createFileTypeByDTO(FileTypeCreateDTO newFileTypeDTO) throws Exception;

    /* READ */
    Page<FileType> getPageAll(Pageable paging) throws Exception;
    List<FileTypeReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    FileType getById(long fileTypeId) throws Exception;
    FileTypeReadDTO getDTOById(long fileTypeId) throws Exception;

    List<FileType> getAllByIdIn(Collection<Long> fileTypeIdCollection) throws Exception;
    List<FileTypeReadDTO> getAllDTOByIdIn(Collection<Long> fileTypeIdCollection) throws Exception;
    Map<Long, FileTypeReadDTO> mapFileTypeIdFileTypeDTOByIdIn(Collection<Long> fileTypeIdCollection) throws Exception;
    Page<FileType> getPageAllByIdIn(Pageable paging, Collection<Long> fileTypeIdCollection) throws Exception;
    List<FileTypeReadDTO> getAllDTOInPagingByIdIn(Pageable paging, Collection<Long> fileTypeIdCollection) throws Exception;

    FileType getByFileTypeName(String fileTypeName) throws Exception;
    FileTypeReadDTO getDTOByFileTypeName(String fileTypeName) throws Exception;

    List<FileType> getAllByFileTypeNameContains(String fileTypeName) throws Exception;
    List<FileTypeReadDTO> getAllDTOByFileTypeNameContains(String fileTypeName) throws Exception;
    Page<FileType> getPageAllByFileTypeNameContains(Pageable paging, String fileTypeName) throws Exception;
    List<FileTypeReadDTO> getAllDTOInPagingByFileTypeNameContains(Pageable paging, String fileTypeName) throws Exception;

    FileType getByFileTypeExtension(String fileTypeExtension) throws Exception;
    FileTypeReadDTO getDTOByFileTypeExtension(String fileTypeExtension) throws Exception;

    List<FileType> getAllByFileTypeExtensionContains(String fileTypeExtension) throws Exception;
    List<FileTypeReadDTO> getAllDTOByFileTypeExtensionContains(String fileTypeExtension) throws Exception;
    Page<FileType> getPageAllByFileTypeExtensionContains(Pageable paging, String fileTypeExtension) throws Exception;
    List<FileTypeReadDTO> getAllDTOInPagingByFileTypeExtensionContains(Pageable paging, String fileTypeExtension) throws Exception;

    /* UPDATE */
    FileType updateFileType(FileType updatedFileType) throws Exception;
    FileTypeReadDTO updateFileTypeByDTO(FileTypeUpdateDTO updatedFileTypeDTO) throws Exception;

    /* DELETE */
    boolean deleteFileType(long fileTypeId) throws Exception;
}
