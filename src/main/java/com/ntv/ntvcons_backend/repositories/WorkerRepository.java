package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {
        List<Worker> findAllByIsDeletedFalse();


        /* Id */
        Optional<Worker> findByWorkerIdAndIsDeletedIsFalse(int workerId);
        List<Worker> findAllByWorkerIdInAndIsDeletedIsFalse(Collection<Integer> workerIdCollection);


        /* fullName */
        Optional<Worker> findByFullNameAndIsDeletedIsFalse(String fullName);
        List<Worker> findByFullNameLikeAndIsDeletedIsFalse(String fullName);


        /* citizenId */
        Optional<Worker> findByCitizenIdAndIsDeletedIsFalse(String citizenId);
        List<Worker> findByCitizenIdLikeAndIsDeletedIsFalse(String citizenId);


        /* socialSecurityCode */
        Optional<Worker> findBySocialSecurityCodeAndIsDeletedIsFalse(String socialSecurityCode);
        List<Worker> findBySocialSecurityCodeLikeAndIsDeletedIsFalse(String socialSecurityCode);


        /* addressId */
        List<Worker> findAllByAddressIdAndIsDeletedIsFalse(int addressId);
        List<Worker> findAllByAddressIdInAndIsDeletedIsFalse(Collection<Integer> addressIdCollection);
}
