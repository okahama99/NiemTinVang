package com.ntv.ntvcons_backend.services.externalFile;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileUpdateDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeReadDTO;
import com.ntv.ntvcons_backend.entities.ExternalFile;
import com.ntv.ntvcons_backend.repositories.ExternalFileRepository;
import com.ntv.ntvcons_backend.services.fileType.FileTypeService;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExternalFileServiceImpl implements ExternalFileService {
    @Autowired
    private ExternalFileRepository externalFileRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileTypeService fileTypeService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    private final String DELETED = Status.DELETED.getStringValue();

    /* CREATE */
    @Override
    public ExternalFile createExternalFile(ExternalFile newFile) throws Exception {
        String errorMsg = "";

        /* TODO: check url link validate */

        /* Check FK */
        if (userService.existsById(newFile.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newFile.getCreatedBy()
                    + "'. Which violate constraint: FK_ExternalFile_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (externalFileRepository
                .existsByFileLinkAndStatusNotContains(
                        newFile.getFileLink(),
                        DELETED)) {
            errorMsg += "Already exists another ExternalFile with link: '"
                    + newFile.getFileLink() + "'. ";
        }

        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        return externalFileRepository.saveAndFlush(newFile);
    }
    @Override
    public ExternalFileReadDTO createExternalFileByDTO(ExternalFileCreateDTO newFileDTO) throws Exception {
        ExternalFile newFile = modelMapper.map(newFileDTO, ExternalFile.class);

        newFile = createExternalFile(newFile);

        return fillDTO(newFile);
    }

    /* READ */
    @Override
    public Page<ExternalFile> getPageAll(Pageable paging) throws Exception {
        Page<ExternalFile> filePage =
                externalFileRepository.findAllByStatusNotContains(DELETED, paging);

        if (filePage.isEmpty())
            return null;

        return filePage;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<ExternalFile> filePage = getPageAll(paging);

        if (filePage == null)
            return null;

        List<ExternalFile> fileList = filePage.getContent();

        if (fileList.isEmpty())
            return null;

        return fillAllDTO(fileList, filePage.getTotalPages());
    }

    @Override
    public boolean existsById(long fileId) throws Exception {
        return externalFileRepository
                .existsByFileIdAndStatusNotContains(fileId, DELETED);
    }
    @Override
    public ExternalFile getById(long fileId) throws Exception {
        return externalFileRepository
                .findByFileIdAndStatusNotContains(fileId, DELETED)
                .orElse(null);
    }
    @Override
    public ExternalFileReadDTO getDTOById(long fileId) throws Exception {
        ExternalFile file = getById(fileId);

        if (file == null)
            return null;

        return fillDTO(file);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> fileIdCollection) throws Exception {
        return externalFileRepository
                .existsAllByFileIdInAndStatusNotContains(fileIdCollection, DELETED);
    }
    @Override
    public List<ExternalFile> getAllByIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFile> fileList =
                externalFileRepository.findAllByFileIdInAndStatusNotContains(fileIdCollection, DELETED);

        if (fileList.isEmpty())
            return null;

        return fileList;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOByIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFile> fileList = getAllByIdIn(fileIdCollection);

        if (fileList == null)
            return null;

        return fillAllDTO(fileList, null);
    }
    @Override
    public Map<Long, ExternalFileReadDTO> mapFileIdExternalFileDTOListByIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFileReadDTO> fileDTOList = getAllDTOByIdIn(fileIdCollection);

        if (fileDTOList == null)
            return new HashMap<>();

        return fileDTOList.stream()
                .collect(Collectors.toMap(ExternalFileReadDTO::getFileId, Function.identity()));
    }

    @Override
    public List<ExternalFile> getAllByFileTypeId(long fileTypeId) throws Exception {
        List<ExternalFile> fileList =
                externalFileRepository.findAllByFileTypeIdAndStatusNotContains(fileTypeId, DELETED);

        if (fileList.isEmpty())
            return null;

        return fileList;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOByFileTypeId(long fileTypeId) throws Exception {
        List<ExternalFile> fileList = getAllByFileTypeId(fileTypeId);

        if (fileList == null)
            return null;

        return fillAllDTO(fileList, null);
    }
    @Override
    public Page<ExternalFile> getPageAllByFileTypeId(Pageable paging, long fileTypeId) throws Exception {
        Page<ExternalFile> filePage =
                externalFileRepository.findAllByFileTypeIdAndStatusNotContains(fileTypeId, paging, DELETED);

        if (filePage.isEmpty())
            return null;

        return filePage;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOInPagingByFileTypeId(Pageable paging, long fileTypeId) throws Exception {
        Page<ExternalFile> filePage = getPageAllByFileTypeId(paging, fileTypeId);

        if (filePage == null)
            return null;

        List<ExternalFile> fileList = filePage.getContent();

        if (fileList.isEmpty())
            return null;

        return fillAllDTO(fileList, filePage.getTotalPages());
    }

    @Override
    public List<ExternalFile> getAllByFileTypeIdIn(Collection<Long> fileTypeIdCollection) throws Exception {
        List<ExternalFile> fileList =
                externalFileRepository.findAllByFileTypeIdInAndStatusNotContains(fileTypeIdCollection, DELETED);

        if (fileList.isEmpty())
            return null;

        return fileList;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOByFileTypeIdIn(Collection<Long> fileTypeIdCollection) throws Exception {
        List<ExternalFile> fileList = getAllByFileTypeIdIn(fileTypeIdCollection);

        if (fileList == null)
            return null;

        return fillAllDTO(fileList, null);
    }
    @Override
    public Page<ExternalFile> getPageAllByFileTypeIdIn(Pageable paging, Collection<Long> fileTypeIdCollection) throws Exception {
        Page<ExternalFile> filePage =
                externalFileRepository.findAllByFileTypeIdInAndStatusNotContains(fileTypeIdCollection, paging, DELETED);

        if (filePage.isEmpty())
            return null;

        return filePage;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOInPagingByFileTypeIdIn(Pageable paging, Collection<Long> fileTypeIdCollection) throws Exception {
        Page<ExternalFile> filePage = getPageAllByFileTypeIdIn(paging, fileTypeIdCollection);

        if (filePage == null)
            return null;

        List<ExternalFile> fileList = filePage.getContent();

        if (fileList.isEmpty())
            return null;

        return fillAllDTO(fileList, filePage.getTotalPages());
    }

    @Override
    public List<ExternalFile> getAllByNameContains(String fileName) throws Exception {
        List<ExternalFile> fileList =
                externalFileRepository.findAllByFileNameContainsAndStatusNotContains(fileName, DELETED);

        if (fileList.isEmpty())
            return null;

        return fileList;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOByNameContains(String fileName) throws Exception {
        List<ExternalFile> fileList = getAllByNameContains(fileName);

        if (fileList == null)
            return null;

        return fillAllDTO(fileList, null);
    }
    @Override
    public Page<ExternalFile> getPageAllByNameContains(Pageable paging, String fileName) throws Exception {
        Page<ExternalFile> filePage =
                externalFileRepository.findAllByFileNameContainsAndStatusNotContains(fileName, paging, DELETED);

        if (filePage.isEmpty())
            return null;

        return filePage;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOInPagingByNameContains(Pageable paging, String fileName) throws Exception {
        Page<ExternalFile> filePage = getPageAllByNameContains(paging, fileName);

        if (filePage == null)
            return null;

        List<ExternalFile> fileList = filePage.getContent();

        if (fileList.isEmpty())
            return null;

        return fillAllDTO(fileList, filePage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public ExternalFile updateExternalFile(ExternalFile updatedFile) throws Exception {
        ExternalFile oldFile = getById(updatedFile.getFileId());

        if (oldFile == null)
            return null;

        String errorMsg = "";

        /* TODO: check url link validate */

        /* Check FK */
        if (oldFile.getUpdatedBy() != null) {
            if (!oldFile.getUpdatedBy().equals(updatedFile.getUpdatedBy())) {
                if (userService.existsById(updatedFile.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedFile.getUpdatedBy()
                            + "'. Which violate constraint: FK_ExternalFile_User_UpdatedBy. ";
                }
            }
        } else {
            if (userService.existsById(updatedFile.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedFile.getUpdatedBy()
                        + "'. Which violate constraint: FK_ExternalFile_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (externalFileRepository
                .existsByFileLinkAndFileIdNotAndStatusNotContains(
                        updatedFile.getFileLink(),
                        updatedFile.getFileId(),
                        DELETED)) {
            errorMsg += "Already exists another ExternalFile with link: '"
                    + updatedFile.getFileLink() + "'. ";
        }

        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        updatedFile.setCreatedAt(oldFile.getCreatedAt());
        updatedFile.setCreatedBy(oldFile.getCreatedBy());

        return externalFileRepository.saveAndFlush(updatedFile);
    }
    @Override
    public ExternalFileReadDTO updateExternalFileByDTO(ExternalFileUpdateDTO updatedFileDTO) throws Exception {
        ExternalFile updatedFile = modelMapper.map(updatedFileDTO, ExternalFile.class);

        updatedFile = createExternalFile(updatedFile);

        if (updatedFile == null)
            return null;

        return fillDTO(updatedFile);
    }

    /* DELETE */
    @Override
    public boolean deleteExternalFile(long fileId) throws Exception {
        ExternalFile file = getById(fileId);

        if (file == null)
            return false;

        file.setStatus(Status.DELETED);
        externalFileRepository.saveAndFlush(file);

        return true;
    }

    @Override
    public boolean deleteAllByIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFile> fileList = getAllByIdIn(fileIdCollection);

        if (fileList == null)
            return false;


        fileList = fileList.stream()
                .peek(file -> file.setStatus(Status.DELETED))
                .collect(Collectors.toList());

        externalFileRepository.saveAllAndFlush(fileList);

        return true;
    }

    /* Utils */
    private ExternalFileReadDTO fillDTO(ExternalFile file) throws Exception {
        ExternalFileReadDTO fileDTO = modelMapper.map(file, ExternalFileReadDTO.class);

        /* Get associate fileType */
        fileDTO.setFileType(
                fileTypeService.getDTOById(file.getFileTypeId()));

        return fileDTO;
    }

    private List<ExternalFileReadDTO> fillAllDTO(Collection<ExternalFile> fileCollection, Integer totalPage) throws Exception {
        Set<Long> fileTypeIdSet = new HashSet<>();

        for (ExternalFile file : fileCollection) {
            fileTypeIdSet.add(file.getFileTypeId());
        }

        /* Get associate fileType */
        Map<Long, FileTypeReadDTO> fileTypeIdFileTypeDTOMap =
                fileTypeService.mapFileTypeIdFileTypeDTOByIdIn(fileTypeIdSet);

        return fileCollection.stream()
                .map(file -> {
                    ExternalFileReadDTO fileDTO =
                            modelMapper.map(file, ExternalFileReadDTO.class);

                    fileDTO.setFileType(fileTypeIdFileTypeDTOMap.get(file.getFileTypeId()));

                    fileDTO.setTotalPage(totalPage);

                    return fileDTO;})
                .collect(Collectors.toList());
    }
}
