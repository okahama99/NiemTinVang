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
    Page<FileType> findAllByFileTypeIdInAndIsDeletedIsFalse(Collection<Long> fileTypeIdCollection, Pageable pageable);
    /* Id & fileTypeName & fileTypeExtension */
    boolean existsByFileTypeNameOrFileTypeExtensionAndFileTypeIdIsNotAndIsDeletedIsFalse
            (String fileTypeName, String fileTypeExtension, long fileTypeId);


    /* fileTypeName */
    Optional<FileType> findByFileTypeNameAndIsDeletedIsFalse(String fileTypeName);
    List<FileType> findAllByFileTypeNameContainsAndIsDeletedIsFalse(String fileTypeName);
    Page<FileType> findAllByFileTypeNameContainsAndIsDeletedIsFalse(String fileTypeName, Pageable pageable);


    /* fileTypeExtension */
    Optional<FileType> findByFileTypeExtensionAndIsDeletedIsFalse(String fileTypeExtension);
    List<FileType> findAllByFileTypeExtensionContainsAndIsDeletedIsFalse(String fileTypeExtension);
    Page<FileType> findAllByFileTypeExtensionContainsAndIsDeletedIsFalse(String fileTypeExtension, Pageable pageable);
    List<FileType> findAllByFileTypeExtensionInAndIsDeletedIsFalse(Collection<String> fileTypeExtensionCollection);
    Page<FileType> findAllByFileTypeExtensionInAndIsDeletedIsFalse(Collection<String> fileTypeExtensionCollection, Pageable pageable);
    /* fileTypeName & fileTypeExtension */
    boolean existsByFileTypeNameOrFileTypeExtensionAndIsDeletedIsFalse(String fileTypeName, String fileTypeExtension);
}
