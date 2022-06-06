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
    Page<ExternalFileEntityWrapperPairing> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<ExternalFileEntityWrapperPairing> findByPairingIdAndIsDeletedIsFalse(long pairingId);
    List<ExternalFileEntityWrapperPairing> findAllByPairingIdInAndIsDeletedIsFalse
            (Collection<Long> pairingIdCollection);


    /* entityWrapperId */
    List<ExternalFileEntityWrapperPairing> findAllByEntityWrapperIdAndIsDeletedIsFalse(long entityWrapperId);
    List<ExternalFileEntityWrapperPairing> findAllByEntityWrapperIdInAndIsDeletedIsFalse
            (Collection<Long> entityWrapperIdCollection);


    /* externalFileId */
    List<ExternalFileEntityWrapperPairing> findAllByExternalFileIdAndIsDeletedIsFalse(long externalFileId);
    List<ExternalFileEntityWrapperPairing> findAllByExternalFileIdInAndIsDeletedIsFalse
            (Collection<Long> externalFileIdCollection);
}
