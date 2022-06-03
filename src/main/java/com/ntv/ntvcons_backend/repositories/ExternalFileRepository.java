package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ExternalFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalFileRepository extends JpaRepository<ExternalFile, Integer> {
    List<ExternalFile> findAllByIsDeletedFalse();

    /* Id */
    Optional<ExternalFile> findByFileIdAndIsDeletedIsFalse(int fileId);
    List<ExternalFile> findAllByFileIdInAndIsDeletedIsFalse(Collection<Integer> fileIdCollection);


    /* fileName */
    Optional<ExternalFile> findByFileNameAndIsDeletedIsFalse(String fileName);
    List<ExternalFile> findAllByFileNameLikeAndIsDeletedIsFalse(String fileName);


    /* fileLink */
    Optional<ExternalFile> findByFileLinkAndIsDeletedIsFalse(String fileLink);
    List<ExternalFile> findAllByFileLinkLikeAndIsDeletedIsFalse(String fileLink);


    /* filetypeId */
    List<ExternalFile> findAllByFileTypeIdAndIsDeletedIsFalse(int filetypeId);
    List<ExternalFile> findAllByFileTypeIdInAndIsDeletedIsFalse(Collection<Integer> fileTypeIdCollection);
}
