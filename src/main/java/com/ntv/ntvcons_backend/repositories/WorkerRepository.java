package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
        Page<Worker> findAllByIsDeletedIsFalse(Pageable pageable);


        /* Id */
        Optional<Worker> findByWorkerIdAndIsDeletedIsFalse(long workerId);
        List<Worker> findAllByWorkerIdInAndIsDeletedIsFalse(Collection<Long> workerIdCollection);


        /* fullName */
        Optional<Worker> findByFullNameAndIsDeletedIsFalse(String fullName);
        List<Worker> findByFullNameContainsAndIsDeletedIsFalse(String fullName);


        /* citizenId */
        Optional<Worker> findByCitizenIdAndIsDeletedIsFalse(String citizenId);
        List<Worker> findByCitizenIdContainsAndIsDeletedIsFalse(String citizenId);


        /* socialSecurityCode */
        Optional<Worker> findBySocialSecurityCodeAndIsDeletedIsFalse(String socialSecurityCode);
        List<Worker> findBySocialSecurityCodeContainsAndIsDeletedIsFalse(String socialSecurityCode);


        /* addressId */
        List<Worker> findAllByAddressIdAndIsDeletedIsFalse(long addressId);
        List<Worker> findAllByAddressIdInAndIsDeletedIsFalse(Collection<Long> addressIdCollection);
}
