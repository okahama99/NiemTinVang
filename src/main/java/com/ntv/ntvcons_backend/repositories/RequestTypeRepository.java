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
    Optional<RequestType> findByRequestTypeIdAndIsDeletedIsFalse(long requestTypeId);
    List<RequestType> findAllByRequestTypeIdInAndIsDeletedIsFalse(Collection<Long> requestTypeIdCollection);


    /* requestTypeName */
    Optional<RequestType> findAllByRequestTypeNameAndIsDeletedIsFalse(String requestTypeName);
    List<RequestType> findAllByRequestTypeNameLikeAndIsDeletedIsFalse(String requestTypeName);
}
