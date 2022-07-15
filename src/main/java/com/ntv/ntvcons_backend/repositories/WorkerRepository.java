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
        Page<Worker> findAllByIsDeletedIsFalse(Pageable paging);


        /* Id */
        boolean existsByWorkerIdAndIsDeletedIsFalse(long workerId);
        Optional<Worker> findByWorkerIdAndIsDeletedIsFalse(long workerId);
        boolean existsAllByWorkerIdInAndIsDeletedIsFalse(Collection<Long> workerIdCollection);
        List<Worker> findAllByWorkerIdInAndIsDeletedIsFalse(Collection<Long> workerIdCollection);
        /* Id & citizenId */
        boolean existsByCitizenIdAndWorkerIdIsNotAndIsDeletedIsFalse(String citizenId, long workerId);


        /* fullName */
        List<Worker> findAllByFullNameAndIsDeletedIsFalse(String fullName);
        Page<Worker> findAllByFullNameAndIsDeletedIsFalse(String fullName, Pageable paging);
        List<Worker> findAllByFullNameContainsAndIsDeletedIsFalse(String fullName);
        Page<Worker> findAllByFullNameContainsAndIsDeletedIsFalse(String fullName, Pageable paging);


        /* citizenId */
        boolean existsByCitizenIdAndIsDeletedIsFalse(String citizenId);
        Optional<Worker> findByCitizenIdAndIsDeletedIsFalse(String citizenId);
        List<Worker> findAllByCitizenIdContainsAndIsDeletedIsFalse(String citizenId);
        Page<Worker> findAllByCitizenIdContainsAndIsDeletedIsFalse(String citizenId, Pageable paging);


        /* socialSecurityCode */
        Optional<Worker> findBySocialSecurityCodeAndIsDeletedIsFalse(String socialSecurityCode);
        List<Worker> findAllBySocialSecurityCodeContainsAndIsDeletedIsFalse(String socialSecurityCode);
        Page<Worker> findAllBySocialSecurityCodeContainsAndIsDeletedIsFalse(String socialSecurityCode, Pageable paging);


        /* addressId */
        List<Worker> findAllByAddressIdAndIsDeletedIsFalse(long addressId);
        Page<Worker> findAllByAddressIdAndIsDeletedIsFalse(long addressId, Pageable paging);
        List<Worker> findAllByAddressIdInAndIsDeletedIsFalse(Collection<Long> addressIdCollection);
        Page<Worker> findAllByAddressIdInAndIsDeletedIsFalse(Collection<Long> addressIdCollection, Pageable paging);
}
