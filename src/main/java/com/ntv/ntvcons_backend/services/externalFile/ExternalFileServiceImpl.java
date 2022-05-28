package com.ntv.ntvcons_backend.services.externalFile;

import com.ntv.ntvcons_backend.entities.ExternalFile;
import com.ntv.ntvcons_backend.entities.externalFileModel.ShowExternalFileModel;
import com.ntv.ntvcons_backend.repositories.ExternalFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalFileServiceImpl implements ExternalFileService {
    @Autowired
    ExternalFileRepository externalFileRepository;

    @Override
    public ExternalFile createExternalFile(String name, String fileLink, int typeID) {
        return null;
    }

    @Override
    public boolean updateExternalFile(int id, String name, String fileLink, int typeID) {
        return false;
    }

    @Override
    public boolean deleteExternalFile(int id) {
        return false;
    }

    @Override
    public ExternalFile checkDuplicate(String name) {
        return null;
    }

    @Override
    public List<ShowExternalFileModel> getAllAvailableFile(int pageNo, int pageSize, String sortBy, boolean sortType) {
        return null;
    }
}
