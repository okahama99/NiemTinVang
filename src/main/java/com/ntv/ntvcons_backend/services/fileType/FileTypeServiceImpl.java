package com.ntv.ntvcons_backend.services.fileType;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.FileType;
import com.ntv.ntvcons_backend.repositories.FileTypeRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FileTypeServiceImpl implements FileTypeService{
    @Autowired
    private FileTypeRepository fileTypeRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public FileType createFileType(FileType newFileType) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!userService.existsById(newFileType.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newFileType.getCreatedBy()
                    + "'. Which violate constraint: FK_FIleType_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (fileTypeRepository
                .existsByFileTypeNameAndStatusNotIn(
                        newFileType.getFileTypeName(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another FileType with fileTypeName: '"
                    + newFileType.getFileTypeName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return fileTypeRepository.saveAndFlush(newFileType);
    }
    @Override
    public FileTypeReadDTO createFileTypeByDTO(FileTypeCreateDTO newFileTypeDTO) throws Exception {
        FileType newFileType = modelMapper.map(newFileTypeDTO, FileType.class);

        newFileType = createFileType(newFileType);

        return fillDTO(newFileType);
    }

    /* READ */
    @Override
    public Page<FileType> getPageAll(Pageable paging) throws Exception {
        Page<FileType> fileTypePage =
                fileTypeRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (fileTypePage.isEmpty()) 
            return null;

        return fileTypePage;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<FileType> fileTypePage = getPageAll(paging);

        if (fileTypePage == null) 
            return null;

        List<FileType> fileTypeList = fileTypePage.getContent();

        if (fileTypeList.isEmpty()) 
            return null;

        return fillAllDTO(fileTypeList, fileTypePage.getTotalPages());
    }

    @Override
    public FileType getById(long fileTypeId) throws Exception {
        return fileTypeRepository
                .findByFileTypeIdAndStatusNotIn(fileTypeId, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public FileTypeReadDTO getDTOById(long fileTypeId) throws Exception {
        FileType fileType = getById(fileTypeId);

        if (fileType == null) 
            return null;

        return fillDTO(fileType);
    }

    @Override
    public List<FileType> getAllByIdIn(Collection<Long> fileTypeIdCollection) throws Exception {
        List<FileType> fileTypeList =
                fileTypeRepository.findAllByFileTypeIdInAndStatusNotIn(fileTypeIdCollection, N_D_S_STATUS_LIST);

        if (fileTypeList.isEmpty()) 
            return null;

        return fileTypeList;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOByIdIn(Collection<Long> fileTypeIdCollection) throws Exception {
        List<FileType> fileTypeList = getAllByIdIn(fileTypeIdCollection);

        if (fileTypeList == null) 
            return null;

        return fillAllDTO(fileTypeList, null);
    }
    @Override
    public Map<Long, FileTypeReadDTO> mapFileTypeIdFileTypeDTOByIdIn(Collection<Long> fileTypeIdCollection) throws Exception {
        List<FileTypeReadDTO> fileTypeDTOList = getAllDTOByIdIn(fileTypeIdCollection);

        if (fileTypeDTOList == null) 
            return new HashMap<>();

        return fileTypeDTOList.stream()
                .collect(Collectors.toMap(FileTypeReadDTO::getFileTypeId, Function.identity()));
    }

    @Override
    public FileType getByFileTypeName(String fileTypeName) throws Exception {
        return fileTypeRepository
                .findByFileTypeNameAndStatusNotIn(fileTypeName, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public FileTypeReadDTO getDTOByFileTypeName(String fileTypeName) throws Exception {
        FileType fileType = getByFileTypeName(fileTypeName);

        if (fileType == null) 
            return null;

        return fillDTO(fileType);
    }

    @Override
    public List<FileType> getAllByFileTypeNameContains(String fileTypeName) throws Exception {
        List<FileType> fileTypeList =
                fileTypeRepository.findAllByFileTypeNameContainsAndStatusNotIn(fileTypeName, N_D_S_STATUS_LIST);

        if (fileTypeList.isEmpty()) 
            return null;

        return fileTypeList;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOByFileTypeNameContains(String fileTypeName) throws Exception {
        List<FileType> fileTypeList = getAllByFileTypeNameContains(fileTypeName);

        if (fileTypeList == null) 
            return null;

        return fillAllDTO(fileTypeList, null);
    }
    @Override
    public Page<FileType> getPageAllByFileTypeNameContains(Pageable paging, String fileTypeName) throws Exception {
        Page<FileType> fileTypePage =
                fileTypeRepository.findAllByFileTypeNameContainsAndStatusNotIn(fileTypeName, N_D_S_STATUS_LIST, paging);

        if (fileTypePage.isEmpty()) 
            return null;

        return fileTypePage;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOInPagingByFileTypeNameContains(Pageable paging, String fileTypeName) throws Exception {
        Page<FileType> fileTypePage = getPageAllByFileTypeNameContains(paging, fileTypeName);

        if (fileTypePage == null)
            return null;

        List<FileType> fileTypeList = fileTypePage.getContent();

        if (fileTypeList.isEmpty())
            return null;

        return fillAllDTO(fileTypeList, fileTypePage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public FileType updateFileType(FileType updatedFileType) throws Exception {
        FileType oldFileType = getById(updatedFileType.getFileTypeId());

        if (oldFileType == null) 
            return null; /* Not found by Id, return null */

        String errorMsg = "";

        /* Check FK */
        if (oldFileType.getUpdatedBy() != null) {
            if (!oldFileType.getUpdatedBy().equals(updatedFileType.getUpdatedBy())) {
                if (!userService.existsById(updatedFileType.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedFileType.getUpdatedBy()
                            + "'. Which violate constraint: FK_FIleType_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedFileType.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedFileType.getUpdatedBy()
                        + "'. Which violate constraint: FK_FIleType_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (fileTypeRepository
                .existsByFileTypeNameAndFileTypeIdIsNotAndStatusNotIn(
                        updatedFileType.getFileTypeName(),
                        updatedFileType.getFileTypeId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another FileType with fileTypeName: '"
                    + updatedFileType.getFileTypeName() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        updatedFileType.setCreatedAt(oldFileType.getCreatedAt());
        updatedFileType.setCreatedBy(oldFileType.getCreatedBy());

        return fileTypeRepository.saveAndFlush(updatedFileType);
    }
    @Override
    public FileTypeReadDTO updateFileTypeByDTO(FileTypeUpdateDTO updatedFileTypeDTO) throws Exception {
        FileType updatedFileType = modelMapper.map(updatedFileTypeDTO, FileType.class);

        updatedFileType = updateFileType(updatedFileType);

        if (updatedFileType == null) 
            return null;

        return fillDTO(updatedFileType);
    }

    /* DELETE */
    @Override
    public boolean deleteFileType(long fileTypeId) throws Exception {
        FileType fileType = getById(fileTypeId);

        if (fileType == null) {
            return false;
            /* Not found with Id */
        }

        fileType.setStatus(Status.DELETED);
        fileTypeRepository.saveAndFlush(fileType);

        return true;
    }

    /* Utils */
    private FileTypeReadDTO fillDTO(FileType fileType) throws Exception {
        return modelMapper.map(fileType, FileTypeReadDTO.class);
    }

    private List<FileTypeReadDTO> fillAllDTO(Collection<FileType> fileTypeCollection, Integer totalPage) throws Exception {
        return fileTypeCollection.stream()
                .map(fileType -> {
                    FileTypeReadDTO fileTypeReadDTO =
                            modelMapper.map(fileType, FileTypeReadDTO.class);

                    fileTypeReadDTO.setTotalPage(totalPage);

                    return fileTypeReadDTO;})
                .collect(Collectors.toList());
    }
}
