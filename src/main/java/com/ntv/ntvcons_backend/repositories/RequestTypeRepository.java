package com.ntv.ntvcons_backend.repositories;

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
    Page<RequestType> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    boolean existsByRequestTypeIdAndIsDeletedIsFalse(long requestTypeId);
    Optional<RequestType> findByRequestTypeIdAndIsDeletedIsFalse(long requestTypeId);
    boolean existsAllByRequestTypeIdInAndIsDeletedIsFalse(Collection<Long> requestTypeIdCollection);
    List<RequestType> findAllByRequestTypeIdInAndIsDeletedIsFalse(Collection<Long> requestTypeIdCollection);
    /* Id & requestTypeName */
    /** Check duplicate requestTypeName for update */
    boolean existsByRequestTypeNameAndRequestTypeIdIsNotAndIsDeletedIsFalse(String requestTypeName, long requestTypeId);


    /* requestTypeName */
    boolean existsByRequestTypeNameAndIsDeletedIsFalse(String requestTypeName);
    Optional<RequestType> findAllByRequestTypeNameAndIsDeletedIsFalse(String requestTypeName);
    List<RequestType> findAllByRequestTypeNameContainsAndIsDeletedIsFalse(String requestTypeName);
}
