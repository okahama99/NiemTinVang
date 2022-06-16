package com.ntv.ntvcons_backend.services.fileType;

import com.ntv.ntvcons_backend.dtos.fileType.FileTypeCreateDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.fileType.FileTypeUpdateDTO;
import com.ntv.ntvcons_backend.entities.FileType;
import com.ntv.ntvcons_backend.repositories.FileTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileTypeServiceImpl implements FileTypeService{
    @Autowired
    private FileTypeRepository fileTypeRepository;
    @Autowired
    private ModelMapper modelMapper;

    /* CREATE */
    @Override
    public FileType createFileType(FileType newFileType) throws Exception {
        /* TODO check repeat name, extension */
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
    public List<FileType> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<FileType> fileTypePage = fileTypeRepository.findAllByIsDeletedIsFalse(paging);

        if (fileTypePage.isEmpty()) {
            return null;
        }

        return fileTypePage.getContent();
    }
    @Override
    public List<FileTypeReadDTO> getAllDTO(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        List<FileType> fileTypeList = getAll(pageNo, pageSize, sortBy, sortType);

        if (fileTypeList != null && !fileTypeList.isEmpty()) {
            int totalPage = (int) Math.ceil((double) fileTypeList.size() / pageSize);

            return fileTypeList.stream()
                    .map(fileType -> {
                        FileTypeReadDTO fileTypeReadDTO =
                                modelMapper.map(fileType, FileTypeReadDTO.class);
                        fileTypeReadDTO.setTotalPage(totalPage);
                        return fileTypeReadDTO;})
                    .collect(Collectors.toList());

        } else {
            return null;
        }
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

        return fileTypeList.stream().map(fileType -> modelMapper.map(fileType, FileTypeReadDTO.class))
                .collect(Collectors.toList());
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

    /* UPDATE */
    @Override
    public FileType updateFileType(FileType updatedFileType) throws Exception {
        FileType fileType = getById(updatedFileType.getFileTypeId());

        if (fileType == null) {
            return null;
            /* Not found by Id, return null */
        }

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
        fileTypeRepository.saveAndFlush(fileType);

        return true;
    }
}
