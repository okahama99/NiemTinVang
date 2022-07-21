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
    List<ExternalFile> findAllByStatusNotContains(String status);
    Page<ExternalFile> findAllByStatusNotContains(String status, Pageable paging);


    /* Id */
    boolean existsByFileIdAndStatusNotContains(long fileId, String status);
    Optional<ExternalFile> findByFileIdAndStatusNotContains(long fileId, String status);
    boolean existsAllByFileIdInAndStatusNotContains(Collection<Long> fileIdCollection, String status);
    List<ExternalFile> findAllByFileIdInAndStatusNotContains(Collection<Long> fileIdCollection, String status);
    /* Id & fileLink */
    boolean existsByFileLinkAndFileIdNotAndStatusNotContains(String fileLink, long fileId, String status);


    /* fileName */
    Optional<ExternalFile> findByFileNameAndStatusNotContains(String fileName, String status);
    List<ExternalFile> findAllByFileNameContainsAndStatusNotContains(String fileName, String status);
    Page<ExternalFile> findAllByFileNameContainsAndStatusNotContains(String fileName, Pageable paging, String status);


    /* fileLink */
    boolean existsByFileLinkAndStatusNotContains(String fileLink, String status);
    Optional<ExternalFile> findByFileLinkAndStatusNotContains(String fileLink, String status);
    List<ExternalFile> findAllByFileLinkContainsAndStatusNotContains(String fileLink, String status);
    Page<ExternalFile> findAllByFileLinkContainsAndStatusNotContains(String fileLink, Pageable paging, String status);


    /* filetypeId */
    List<ExternalFile> findAllByFileTypeIdAndStatusNotContains(long filetypeId, String status);
    Page<ExternalFile> findAllByFileTypeIdAndStatusNotContains(long filetypeId, Pageable paging, String status);
    List<ExternalFile> findAllByFileTypeIdInAndStatusNotContains(Collection<Long> fileTypeIdCollection, String status);
    Page<ExternalFile> findAllByFileTypeIdInAndStatusNotContains(Collection<Long> fileTypeIdCollection, Pageable paging, String status);
}
