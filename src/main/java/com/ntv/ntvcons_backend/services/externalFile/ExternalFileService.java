package com.ntv.ntvcons_backend.services.externalFile;

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

    /* READ */
    Page<ExternalFile> getPageAll(Pageable paging) throws Exception;
    List<ExternalFileReadDTO> getAllDTOInPaging(Pageable paging) throws Exception;

    boolean existsById(long fileId) throws Exception;
    ExternalFile getById(long fileId) throws Exception;
    ExternalFileReadDTO getDTOById(long fileId) throws Exception;

    boolean existsAllByIdIn(Collection<Long> fileIdCollection) throws Exception;
    List<ExternalFile> getAllByIdIn(Collection<Long> fileIdCollection) throws Exception;
    List<ExternalFileReadDTO> getAllDTOByIdIn(Collection<Long> fileIdCollection) throws Exception;
    Map<Long, List<ExternalFileReadDTO>> mapFileTypeDTOExternalFileDTOListByIdIn(Collection<Long> fileIdCollection) throws Exception;

    List<ExternalFile> getAllByFileTypeId(long fileTypeId) throws Exception;
    List<ExternalFileReadDTO> getAllDTOByFileTypeId(long fileTypeId) throws Exception;
    Page<ExternalFile> getPageAllByFileTypeId(Pageable paging, long fileTypeId) throws Exception;
    List<ExternalFileReadDTO> getAllDTOInPagingByFileTypeId(Pageable paging, long fileTypeId) throws Exception;

    List<ExternalFile> getAllByFileTypeIdIn(Collection<Long> fileTypeIdCollection) throws Exception;
    List<ExternalFileReadDTO> getAllDTOByFileTypeIdIn(Collection<Long> fileTypeIdCollection) throws Exception;
    Page<ExternalFile> getPageAllByFileTypeIdIn(Pageable paging, Collection<Long> fileTypeIdCollection) throws Exception;
    List<ExternalFileReadDTO> getAllDTOInPagingByFileTypeIdIn(Pageable paging, Collection<Long> fileTypeIdCollection) throws Exception;

    List<ExternalFile> getAllByNameContains(String fileName) throws Exception;
    List<ExternalFileReadDTO> getAllDTOByNameContains(String fileName) throws Exception;
    Page<ExternalFile> getPageAllByNameContains(Pageable paging, String fileName) throws Exception;
    List<ExternalFileReadDTO> getAllDTOInPagingByNameContains(Pageable paging, String fileName) throws Exception;

    /* UPDATE */
    ExternalFile updateExternalFile(ExternalFile updatedFile) throws Exception;
    ExternalFileReadDTO updateExternalFileByDTO(ExternalFileUpdateDTO updatedFileDTO) throws Exception;

    /* DELETE */
    boolean deleteExternalFile(long fileId) throws Exception;
    boolean deleteAllByIdIn(Collection<Long> fileIdCollection) throws Exception;
}
