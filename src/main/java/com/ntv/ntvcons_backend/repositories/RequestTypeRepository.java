package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.RequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestTypeRepository extends JpaRepository<RequestType, Long> {
    Page<RequestType> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByRequestTypeIdAndStatusNotIn(
            long requestTypeId, Collection<Status> statusCollection);
    Optional<RequestType> findByRequestTypeIdAndStatusNotIn(
            long requestTypeId, Collection<Status> statusCollection);
    boolean existsAllByRequestTypeIdInAndStatusNotIn(
            Collection<Long> requestTypeIdCollection, Collection<Status> statusCollection);
    List<RequestType> findAllByRequestTypeIdInAndStatusNotIn(
            Collection<Long> requestTypeIdCollection, Collection<Status> statusCollection);
    /* Id & requestTypeName */
    /** Check duplicate requestTypeName for update */
    boolean existsByRequestTypeNameAndRequestTypeIdIsNotAndStatusNotIn(
            String requestTypeName, long requestTypeId, Collection<Status> statusCollection);


    /* requestTypeName */
    boolean existsByRequestTypeNameAndStatusNotIn(
            String requestTypeName, Collection<Status> statusCollection);
    Optional<RequestType> findByRequestTypeNameAndStatusNotIn(
            String requestTypeName, Collection<Status> statusCollection);
    List<RequestType> findAllByRequestTypeNameContainsAndStatusNotIn(
            String requestTypeName, Collection<Status> statusCollection);
    Page<RequestType> findAllByRequestTypeNameContainsAndStatusNotIn(
            String requestTypeName, Collection<Status> statusCollection, Pageable paging);
}
