package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.FileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileTypeRepository extends JpaRepository<FileType, Long> {
    Page<FileType> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<FileType> findByFileTypeIdAndIsDeletedIsFalse(long fileTypeId);
    List<FileType> findAllByFileTypeIdInAndIsDeletedIsFalse(Collection<Long> fileTypeIdCollection);


    /* fileTypeName */
    Optional<FileType> findByFileTypeNameAndIsDeletedIsFalse(String fileTypeName);
    List<FileType> findAllByFileTypeNameContainsAndIsDeletedIsFalse(String fileTypeName);


    /* fileTypeExtension */
    Optional<FileType> findByFileTypeExtensionAndIsDeletedIsFalse(String fileTypeExtension);
    List<FileType> findAllByFileTypeExtensionContainsAndIsDeletedIsFalse(String fileTypeExtension);
    List<FileType> findAllByFileTypeExtensionInAndIsDeletedIsFalse(Collection<String> fileTypeExtensionCollection);
}
