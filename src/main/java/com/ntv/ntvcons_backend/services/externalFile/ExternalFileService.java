package com.ntv.ntvcons_backend.services.externalFile;

import com.ntv.ntvcons_backend.entities.externalFileModel.ShowExternalFileModel;

import java.util.Collection;
import java.util.List;

public interface ExternalFileService {
    /* CREATE */
    ExternalFile createExternalFile(String fileName, String fileLink, int fileTypeId);

    /* READ */
    ExternalFile checkDuplicateByFileName(String fileName);

    List<ShowExternalFileModel> getAllWithPagingAndSorting
            (int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ExternalFile> getAllByFileTypeId(int fileTypeId);

    List<ExternalFile> getAllByFileTypeIdIn(Collection<Integer> fileTypeIdCollection);

    List<ExternalFile> getAllByNameLike(String fileName);

    List<ExternalFile> getAllByIdIn(Collection<Integer> fileIdCollection);

    ExternalFile getById(int fileId);

    /* UPDATE */
    ExternalFile updateExternalFile(int id, String name, String fileLink, int typeID);

    /* DELETE */
    boolean deleteExternalFile(int id);

}
