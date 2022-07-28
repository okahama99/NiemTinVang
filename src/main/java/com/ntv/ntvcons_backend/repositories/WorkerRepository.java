package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
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
        Page<Worker> findAllByStatusNotIn
                (Collection<Status> statusCollection, Pageable paging);


        /* Id */
        boolean existsByWorkerIdAndStatusNotIn
                (long workerId, Collection<Status> statusCollection);
        Optional<Worker> findByWorkerIdAndStatusNotIn
                (long workerId, Collection<Status> statusCollection);
        boolean existsAllByWorkerIdInAndStatusNotIn
                (Collection<Long> workerIdCollection, Collection<Status> statusCollection);
        List<Worker> findAllByWorkerIdInAndStatusNotIn
                (Collection<Long> workerIdCollection, Collection<Status> statusCollection);
        /* Id & citizenId */
        boolean existsByCitizenIdAndWorkerIdIsNotAndStatusNotIn
                (String citizenId, long workerId, Collection<Status> statusCollection);


        /* fullName */
        List<Worker> findAllByFullNameAndStatusNotIn
                (String fullName, Collection<Status> statusCollection);
        Page<Worker> findAllByFullNameAndStatusNotIn
                (String fullName, Collection<Status> statusCollection, Pageable paging);
        List<Worker> findAllByFullNameContainsAndStatusNotIn
                (String fullName, Collection<Status> statusCollection);
        Page<Worker> findAllByFullNameContainsAndStatusNotIn
                (String fullName, Collection<Status> statusCollection, Pageable paging);


        /* citizenId */
        boolean existsByCitizenIdAndStatusNotIn
                (String citizenId, Collection<Status> statusCollection);
        Optional<Worker> findByCitizenIdAndStatusNotIn
                (String citizenId, Collection<Status> statusCollection);
        List<Worker> findAllByCitizenIdContainsAndStatusNotIn
                (String citizenId, Collection<Status> statusCollection);
        Page<Worker> findAllByCitizenIdContainsAndStatusNotIn
                (String citizenId, Collection<Status> statusCollection, Pageable paging);


        /* socialSecurityCode */
        Optional<Worker> findBySocialSecurityCodeAndStatusNotIn
                (String socialSecurityCode, Collection<Status> statusCollection);
        List<Worker> findAllBySocialSecurityCodeContainsAndStatusNotIn
                (String socialSecurityCode, Collection<Status> statusCollection);
        Page<Worker> findAllBySocialSecurityCodeContainsAndStatusNotIn
                (String socialSecurityCode, Collection<Status> statusCollection, Pageable paging);


        /* addressId */
        List<Worker> findAllByAddressIdAndStatusNotIn
                (long addressId, Collection<Status> statusCollection);
        Page<Worker> findAllByAddressIdAndStatusNotIn
                (long addressId, Collection<Status> statusCollection, Pageable paging);
        List<Worker> findAllByAddressIdInAndStatusNotIn
                (Collection<Long> addressIdCollection, Collection<Status> statusCollection);
        Page<Worker> findAllByAddressIdInAndStatusNotIn
                (Collection<Long> addressIdCollection, Collection<Status> statusCollection, Pageable paging);
}
