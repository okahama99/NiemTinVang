package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ExternalFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalFileRepository extends JpaRepository<ExternalFile, Long> {
    Page<ExternalFile> findAllByIsDeletedIsFalse(Pageable paging);

    /* Id */
    boolean existsByFileIdAndIsDeletedIsFalse(long fileId);
    Optional<ExternalFile> findByFileIdAndIsDeletedIsFalse(long fileId);
    boolean existsAllByFileIdInAndIsDeletedIsFalse(Collection<Long> fileIdCollection);
    List<ExternalFile> findAllByFileIdInAndIsDeletedIsFalse(Collection<Long> fileIdCollection);


    /* fileName */
    Optional<ExternalFile> findByFileNameAndIsDeletedIsFalse(String fileName);
    List<ExternalFile> findAllByFileNameContainsAndIsDeletedIsFalse(String fileName);
    Page<ExternalFile> findAllByFileNameContainsAndIsDeletedIsFalse(String fileName, Pageable paging);


    /* fileLink */
    boolean existsByFileLinkAndIsDeletedIsFalse(String fileLink);
    Optional<ExternalFile> findByFileLinkAndIsDeletedIsFalse(String fileLink);
    List<ExternalFile> findAllByFileLinkContainsAndIsDeletedIsFalse(String fileLink);
    Page<ExternalFile> findAllByFileLinkContainsAndIsDeletedIsFalse(String fileLink, Pageable paging);


    /* filetypeId */
    List<ExternalFile> findAllByFileTypeIdAndIsDeletedIsFalse(long filetypeId);
    Page<ExternalFile> findAllByFileTypeIdAndIsDeletedIsFalse(long filetypeId, Pageable paging);
    List<ExternalFile> findAllByFileTypeIdInAndIsDeletedIsFalse(Collection<Long> fileTypeIdCollection);
    Page<ExternalFile> findAllByFileTypeIdInAndIsDeletedIsFalse(Collection<Long> fileTypeIdCollection, Pageable paging);
}
