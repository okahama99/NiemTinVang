package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileTypeRepository extends JpaRepository<FileType, Integer> {
    FileType findByFileTypeNameLike (String type);

    FileType findByFileTypeDescLike (String description);

    FileType findByFileTypeExtension (String extension);
}
