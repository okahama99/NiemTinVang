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
    List<ExternalFile> findAllByStatusNotIn
            (Collection<Status> statusCollection);
    Page<ExternalFile> findAllByStatusNotIn
            (Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByFileIdAndStatusNotIn
            (long fileId, Collection<Status> statusCollection);
    Optional<ExternalFile> findByFileIdAndStatusNotIn
            (long fileId, Collection<Status> statusCollection);
    boolean existsAllByFileIdInAndStatusNotIn
            (Collection<Long> fileIdCollection, Collection<Status> statusCollection);
    List<ExternalFile> findAllByFileIdInAndStatusNotIn
            (Collection<Long> fileIdCollection, Collection<Status> statusCollection);
    /* Id & fileLink */
    /** Check duplicate for Update */
    boolean existsByFileLinkAndFileIdNotAndStatusNotIn
            (String fileLink, long fileId, Collection<Status> statusCollection);


    /* fileName */
    Optional<ExternalFile> findByFileNameAndStatusNotIn
            (String fileName, Collection<Status> statusCollection);
    List<ExternalFile> findAllByFileNameContainsAndStatusNotIn
            (String fileName, Collection<Status> statusCollection);
    Page<ExternalFile> findAllByFileNameContainsAndStatusNotIn
            (String fileName, Pageable paging, Collection<Status> statusCollection);


    /* fileLink */
    /** Check duplicate for Create */
    boolean existsByFileLinkAndStatusNotIn
            (String fileLink, Collection<Status> statusCollection);
    Optional<ExternalFile> findByFileLinkAndStatusNotIn
            (String fileLink, Collection<Status> statusCollection);
    List<ExternalFile> findAllByFileLinkContainsAndStatusNotIn
            (String fileLink, Collection<Status> statusCollection);
    Page<ExternalFile> findAllByFileLinkContainsAndStatusNotIn
            (String fileLink, Pageable paging, Collection<Status> statusCollection);


    /* filetypeId */
    List<ExternalFile> findAllByFileTypeIdAndStatusNotIn
            (long filetypeId, Collection<Status> statusCollection);
    Page<ExternalFile> findAllByFileTypeIdAndStatusNotIn
            (long filetypeId, Pageable paging, Collection<Status> statusCollection);
    List<ExternalFile> findAllByFileTypeIdInAndStatusNotIn
            (Collection<Long> fileTypeIdCollection, Collection<Status> statusCollection);
    Page<ExternalFile> findAllByFileTypeIdInAndStatusNotIn
            (Collection<Long> fileTypeIdCollection, Pageable paging, Collection<Status> statusCollection);
}
