package com.example.ntv.Services.ExternalFile;

import com.example.ntv.Database.Entities.ExternalFile;
import com.example.ntv.Database.ExternalFileModel.ShowExternalFileModel;

import java.util.List;

public interface ExternalFileService {
    ExternalFile createExternalFile(String name, String fileLink, int typeID);

    boolean updateExternalFile(int id, String name, String fileLink, int typeID);

    boolean deleteExternalFile(int id);

    ExternalFile checkDuplicate(String name);

    List<ShowExternalFileModel> getAllAvailableFile(int pageNo, int pageSize, String sortBy, boolean sortType);
}
