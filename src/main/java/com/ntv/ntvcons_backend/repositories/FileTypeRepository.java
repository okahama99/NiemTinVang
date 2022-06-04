package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.FileType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileTypeRepository extends CrudRepository<FileType, Integer> {
    FileType findByFileTypeNameLike (String type);

    FileType findByFileTypeDescLike (String description);

    FileType findByFileTypeExtension (String extension);
}
