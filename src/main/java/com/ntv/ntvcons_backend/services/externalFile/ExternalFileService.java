package com.ntv.ntvcons_backend.services.externalFile;

import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileUpdateDTO;
import com.ntv.ntvcons_backend.entities.ExternalFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ExternalFileService {
    /* CREATE */
    ExternalFile createExternalFile(ExternalFile newFile) throws Exception;
    ExternalFileReadDTO createExternalFileByDTO(ExternalFileCreateDTO newFileDTO) throws Exception;

    List<ExternalFile> createBulkExternalFile(List<ExternalFile> newFileList) throws Exception;
    List<ExternalFileReadDTO> createBulkExternalFileByDTO(List<ExternalFileCreateDTO> newFileDTOList) throws Exception;

    /* READ */
    Page<ExternalFile> getPageAll(Pageable paging) throws Exception;
    List<ExternalFileReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    boolean existsById(long fileId) throws Exception;
    ExternalFile getById(long fileId) throws Exception;
    ExternalFileReadDTO getDTOById(long fileId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> fileIdCollection) throws Exception;
    List<ExternalFile> getAllByIdIn(Collection<Long> fileIdCollection) throws Exception;
    List<ExternalFileReadDTO> getAllDTOByIdIn(Collection<Long> fileIdCollection) throws Exception;
    Map<Long, ExternalFileReadDTO> mapFileIdExternalFileDTOByIdIn(Collection<Long> fileIdCollection) throws Exception;

    List<ExternalFile> getAllByFileType(FileType fileType) throws Exception;
    List<ExternalFileReadDTO> getAllDTOByFileType(FileType fileType) throws Exception;
    Page<ExternalFile> getPageAllByFileType(Pageable paging, FileType fileTypeId) throws Exception;
    List<ExternalFileReadDTO> getAllDTOInPagingByFileType(Pageable paging, FileType fileType) throws Exception;

    boolean existsByFileName(String fileName) throws Exception;
    ExternalFile getByFileName(String fileName) throws Exception;
    ExternalFileReadDTO getDTOByFileName(String fileName) throws Exception;

    List<ExternalFile> getAllByFileNameContains(String fileName) throws Exception;
    List<ExternalFileReadDTO> getAllDTOByFileNameContains(String fileName) throws Exception;
    Page<ExternalFile> getPageAllByFileNameContains(Pageable paging, String fileName) throws Exception;
    List<ExternalFileReadDTO> getAllDTOInPagingByFileNameContains(Pageable paging, String fileName) throws Exception;

    boolean existsByFileLink(String fileLink) throws Exception;
    ExternalFile getByFileLink(String fileLink) throws Exception;
    ExternalFileReadDTO getDTOByFileLink(String fileLink) throws Exception;

    List<ExternalFile> getAllByFileLinkContains(String fileLink) throws Exception;
    List<ExternalFileReadDTO> getAllDTOByFileLinkContains(String fileLink) throws Exception;
    Page<ExternalFile> getPageAllByFileLinkContains(Pageable paging, String fileLink) throws Exception;
    List<ExternalFileReadDTO> getAllDTOInPagingByFileLinkContains(Pageable paging, String fileLink) throws Exception;

    boolean existsByFileNameOrFileLink(String fileName, String fileLink) throws Exception;

    /* UPDATE */
    ExternalFile updateExternalFile(ExternalFile updatedFile) throws Exception;
    ExternalFileReadDTO updateExternalFileByDTO(ExternalFileUpdateDTO updatedFileDTO) throws Exception;

    List<ExternalFile> updateBulkExternalFile(List<ExternalFile> updatedFileList) throws Exception;
    List<ExternalFileReadDTO> updateBulkExternalFileByDTO(List<ExternalFileUpdateDTO> updatedFileDTOList) throws Exception;

    /* DELETE */
    boolean deleteExternalFile(long fileId) throws Exception;
    boolean deleteAllByIdIn(Collection<Long> fileIdCollection) throws Exception;
}
