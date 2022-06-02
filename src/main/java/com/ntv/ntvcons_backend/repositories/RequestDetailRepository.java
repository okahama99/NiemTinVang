package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail, Integer> {
    List<RequestDetail> findAllByIsDeletedFalse();


    /* Id */
    Optional<RequestDetail> findByRequestDetailIdAndIsDeletedIsFalse(int requestDetailId);
    List<RequestDetail> findAllByRequestDetailIdInAndIsDeletedIsFalse(Collection<Integer> requestDetailIdCollection);


    /* requestId */
    List<RequestDetail> findAllByRequestIdAndIsDeletedIsFalse(int requestId);
    List<RequestDetail> findAllByRequestIdInAndIsDeletedIsFalse(Collection<Integer> requestIdCollection);
}
