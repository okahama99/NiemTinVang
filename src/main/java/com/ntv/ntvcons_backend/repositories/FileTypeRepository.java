package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
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
    Page<FileType> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    Optional<FileType> findByFileTypeIdAndStatusNotIn(
            long fileTypeId, Collection<Status> statusCollection);
    List<FileType> findAllByFileTypeIdInAndStatusNotIn(
            Collection<Long> fileTypeIdCollection, Collection<Status> statusCollection);
    Page<FileType> findAllByFileTypeIdInAndStatusNotIn(
            Collection<Long> fileTypeIdCollection, Collection<Status> statusCollection, Pageable paging);
    /* Id & fileTypeName */
    /** Check duplicate for Update */
    boolean existsByFileTypeNameAndFileTypeIdIsNotAndStatusNotIn(
            String fileTypeName, long fileTypeId, Collection<Status> statusCollection);


    /* fileTypeName */
    /** Check duplicate for Create */
    boolean existsByFileTypeNameAndStatusNotIn(
            String fileTypeName, Collection<Status> statusCollection);
    Optional<FileType> findByFileTypeNameAndStatusNotIn(
            String fileTypeName, Collection<Status> statusCollection);
    List<FileType> findAllByFileTypeNameContainsAndStatusNotIn(
            String fileTypeName, Collection<Status> statusCollection);
    Page<FileType> findAllByFileTypeNameContainsAndStatusNotIn(
            String fileTypeName, Collection<Status> statusCollection, Pageable paging);
}
