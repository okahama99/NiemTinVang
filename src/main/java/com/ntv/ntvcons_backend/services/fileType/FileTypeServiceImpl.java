package com.ntv.ntvcons_backend.services.fileType;

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
                .existsByFileTypeNameOrFileTypeExtensionAndIsDeletedIsFalse(
                        newFileType.getFileTypeName(),
                        newFileType.getFileTypeExtension())) {
            errorMsg += "Already exists another FileType with fileTypeName: '" + newFileType.getFileTypeName()
                    + "'. Or with fileTypeExtension: '" + newFileType.getFileTypeExtension() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return fileTypeRepository.saveAndFlush(newFileType);
    }
    @Override
    public FileTypeReadDTO createFileTypeByDTO(FileTypeCreateDTO newFileTypeDTO) throws Exception {
        FileType newFileType = modelMapper.map(newFileTypeDTO, FileType.class);

        newFileType = createFileType(newFileType);

        return modelMapper.map(newFileType, FileTypeReadDTO.class);
    }

    /* READ */
    @Override
    public Page<FileType> getPageAll(Pageable paging) throws Exception {
        Page<FileType> fileTypePage = fileTypeRepository.findAllByIsDeletedIsFalse(paging);

        if (fileTypePage.isEmpty()) {
            return null;
        }

        return fileTypePage;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<FileType> fileTypePage = getPageAll(paging);

        if (fileTypePage == null) {
            return null;
        }

        List<FileType> fileTypeList = fileTypePage.getContent();

        if (fileTypeList.isEmpty()) {
            return null;
        }

        int totalPage = fileTypePage.getTotalPages();

        return fileTypeList.stream()
                .map(fileType -> {
                    FileTypeReadDTO fileTypeReadDTO =
                            modelMapper.map(fileType, FileTypeReadDTO.class);

                    fileTypeReadDTO.setTotalPage(totalPage);

                    return fileTypeReadDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public FileType getById(long fileTypeId) throws Exception {
        return fileTypeRepository
                .findByFileTypeIdAndIsDeletedIsFalse(fileTypeId)
                .orElse(null);
    }
    @Override
    public FileTypeReadDTO getDTOById(long fileTypeId) throws Exception {
        FileType fileType = getById(fileTypeId);

        if (fileType == null) {
            return null;
        }

        return modelMapper.map(fileType, FileTypeReadDTO.class);
    }

    @Override
    public List<FileType> getAllByIdIn(Collection<Long> fileTypeIdCollection) throws Exception {
        List<FileType> fileTypeList = fileTypeRepository.findAllByFileTypeIdInAndIsDeletedIsFalse(fileTypeIdCollection);

        if (fileTypeList.isEmpty()) {
            return null;
        }

        return fileTypeList;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOByIdIn(Collection<Long> fileTypeIdCollection) throws Exception {
        List<FileType> fileTypeList = getAllByIdIn(fileTypeIdCollection);

        if (fileTypeList == null) {
            return null;
        }

        return fileTypeList.stream()
                .map(fileType -> modelMapper.map(fileType, FileTypeReadDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public Map<Long, FileTypeReadDTO> mapFileTypeIdFileTypeDTOByIdIn(Collection<Long> fileTypeIdCollection) throws Exception {
        List<FileTypeReadDTO> fileTypeDTOList = getAllDTOByIdIn(fileTypeIdCollection);

        if (fileTypeDTOList == null) {
            return new HashMap<>();
        }

        return fileTypeDTOList.stream()
                .collect(Collectors.toMap(FileTypeReadDTO::getFileTypeId, Function.identity()));
    }
    @Override
    public Page<FileType> getPageAllByIdIn(Pageable paging, Collection<Long> fileTypeIdCollection) throws Exception {
        Page<FileType> fileTypePage =
                fileTypeRepository.findAllByFileTypeIdInAndIsDeletedIsFalse(fileTypeIdCollection, paging);

        if (fileTypePage.isEmpty()) {
            return null;
        }

        return fileTypePage;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOInPagingByIdIn(Pageable paging, Collection<Long> fileTypeIdCollection) throws Exception {
        Page<FileType> fileTypePage = getPageAllByIdIn(paging, fileTypeIdCollection);

        if (fileTypePage == null) {
            return null;
        }

        List<FileType> fileTypeList = fileTypePage.getContent();

        if (fileTypeList.isEmpty()) {
            return null;
        }

        int totalPage = fileTypePage.getTotalPages();

        return fileTypeList.stream()
                .map(fileType -> {
                    FileTypeReadDTO fileTypeReadDTO =
                            modelMapper.map(fileType, FileTypeReadDTO.class);

                    fileTypeReadDTO.setTotalPage(totalPage);

                    return fileTypeReadDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public FileType getByFileTypeName(String fileTypeName) throws Exception {
        return fileTypeRepository
                .findByFileTypeNameAndIsDeletedIsFalse(fileTypeName)
                .orElse(null);
    }
    @Override
    public FileTypeReadDTO getDTOByFileTypeName(String fileTypeName) throws Exception {
        FileType fileType = getByFileTypeName(fileTypeName);

        if (fileType == null) {
            return null;
        }

        return modelMapper.map(fileType, FileTypeReadDTO.class);
    }

    @Override
    public List<FileType> getAllByFileTypeNameContains(String fileTypeName) throws Exception {
        List<FileType> fileTypeList =
                fileTypeRepository.findAllByFileTypeNameContainsAndIsDeletedIsFalse(fileTypeName);

        if (fileTypeList.isEmpty()) {
            return null;
        }

        return fileTypeList;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOByFileTypeNameContains(String fileTypeName) throws Exception {
        List<FileType> fileTypeList = getAllByFileTypeNameContains(fileTypeName);

        if (fileTypeList == null) {
            return null;
        }

        return fileTypeList.stream()
                .map(fileType -> modelMapper.map(fileType, FileTypeReadDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public Page<FileType> getPageAllByFileTypeNameContains(Pageable paging, String fileTypeName) throws Exception {
        Page<FileType> fileTypePage =
                fileTypeRepository.findAllByFileTypeNameContainsAndIsDeletedIsFalse(fileTypeName, paging);

        if (fileTypePage.isEmpty()) {
            return null;
        }

        return fileTypePage;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOInPagingByFileTypeNameContains(Pageable paging, String fileTypeName) throws Exception {
        Page<FileType> fileTypePage = getPageAllByFileTypeNameContains(paging, fileTypeName);

        if (fileTypePage == null) {
            return null;
        }

        List<FileType> fileTypeList = fileTypePage.getContent();

        if (fileTypeList.isEmpty()) {
            return null;
        }

        int totalPage = fileTypePage.getTotalPages();

        return fileTypeList.stream()
                .map(fileType -> {
                    FileTypeReadDTO fileTypeReadDTO =
                            modelMapper.map(fileType, FileTypeReadDTO.class);

                    fileTypeReadDTO.setTotalPage(totalPage);

                    return fileTypeReadDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public FileType getByFileTypeExtension(String fileTypeExtension) throws Exception {
        return fileTypeRepository
                .findByFileTypeExtensionAndIsDeletedIsFalse(fileTypeExtension)
                .orElse(null);
    }
    @Override
    public FileTypeReadDTO getDTOByFileTypeExtension(String fileTypeExtension) throws Exception {
        FileType fileType = getByFileTypeExtension(fileTypeExtension);

        if (fileType == null) {
            return null;
        }

        return modelMapper.map(fileType, FileTypeReadDTO.class);
    }

    @Override
    public List<FileType> getAllByFileTypeExtensionContains(String fileTypeExtension) throws Exception {
        List<FileType> fileTypeList =
                fileTypeRepository.findAllByFileTypeExtensionContainsAndIsDeletedIsFalse(fileTypeExtension);

        if (fileTypeList.isEmpty()) {
            return null;
        }

        return fileTypeList;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOByFileTypeExtensionContains(String fileTypeExtension) throws Exception {
        List<FileType> fileTypeList = getAllByFileTypeExtensionContains(fileTypeExtension);

        if (fileTypeList == null) {
            return null;
        }

        return fileTypeList.stream()
                .map(fileType -> modelMapper.map(fileType, FileTypeReadDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public Page<FileType> getPageAllByFileTypeExtensionContains(Pageable paging, String fileTypeExtension) throws Exception {
        Page<FileType> fileTypePage =
                fileTypeRepository.findAllByFileTypeExtensionContainsAndIsDeletedIsFalse(fileTypeExtension, paging);

        if (fileTypePage.isEmpty()) {
            return null;
        }

        return fileTypePage;
    }
    @Override
    public List<FileTypeReadDTO> getAllDTOInPagingByFileTypeExtensionContains(Pageable paging, String fileTypeExtension) throws Exception {
        Page<FileType> fileTypePage = getPageAllByFileTypeExtensionContains(paging, fileTypeExtension);

        if (fileTypePage == null) {
            return null;
        }

        List<FileType> fileTypeList = fileTypePage.getContent();

        if (fileTypeList.isEmpty()) {
            return null;
        }

        int totalPage = fileTypePage.getTotalPages();

        return fileTypeList.stream()
                .map(fileType -> {
                    FileTypeReadDTO fileTypeReadDTO =
                            modelMapper.map(fileType, FileTypeReadDTO.class);

                    fileTypeReadDTO.setTotalPage(totalPage);

                    return fileTypeReadDTO;})
                .collect(Collectors.toList());
    }

    /* UPDATE */
    @Override
    public FileType updateFileType(FileType updatedFileType) throws Exception {
        FileType oldFileType = getById(updatedFileType.getFileTypeId());

        if (oldFileType == null) {
            return null;
            /* Not found by Id, return null */
        }

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
                .existsByFileTypeNameOrFileTypeExtensionAndFileTypeIdIsNotAndIsDeletedIsFalse(
                        updatedFileType.getFileTypeName(),
                        updatedFileType.getFileTypeExtension(),
                        updatedFileType.getFileTypeId())) {
            errorMsg += "Already exists another FileType with fileTypeName: '" + updatedFileType.getFileTypeName()
                    + "'. Or with fileTypeExtension: '" + updatedFileType.getFileTypeExtension() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        updatedFileType.setCreatedAt(oldFileType.getCreatedAt());
        updatedFileType.setCreatedBy(oldFileType.getCreatedBy());

        return fileTypeRepository.saveAndFlush(updatedFileType);
    }
    @Override
    public FileTypeReadDTO updateFileTypeByDTO(FileTypeUpdateDTO updatedFileTypeDTO) throws Exception {
        FileType updatedFileType = modelMapper.map(updatedFileTypeDTO, FileType.class);

        updatedFileType = updateFileType(updatedFileType);

        if (updatedFileType == null) {
            return null;
        }

        return modelMapper.map(updatedFileType, FileTypeReadDTO.class);
    }

    /* DELETE */
    @Override
    public boolean deleteFileType(long fileTypeId) throws Exception {
        FileType fileType = getById(fileTypeId);

        if (fileType == null) {
            return false;
            /* Not found with Id */
        }

        fileType.setIsDeleted(true);
//      TODO:  fileType.setStatus(Status.DELETED);
        fileTypeRepository.saveAndFlush(fileType);

        return true;
    }
}
