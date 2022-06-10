package com.ntv.ntvcons_backend.services.externalFile;

import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileDTO;
import com.ntv.ntvcons_backend.entities.ExternalFile;
import com.ntv.ntvcons_backend.repositories.ExternalFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ExternalFileServiceImpl implements ExternalFileService {
    @Autowired
    private ExternalFileRepository externalFileRepository;

    /* CREATE */
    @Override
    public ExternalFile createExternalFile(String fileName, String fileLink, int fileTypeId) {
        return null;
    }

    /* READ */
    @Override
    public ExternalFile checkDuplicateByFileName(String fileName) {
        return null;
    }

    @Override
    public List<ExternalFileDTO> getAllWithPagingAndSorting(int pageNo, int pageSize, String sortBy, boolean sortType) {
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

    /* UPDATE */
    @Override
    public ExternalFile updateExternalFile(int id, String name, String fileLink, int typeID) {
        return null;
    }

    /* DELETE */
    @Override
    public boolean deleteExternalFile(int id) {
        return false;
    }
}
