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
    List<ExternalFileEntityWrapperPairing> findAllByStatusNot(Status status);
    Page<ExternalFileEntityWrapperPairing> findAllByStatusNot(Status status, Pageable paging);


    /* Id */
    boolean existsByPairingIdAndStatusNot(long pairingId, Status status);
    Optional<ExternalFileEntityWrapperPairing> findByPairingIdAndStatusNot(long pairingId, Status status);
    List<ExternalFileEntityWrapperPairing> findAllByPairingIdInAndStatusNot
            (Collection<Long> pairingIdCollection, Status status);
    /* Id & entityWrapperId & externalFileId */
    boolean existsByEntityWrapperIdAndExternalFileIdAndPairingIdIsNotAndStatusNot
            (long entityWrapperId, long externalFileId, long pairingId, Status status);


    /* entityWrapperId */
    List<ExternalFileEntityWrapperPairing> findAllByEntityWrapperIdAndStatusNot(long entityWrapperId, Status status);
    List<ExternalFileEntityWrapperPairing> findAllByEntityWrapperIdInAndStatusNot
            (Collection<Long> entityWrapperIdCollection, Status status);


    /* externalFileId */
    List<ExternalFileEntityWrapperPairing> findAllByExternalFileIdAndStatusNot(long externalFileId, Status status);
    List<ExternalFileEntityWrapperPairing> findAllByExternalFileIdInAndStatusNot
            (Collection<Long> externalFileIdCollection, Status status);
    /* entityWrapperId & externalFileId */
    boolean existsByEntityWrapperIdAndExternalFileIdAndStatusNot
            (long entityWrapperId, long externalFileId, Status status);
}
