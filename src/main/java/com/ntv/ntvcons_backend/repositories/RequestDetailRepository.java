package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.RequestDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {
    Page<RequestDetail> findAllByIsDeletedIsFalse(Pageable paging);


    /* Id */
    Optional<RequestDetail> findByRequestDetailIdAndIsDeletedIsFalse(long requestDetailId);
    List<RequestDetail> findAllByRequestDetailIdInAndIsDeletedIsFalse(Collection<Long> requestDetailIdCollection);
    /* Id & requestId & itemDesc & itemPrice */
    boolean existsByRequestIdAndItemDescAndItemPriceAndRequestDetailIdIsNotAndIsDeletedIsFalse
            (long requestId, String itemDesc, double itemPrice, long requestDetailId);


    /* requestId */
    List<RequestDetail> findAllByRequestIdAndIsDeletedIsFalse(long requestId);
    List<RequestDetail> findAllByRequestIdInAndIsDeletedIsFalse(Collection<Long> requestIdCollection);


    /* Id & requestId & itemDesc & itemPrice */
    boolean existsByRequestIdAndItemDescAndItemPriceAndIsDeletedIsFalse(long requestId, String itemDesc, double itemPrice);
}
