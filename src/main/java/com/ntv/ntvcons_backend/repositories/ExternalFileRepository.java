package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.ExternalFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalFileRepository extends JpaRepository<ExternalFile, Long> {
    List<ExternalFile> findAllByStatusNotIn(
            Collection<Status> statusCollection);
    Page<ExternalFile> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByFileIdAndStatusNotIn(
            long fileId, Collection<Status> statusCollection);
    Optional<ExternalFile> findByFileIdAndStatusNotIn(
            long fileId, Collection<Status> statusCollection);
    boolean existsAllByFileIdInAndStatusNotIn(
            Collection<Long> fileIdCollection, Collection<Status> statusCollection);
    List<ExternalFile> findAllByFileIdInAndStatusNotIn(
            Collection<Long> fileIdCollection, Collection<Status> statusCollection);
    /* !Id & (fileName || fileLink) */
    /** Check duplicate for Update */
    @Query("SELECT case when count(eF) > 0 then true else false end " +
            "FROM ExternalFile eF " +
            "WHERE (eF.fileNameFirebase = ?1 or eF.fileLink = ?2) and eF.fileId <> ?3 and eF.status not in ?4 ")
    boolean existsByFileNameFirebaseOrFileLinkAndFileIdIsNotAndStatusNotIn(
            String fileNameFirebase, String fileLink, long fileId, Collection<Status> statusCollection);


    /* fileName */
    boolean existsByFileNameAndStatusNotIn(
            String fileName, Collection<Status> statusCollection);
    Optional<ExternalFile> findByFileNameAndStatusNotIn(
            String fileName, Collection<Status> statusCollection);
    List<ExternalFile> findAllByFileNameContainsAndStatusNotIn(
            String fileName, Collection<Status> statusCollection);
    Page<ExternalFile> findAllByFileNameContainsAndStatusNotIn(
            String fileName, Collection<Status> statusCollection, Pageable paging);


    /* fileLink */
    boolean existsByFileLinkAndStatusNotIn(
            String fileLink, Collection<Status> statusCollection);
    Optional<ExternalFile> findByFileLinkAndStatusNotIn(
            String fileLink, Collection<Status> statusCollection);
    List<ExternalFile> findAllByFileLinkContainsAndStatusNotIn(
            String fileLink, Collection<Status> statusCollection);
    Page<ExternalFile> findAllByFileLinkContainsAndStatusNotIn(
            String fileLink, Collection<Status> statusCollection, Pageable paging);
    /* fileName || fileLink */
    /** Check duplicate for Create */
    @Query("SELECT case when count(eF) > 0 then true else false end " +
            "FROM ExternalFile eF " +
            "WHERE (eF.fileNameFirebase = ?1 or eF.fileLink = ?2) and eF.status not in ?3 ")
    boolean existsByFileNameFirebaseOrFileLinkAndStatusNotIn(
            String fileNameFirebase, String fileLink, Collection<Status> statusCollection);


    /* filetype */
    List<ExternalFile> findAllByFileTypeAndStatusNotIn(
            FileType filetype, Collection<Status> statusCollection);
    Page<ExternalFile> findAllByFileTypeAndStatusNotIn(
            FileType filetype, Collection<Status> statusCollection, Pageable paging);
}
