package com.ntv.ntvcons_backend.repositories;

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
    List<ExternalFileEntityWrapperPairing> findAllByStatusNotContains(String status);
    Page<ExternalFileEntityWrapperPairing> findAllByStatusNotContains(String status, Pageable paging);


    /* Id */
    boolean existsByPairingIdAndStatusNotContains(long pairingId, String status);
    Optional<ExternalFileEntityWrapperPairing> findByPairingIdAndStatusNotContains(long pairingId, String status);
    List<ExternalFileEntityWrapperPairing> findAllByPairingIdInAndStatusNotContains
            (Collection<Long> pairingIdCollection, String status);
    /* Id & entityWrapperId & externalFileId */
    boolean existsByEntityWrapperIdAndExternalFileIdAndPairingIdIsNotAndStatusNotContains
            (long entityWrapperId, long externalFileId, long pairingId, String status);


    /* entityWrapperId */
    List<ExternalFileEntityWrapperPairing> findAllByEntityWrapperIdAndStatusNotContains(long entityWrapperId, String status);
    List<ExternalFileEntityWrapperPairing> findAllByEntityWrapperIdInAndStatusNotContains
            (Collection<Long> entityWrapperIdCollection, String status);


    /* externalFileId */
    List<ExternalFileEntityWrapperPairing> findAllByExternalFileIdAndStatusNotContains(long externalFileId, String status);
    List<ExternalFileEntityWrapperPairing> findAllByExternalFileIdInAndStatusNotContains
            (Collection<Long> externalFileIdCollection, String status);
    /* entityWrapperId & externalFileId */
    boolean existsByEntityWrapperIdAndExternalFileIdAndStatusNotContains
            (long entityWrapperId, long externalFileId, String status);
}
