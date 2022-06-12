package com.ntv.ntvcons_backend.services.fileType;

import com.ntv.ntvcons_backend.entities.FileType;
import com.ntv.ntvcons_backend.entities.FileTypeModels.ShowFileTypeModel;
import com.ntv.ntvcons_backend.repositories.FileTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileTypeServiceImpl implements FileTypeService{
    @Autowired
    private FileTypeRepository fileTypeRepository;

    /* CREATE */
    @Override
    public FileType createFileType(String fileTypeName, String fileTypeDesc, String fileTypeExtension) {
        return null;
    }

    /* READ */
    @Override
    public List<FileType> getAll() {
        return null;
    }

    @Override
    public List<FileType> getAllByFileTypeNameContains(String fileTypeName) {
        return null;
    }

    @Override
    public List<FileType> getAllByFileTypeExtensionContains(String fileTypeExtension) {
        return null;
    }

    @Override
    public FileType getByFileTypeName(String fileTypeName) {
        return null;
    }

    @Override
    public FileType getByFileTypeExtension(String fileTypeExtension) {
        return null;
    }

    @Override
    public FileType getById(int fileTypeId) {
        return null;
    }

    /* UPDATE */
    @Override
    public boolean updateFileType(ShowFileTypeModel showFileTypeModel) {
        return true;
    }

    /* DELETE */
    @Override
    public boolean deleteFileType(int fileTypeId) {
        return false;
    }
}
