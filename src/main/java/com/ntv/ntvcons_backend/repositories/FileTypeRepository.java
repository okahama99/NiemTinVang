package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileTypeRepository extends JpaRepository<FileType, Integer> {
    List<FileType> findAllByIsDeletedFalse();


    /* Id */
    Optional<FileType> findByFileTypeIdAndIsDeletedIsFalse(int fileTypeId);
    List<FileType> findAllByFileTypeIdInAndIsDeletedIsFalse(Collection<Integer> fileTypeIdCollection);


    /* fileTypeName */
    Optional<FileType> findByFileTypeNameAndIsDeletedIsFalse(String fileTypeName);
    List<FileType> findAllByFileTypeNameLikeAndIsDeletedIsFalse(String fileTypeName);


    /* fileTypeExtension */
    Optional<FileType> findByFileTypeExtensionAndIsDeletedIsFalse(String fileTypeExtension);
    List<FileType> findAllByFileTypeExtensionLikeAndIsDeletedIsFalse(String fileTypeExtension);
    List<FileType> findAllByFileTypeExtensionInAndIsDeletedIsFalse(Collection<String> fileTypeExtensionCollection);
}
