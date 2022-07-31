package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.ExternalFileEntityWrapperPairing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalFileEntityWrapperPairingRepository
        extends JpaRepository<ExternalFileEntityWrapperPairing, Long> {
    List<ExternalFileEntityWrapperPairing> findAllByStatusNotIn(
            Collection<Status> statusCollection);
    Page<ExternalFileEntityWrapperPairing> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByPairingIdAndStatusNotIn(
            long pairingId, Collection<Status> statusCollection);
    Optional<ExternalFileEntityWrapperPairing> findByPairingIdAndStatusNotIn(
            long pairingId, Collection<Status> statusCollection);
    List<ExternalFileEntityWrapperPairing> findAllByPairingIdInAndStatusNotIn(
            Collection<Long> pairingIdCollection, Collection<Status> statusCollection);
    /* Id & entityWrapperId & externalFileId */
    /** Check duplicate for Update */
    boolean existsByEntityWrapperIdAndExternalFileIdAndPairingIdIsNotAndStatusNotIn(
            long entityWrapperId, long externalFileId, long pairingId, Collection<Status> statusCollection);


    /* entityWrapperId */
    List<ExternalFileEntityWrapperPairing> findAllByEntityWrapperIdAndStatusNotIn(
            long entityWrapperId, Collection<Status> statusCollection);
    List<ExternalFileEntityWrapperPairing> findAllByEntityWrapperIdInAndStatusNotIn(
            Collection<Long> entityWrapperIdCollection, Collection<Status> statusCollection);


    /* externalFileId */
    List<ExternalFileEntityWrapperPairing> findAllByExternalFileIdAndStatusNotIn(
            long externalFileId, Collection<Status> statusCollection);
    List<ExternalFileEntityWrapperPairing> findAllByExternalFileIdInAndStatusNotIn(
            Collection<Long> externalFileIdCollection, Collection<Status> statusCollection);
    /* entityWrapperId & externalFileId */
    /** Check duplicate for Create */
    boolean existsByEntityWrapperIdAndExternalFileIdAndStatusNotIn(
            long entityWrapperId, long externalFileId, Collection<Status> statusCollection);
}
