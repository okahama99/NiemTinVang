package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ExternalFileEntityWrapperPairing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalFileEntityWrapperPairingRepository
        extends JpaRepository<ExternalFileEntityWrapperPairing, Integer> {
    List<ExternalFileEntityWrapperPairing> findAllByIsDeletedFalse();


    /* Id */
    Optional<ExternalFileEntityWrapperPairing> findByPairingIdAndIsDeletedIsFalse(int pairingId);
    List<ExternalFileEntityWrapperPairing> findAllByPairingIdInAndIsDeletedIsFalse
            (Collection<Integer> pairingIdCollection);


    /* entityWrapperId */
    List<ExternalFileEntityWrapperPairing> findAllByEntityWrapperIdAndIsDeletedIsFalse(int entityWrapperId);
    List<ExternalFileEntityWrapperPairing> findAllByEntityWrapperIdInAndIsDeletedIsFalse
            (Collection<Integer> entityWrapperIdCollection);


    /* externalFileId */
    List<ExternalFileEntityWrapperPairing> findAllByExternalFileIdAndIsDeletedIsFalse(int externalFileId);
    List<ExternalFileEntityWrapperPairing> findAllByExternalFileIdInAndIsDeletedIsFalse
            (Collection<Integer> externalFileIdCollection);
}
