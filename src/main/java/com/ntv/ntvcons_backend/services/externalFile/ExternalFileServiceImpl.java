package com.ntv.ntvcons_backend.services.externalFile;

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
                .existsByFileLinkAndIsDeletedIsFalse(newFile.getFileLink())) {
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
                externalFileRepository.findAllByIsDeletedIsFalse(paging);

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
                .existsByFileIdAndIsDeletedIsFalse(fileId);
    }
    @Override
    public ExternalFile getById(long fileId) throws Exception {
        return externalFileRepository
                .findByFileIdAndIsDeletedIsFalse(fileId)
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
                .existsAllByFileIdInAndIsDeletedIsFalse(fileIdCollection);
    }
    @Override
    public List<ExternalFile> getAllByIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFile> fileList =
                externalFileRepository.findAllByFileIdInAndIsDeletedIsFalse(fileIdCollection);

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
    public Map<Long, List<ExternalFileReadDTO>> mapFileTypeDTOExternalFileDTOListByIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFileReadDTO> fileDTOList = getAllDTOByIdIn(fileIdCollection);

        if (fileDTOList == null)
            return new HashMap<>();

        Map<Long, List<ExternalFileReadDTO>> fileTypeDTOExternalFileDTOListMap = new HashMap<>();

        long tmpFileTypeId;
        List<ExternalFileReadDTO> tmpFileDTOList;

        for (ExternalFileReadDTO fileDTO : fileDTOList) {
            tmpFileTypeId = fileDTO.getFileType().getFileTypeId();
            tmpFileDTOList = fileTypeDTOExternalFileDTOListMap.get(tmpFileTypeId);

            if (tmpFileDTOList == null) {
                fileTypeDTOExternalFileDTOListMap
                        .put(tmpFileTypeId, new ArrayList<>(Collections.singletonList(fileDTO)));
            } else {
                tmpFileDTOList.add(fileDTO);

                fileTypeDTOExternalFileDTOListMap.put(tmpFileTypeId, fileDTOList);
            }
        }

        return fileTypeDTOExternalFileDTOListMap;
    }

    @Override
    public List<ExternalFile> getAllByFileTypeId(long fileTypeId) throws Exception {
        List<ExternalFile> fileList =
                externalFileRepository.findAllByFileTypeIdAndIsDeletedIsFalse(fileTypeId);

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
                externalFileRepository.findAllByFileTypeIdAndIsDeletedIsFalse(fileTypeId, paging);

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
                externalFileRepository.findAllByFileTypeIdInAndIsDeletedIsFalse(fileTypeIdCollection);

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
                externalFileRepository.findAllByFileTypeIdInAndIsDeletedIsFalse(fileTypeIdCollection, paging);

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
                externalFileRepository.findAllByFileNameContainsAndIsDeletedIsFalse(fileName);

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
                externalFileRepository.findAllByFileNameContainsAndIsDeletedIsFalse(fileName, paging);

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
        return null;
    }
    @Override
    public ExternalFileReadDTO updateExternalFileByDTO(ExternalFileUpdateDTO updatedFileDTO) throws Exception {
        return null;
    }

    /* DELETE */
    @Override
    public boolean deleteExternalFile(long fileId) throws Exception {
        return false;
    }

    @Override
    public boolean deleteAllByIdIn(Collection<Long> fileIdCollection) throws Exception {
        return false;
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
