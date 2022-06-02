package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestTypeRepository extends JpaRepository<RequestType, Integer> {
    List<RequestType> findAllByIsDeletedFalse();


    /* Id */
    Optional<RequestType> findByRequestTypeIdAndIsDeletedIsFalse(int requestTypeId);
    List<RequestType> findAllByRequestTypeIdInAndIsDeletedIsFalse(Collection<Integer> requestTypeIdCollection);


    /* requestTypeName */
    Optional<RequestType> findAllByRequestTypeNameAndIsDeletedIsFalse(String requestTypeName);
    List<RequestType> findAllByRequestTypeNameLikeAndIsDeletedIsFalse(String requestTypeName);
}
