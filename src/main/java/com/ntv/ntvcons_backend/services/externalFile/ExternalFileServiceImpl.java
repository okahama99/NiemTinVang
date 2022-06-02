package com.ntv.ntvcons_backend.services.externalFile;

import com.ntv.ntvcons_backend.entities.externalFileModel.ShowExternalFileModel;
import com.ntv.ntvcons_backend.repositories.ExternalFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ExternalFileServiceImpl implements ExternalFileService {
    @Autowired
    private ExternalFileRepository externalFileRepository;

    @Override
    public ExternalFile createExternalFile(String fileName, String fileLink, int fileTypeId) {
        return null;
    }

    @Override
    public ExternalFile checkDuplicateByFileName(String fileName) {
        return null;
    }

    @Override
    public List<ShowExternalFileModel> getAllWithPagingAndSorting(int pageNo, int pageSize, String sortBy, boolean sortType) {
        return null;
    }

    @Override
    public List<ExternalFile> getAllByFileTypeId(int fileTypeId) {
        return null;
    }

    @Override
    public List<ExternalFile> getAllByFileTypeIdIn(Collection<Integer> fileTypeIdCollection) {
        return null;
    }

    @Override
    public List<ExternalFile> getAllByNameLike(String fileName) {
        return null;
    }

    @Override
    public List<ExternalFile> getAllByIdIn(Collection<Integer> fileIdCollection) {
        return null;
    }

    @Override
    public ExternalFile getById(int fileId) {
        return null;
    }

    @Override
    public ExternalFile updateExternalFile(int id, String name, String fileLink, int typeID) {
        return null;
    }

    @Override
    public boolean deleteExternalFile(int id) {
        return false;
    }
}
