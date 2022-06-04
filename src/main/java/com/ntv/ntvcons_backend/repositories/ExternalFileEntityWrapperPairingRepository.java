package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ExternalFileEntityWrapperPairing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalFileEntityWrapperPairingRepository
        extends CrudRepository<ExternalFileEntityWrapperPairing, Integer> {


}
