package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
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
    List<ExternalFile> findAllByStatusNot(Status status);
    Page<ExternalFile> findAllByStatusNot(Status status, Pageable paging);


    /* Id */
    boolean existsByFileIdAndStatusNot(long fileId, Status status);
    Optional<ExternalFile> findByFileIdAndStatusNot(long fileId, Status status);
    boolean existsAllByFileIdInAndStatusNot(Collection<Long> fileIdCollection, Status status);
    List<ExternalFile> findAllByFileIdInAndStatusNot(Collection<Long> fileIdCollection, Status status);
    /* Id & fileLink */
    boolean existsByFileLinkAndFileIdNotAndStatusNot(String fileLink, long fileId, Status status);


    /* fileName */
    Optional<ExternalFile> findByFileNameAndStatusNot(String fileName, Status status);
    List<ExternalFile> findAllByFileNameContainsAndStatusNot(String fileName, Status status);
    Page<ExternalFile> findAllByFileNameContainsAndStatusNot(String fileName, Pageable paging, Status status);


    /* fileLink */
    boolean existsByFileLinkAndStatusNot(String fileLink, Status status);
    Optional<ExternalFile> findByFileLinkAndStatusNot(String fileLink, Status status);
    List<ExternalFile> findAllByFileLinkContainsAndStatusNot(String fileLink, Status status);
    Page<ExternalFile> findAllByFileLinkContainsAndStatusNot(String fileLink, Pageable paging, Status status);


    /* filetypeId */
    List<ExternalFile> findAllByFileTypeIdAndStatusNot(long filetypeId, Status status);
    Page<ExternalFile> findAllByFileTypeIdAndStatusNot(long filetypeId, Pageable paging, Status status);
    List<ExternalFile> findAllByFileTypeIdInAndStatusNot(Collection<Long> fileTypeIdCollection, Status status);
    Page<ExternalFile> findAllByFileTypeIdInAndStatusNot(Collection<Long> fileTypeIdCollection, Pageable paging, Status status);
}
